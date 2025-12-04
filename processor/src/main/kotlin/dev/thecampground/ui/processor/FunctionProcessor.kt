package dev.thecampground.ui.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.joinToCode
import dev.thecampground.ui.annotation.CampgroundDocComponent
import dev.thecampground.ui.annotation.CampgroundDocComponentProp
import kotlin.system.exitProcess

data class CampgroundDocComponentExampleWrapper(val component: CampgroundDocComponent, val example: String? = null)
class FunctionProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    private val collectedComponents = mutableListOf<Pair<KSFunctionDeclaration, CampgroundDocComponentExampleWrapper>>()
    private var fileAlreadyGenerated = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(
            "dev.thecampground.ui.annotation.CampgroundUIComponent",
            inDepth = true
        )

        val invalid = symbols.filter { !it.validate() }.toList()

        symbols
            .filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(CampgroundUIComponentVisitor(), Unit) }

        if (!fileAlreadyGenerated) {
            generateCombinedFile()
            fileAlreadyGenerated = true
        }

        return invalid
    }

    private fun generateCombinedFile() {
        if (collectedComponents.isEmpty()) return

        val pkg = collectedComponents.first().first.packageName.asString()

        val grouped = collectedComponents.groupBy { (fn, _) ->
            fn.simpleName.asString()
        }

        val file = codeGenerator.createNewFile(
            Dependencies(false, *collectedComponents.map { it.first.containingFile!! }.toTypedArray()),
            pkg,
            "CampgroundUIDocDefinitions",
            "kt"
        )

        val mapEntries = grouped.map { (name, comps) ->
            val componentExprs = comps.map { (_, comp) -> buildComponentExpr(comp) }

            val listBlock = componentExprs
                .map { CodeBlock.of("%L", it) }
                .joinToCode(separator = ",\n")

            CodeBlock.builder()
                .add("\n%S to listOf(\n", name)
                .indent()
                .add(listBlock)
                .unindent()
                .add("\n)")
                .build()
        }

        val examples = mutableListOf<String>()

        for ((_, wrapper) in collectedComponents) {
            if (wrapper.example != null) examples.add(wrapper.example)
        }


        val definitionsProp = PropertySpec.builder(
            "componentDefinitions",
            Map::class.asClassName().parameterizedBy(
                String::class.asClassName(),
                List::class.asClassName().parameterizedBy(
                    ClassName("dev.thecampground.ui.internal", "CampgroundDocComponent")
                )
            )
        )
            .initializer(
                mapEntries.joinToString(prefix = "mapOf(\n", postfix = "\n)") { "%L" },
                *mapEntries.toTypedArray()
            )
            .build()

        val objType = TypeSpec.objectBuilder("CampgroundUIDocDefinitions")
            .addProperty(definitionsProp)
            .build()

        val fileSpec = FileSpec.builder(pkg, "CampgroundUIDocDefinitions")
            .addType(objType)
            .addImport("dev.thecampground.ui.internal", "CampgroundDocComponent")
            .addImport("dev.thecampground.ui.annotation", "CampgroundDocComponentProp")
            .addImport(packageName = "dev.thecampground.ui.examples", names = examples.toTypedArray())
            .build()

        file.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    private fun buildComponentExpr(wrapper: CampgroundDocComponentExampleWrapper): CodeBlock {
        val comp = wrapper.component

        val propsList = comp.props.map { prop ->
            CodeBlock.of(
                "CampgroundDocComponentProp(name = %S, type = %S, default = %L, description = %S)",
                prop.name,
                prop.type,
                prop.default,
                prop.description,
            )
        }

        return CodeBlock.builder()
            .add("CampgroundDocComponent(\n")
            .indent()
            .add("name = %S,\n", comp.name)
            .add("uniqueName = %S,\n", comp.uniqueName)
            .add("description = %S,\n", comp.description)
            .apply {
                if (wrapper.example != null) add("example = { %L() },\n", wrapper.example)
            }
            .add("props = listOf(\n")
            .indent()
            .apply {
                propsList.forEachIndexed { idx, p ->
                    add(p)
                    if (idx != propsList.lastIndex) add(",\n") else add("\n")
                }
            }
            .unindent()
            .add(")\n")
            .unindent()
            .add(")")
            .build()
    }

    inner class CampgroundUIComponentVisitor() : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val comp = generateFunctionDef(function)
            collectedComponents.add(function to comp)
        }
    }

    private fun generateFunctionDef(func: KSFunctionDeclaration): CampgroundDocComponentExampleWrapper {
        val funcName = func.simpleName.asString()
        val paramList = mutableListOf<CampgroundDocComponentProp>()
        val annotation = func.annotations.firstOrNull { ann ->
            ann.annotationType
                .resolve()
                .declaration
                .qualifiedName
                ?.asString() == "dev.thecampground.ui.annotation.CampgroundUIComponent"
        }
        val description = annotation
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "description" }
            ?.value as? String ?: ""

       val uniqueName = annotation
           ?.arguments
           ?.firstOrNull { it.name?.asString() == "uniqueName" }
           ?.value as? String

        // TODO: Probably a better way to see if a component has a unique name.
        if (uniqueName == null || collectedComponents.find { comp -> comp.second.component.uniqueName == uniqueName } != null) {
            println("CampgroundUIComponent \"${funcName}\" MUST have a unique name specified!")
            exitProcess(1)
        }

        func.parameters.forEach { param ->
            paramList.add(generateParamDef(param))
        }

        return CampgroundDocComponentExampleWrapper(
            component = CampgroundDocComponent(
                uniqueName = uniqueName,
                name = funcName,
                description = description,
                props = paramList
            ),
                example = uniqueName
            )
    }

    @OptIn(KspExperimental::class)
    private fun generateParamDef(param: KSValueParameter): CampgroundDocComponentProp {
        val name = param.name!!.asString()
        val default = param.hasDefault
        val typeName = param.type.resolve().declaration.simpleName.asString()
        val annotation = param.annotations.firstOrNull { ann ->
            ann.annotationType
                .resolve()
                .declaration
                .qualifiedName
                ?.asString() == "dev.thecampground.ui.annotation.CampgroundUIComponentProp"
        }
        val description = annotation
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "description" }
            ?.value as? String ?: ""

        return CampgroundDocComponentProp(
            name,
            typeName,
            default,
            description = description
        )
    }
}
class BuilderProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return FunctionProcessor(environment.codeGenerator, environment.logger)
    }
}
