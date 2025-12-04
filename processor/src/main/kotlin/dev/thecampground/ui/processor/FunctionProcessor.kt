package dev.thecampground.ui.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
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
import dev.thecampground.ui.annotation.model.CampgroundComponent
import dev.thecampground.ui.annotation.model.CampgroundProp

class FunctionProcessor(
    val codeGenerator: CodeGenerator,
    @Suppress("unused")
    val logger: KSPLogger
) : SymbolProcessor {
    private val collectedComponents = mutableListOf<Pair<KSFunctionDeclaration, CampgroundComponent>>()
    private var fileAlreadyGenerated = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(
            "dev.thecampground.ui.annotation.CampgroundComponent",
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


        val grouped = collectedComponents.groupBy { (fn, _) ->
            fn.simpleName.asString()
        }

        val file = codeGenerator.createNewFile(
            Dependencies(false, *collectedComponents.map { it.first.containingFile!! }.toTypedArray()),
            PKG,
            "CampgroundComponents",
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


        val definitionsProp = PropertySpec.builder(
            "components",
            Map::class.asClassName().parameterizedBy(
                String::class.asClassName(),
                List::class.asClassName().parameterizedBy(
                    ClassName("dev.thecampground.ui.annotation.model", "CampgroundComponent")
                )
            )
        )
            .initializer(
                mapEntries.joinToString(prefix = "mapOf(\n", postfix = "\n)") { "%L" },
                *mapEntries.toTypedArray()
            )
            .build()

        val objType = TypeSpec.objectBuilder("CampgroundComponents")
            .addProperty(definitionsProp)
            .build()

        val fileSpec = FileSpec.builder(PKG, "CampgroundComponents")
            .addType(objType)
            .addImport("dev.thecampground.ui.annotation.model", "CampgroundComponent")
            .addImport("dev.thecampground.ui.annotation.model", "CampgroundProp")
//            .addImport(packageName = "dev.thecampground.ui.examples", names = examples.toTypedArray())
            .build()

        file.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    private fun buildComponentExpr(comp: CampgroundComponent): CodeBlock {

        val propsList = comp.props.map { prop ->
            CodeBlock.of(
                "CampgroundProp(name = %S, type = %S, default = %L, description = %S)",
                prop.name,
                prop.type,
                prop.default,
                prop.description,
            )
        }

        return CodeBlock.builder()
            .add("CampgroundComponent(\n")
            .indent()
            .add("name = %S,\n", comp.name)
            .add("description = %S,\n", comp.description)
//            .apply {
//                if (wrapper.example != null) add("example = { %L() },\n", wrapper.example)
//            }
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

    private fun generateFunctionDef(func: KSFunctionDeclaration): CampgroundComponent {
        val funcName = func.simpleName.asString()
        val paramList = mutableListOf<CampgroundProp>()
        val annotation = func.getAnnotationOrNull("dev.thecampground.ui.annotation.CampgroundComponent")
        val description = annotation?.getArgumentValueAsString("description") ?: "No description provided."


        func.parameters.forEach { param ->
            paramList.add(generateParamDef(param))
        }

        return CampgroundComponent(
            name = funcName,
            description = description,
            props = paramList
        )
    }

    @OptIn(KspExperimental::class)
    private fun generateParamDef(param: KSValueParameter): CampgroundProp {
        val name = param.name!!.asString()
        val default = param.hasDefault
        val typeName = param.type.resolve().declaration.simpleName.asString()
        val annotation = param.getAnnotationOrNull("dev.thecampground.ui.annotation.CampgroundProp")
        val description = annotation?.getArgumentValueAsString("description") ?: "Not provided."

        return CampgroundProp(
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
