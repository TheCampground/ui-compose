package dev.thecampground.ui.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import dev.thecampground.ui.annotation.CampgroundDocComponent
import dev.thecampground.ui.annotation.CampgroundDocType
import dev.thecampground.ui.annotation.CampgroundUIComponent
import dev.thecampground.ui.annotation.CampgroundUIType

class CampgroundUITypeProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    private var fileAlreadyGenerated = false
    private var collectedClasses = mutableListOf<Pair<KSClassDeclaration, CampgroundDocType>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("dev.thecampground.ui.annotation.CampgroundUIType", inDepth = true)

        val invalid = symbols.filter { !it.validate() }.toList()

        symbols
            .filter {  it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(CampgroundUITypeClassVisitor(), Unit) }

        if (!fileAlreadyGenerated) {
            generateCombinedFile()
            fileAlreadyGenerated = true
        }

        return invalid
    }

    private fun generateCombinedFile() {
        if (collectedClasses.isEmpty()) return

        val pkg = "dev.thecampground.ui.internal"

        val file = codeGenerator.createNewFile(
            Dependencies(false, *collectedClasses.map { it.first.containingFile!! }.toTypedArray()),
            pkg,
            "CampgroundUIDocTypeDefinitions",
            "kt"
        )

        val typeExprs = collectedClasses.map { (clazz, type) ->
            val key = clazz.simpleName.asString()
            CodeBlock.of("%S to %L", key, buildTypeExpr(type))
        }

        val typeDefinitions = PropertySpec.builder(
            "typeDefinitions",
            Map::class.asClassName().parameterizedBy(
                STRING,
                ClassName("dev.thecampground.ui.annotation", "CampgroundDocType")
            )
        ).initializer(
            typeExprs.joinToString(prefix = "mapOf(\n", postfix = "\n)") { "%L" },
            *typeExprs.toTypedArray()
        ).build()

        val objType = TypeSpec.objectBuilder("CampgroundUIDocTypeDefinitions")
            .addProperty(typeDefinitions)
            .build()

        val fileSpec = FileSpec.builder(pkg, "CampgroundUIDocTypeDefinitions")
            .addType(objType)
            .addImport("dev.thecampground.ui.annotation", "CampgroundDocType")
            .build()

        file.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    private fun buildTypeExpr(comp: CampgroundDocType): CodeBlock {
        return CodeBlock.builder()
            .add("\nCampgroundDocType(\n")
            .indent()
            .add("name = %S,\n", comp.name)
            .add("description = %S,\n", comp.description)
            .add("properties = listOf(\n")
            .indent()
            .apply {
                comp.properties.forEachIndexed { idx, p ->
                    add("%S", p)
                    if (idx != comp.properties.lastIndex) add (",\n") else add("\n")
                }
            }
            .unindent()
            .add(")\n")
            .unindent()
            .add(")")
            .build()
    }
    private fun genTypeDef(clazz: KSClassDeclaration): CampgroundDocType {
        val className = clazz.simpleName.asString()
        val companionObj = clazz.declarations
            .filterIsInstance<KSClassDeclaration>()
            .firstOrNull() { it.isCompanionObject }

        val properties = when (companionObj) {
            null -> listOf<String>()
            else -> companionObj.getAllProperties().map { it.simpleName.asString() }.toList()
        }

        return CampgroundDocType(
            name = className,
            description = "Not provided",
            properties = properties
        )
    }
    inner class CampgroundUITypeClassVisitor() : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val def = genTypeDef(classDeclaration)
            collectedClasses.add(classDeclaration to def)
        }
    }
}

class CampgroundTypeProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return CampgroundUITypeProcessor(environment.codeGenerator, environment.logger)
    }
}
