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
import dev.thecampground.ui.annotation.CampgroundDocComponent
import dev.thecampground.ui.annotation.CampgroundDocComponentProp

class FunctionProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    private val collectedComponents = mutableListOf<Pair<KSFunctionDeclaration, CampgroundDocComponent>>()
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

        val file = codeGenerator.createNewFile(
            Dependencies(false, *collectedComponents.map { it.first.containingFile!! }.toTypedArray()),
            pkg,
            "CampgroundUIDocDefinitions",
            "kt"
        )

        val componentExprs = collectedComponents.map { (_, comp) ->
            buildComponentExpr(comp)
        }

        val definitionsProp = PropertySpec.builder(
            "definitions",
            List::class.asClassName().parameterizedBy(
                ClassName("dev.thecampground.ui.annotation", "CampgroundDocComponent")
            )
        )
            .initializer(
                componentExprs.joinToString(prefix = "listOf(\n", postfix = "\n)") { "%L" },
                *componentExprs.toTypedArray()
            )
            .build()

        val objType = TypeSpec.objectBuilder("CampgroundUIDocDefinitions")
            .addProperty(definitionsProp)
            .build()

        val fileSpec = FileSpec.builder(pkg, "CampgroundUIDocDefinitions")
            .addType(objType)
            .addImport("dev.thecampground.ui.annotation", "CampgroundDocComponent")
            .addImport("dev.thecampground.ui.annotation", "CampgroundDocComponentProp")
            .build()

        file.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    private fun buildComponentExpr(comp: CampgroundDocComponent): CodeBlock {
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
            .add("description = %S,\n", comp.description)
            .add("examples = emptyList(),\n")
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

    private fun generateFunctionDef(func: KSFunctionDeclaration): CampgroundDocComponent {
        val funcName = func.simpleName.asString()
        val paramList = mutableListOf<CampgroundDocComponentProp>()
        func.parameters.forEach { param ->
            paramList.add(generateParamDef(param))
        }

        return CampgroundDocComponent(funcName, "Rofl", props = paramList)
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
