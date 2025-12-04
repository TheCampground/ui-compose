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
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSTypeAlias
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
import dev.thecampground.ui.annotation.model.CampgroundType
import dev.thecampground.ui.annotation.model.CampgroundTypeType
import kotlin.system.exitProcess

class CampgroundUITypeProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    private var fileAlreadyGenerated = false
    private var collectedItems = mutableListOf<Pair<KSDeclaration, CampgroundType>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("dev.thecampground.ui.annotation.CampgroundType", inDepth = true)

        val invalid = symbols.filter { !it.validate() }.toList()

        symbols
            .filter {  it is KSClassDeclaration || it is KSTypeAlias && it.validate() }
            .forEach { it.accept(CampgroundUITypeVisitor(), Unit) }

        if (!fileAlreadyGenerated) {
            generateCombinedFile()
            fileAlreadyGenerated = true
        }

        return invalid
    }

    private fun generateCombinedFile() {
        if (collectedItems.isEmpty()) return


        val file = codeGenerator.createNewFile(
            Dependencies(false, *collectedItems.map { it.first.containingFile!! }.toTypedArray()),
            PKG,
            "CampgroundTypes",
            "kt"
        )

        val typeExpressions = collectedItems.map { (clazz, type) ->
            val key = clazz.simpleName.asString()
            CodeBlock.of("%S to %L", key, buildTypeExpr(type))
        }

        val typeDefinitions = PropertySpec.builder(
            "types",
            Map::class.asClassName().parameterizedBy(
                STRING,
                ClassName("dev.thecampground.ui.annotation.model", "CampgroundType")
            )
        ).initializer(
            typeExpressions.joinToString(prefix = "mapOf(\n", postfix = "\n)") { "%L" },
            *typeExpressions.toTypedArray()
        ).build()

        val objType = TypeSpec.objectBuilder("CampgroundTypes")
            .addProperty(typeDefinitions)
            .build()

        val fileSpec = FileSpec.builder(PKG, "CampgroundTypes")
            .addType(objType)
            .addImport("dev.thecampground.ui.annotation.model", "CampgroundType")
            .addImport("dev.thecampground.ui.annotation.model", "CampgroundTypeType")
            .build()

        file.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    private fun buildTypeExpr(comp: CampgroundType): CodeBlock {
        return CodeBlock.builder()
            .add("\nCampgroundType(\n")
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
            .add("),\n")
            .unindent()
            .add("type = CampgroundTypeType.${comp.type.name},\n")
            .add(")")
            .build()
    }
    private fun generateTypeForClass(clazz: KSClassDeclaration): CampgroundType {
        val className = clazz.simpleName.asString()
        val companionObj = clazz.declarations
            .filterIsInstance<KSClassDeclaration>()
            .firstOrNull() { it.isCompanionObject }
        val description =  clazz
            .getAnnotationOrNull("dev.thecampground.ui.annotation.CampgroundType")
            ?.getArgumentValueAsString("description") ?: "Not provided"

        val properties = when (companionObj) {
            null -> listOf<String>()
            else -> companionObj.getAllProperties().map { it.simpleName.asString() }.toList()
        }

        return CampgroundType(
            name = className,
            description = description,
            properties = properties,
            type = CampgroundTypeType.CLASS
        )
    }
    
    private fun generateTypeForAlias(alias: KSTypeAlias): CampgroundType {
        val aliasName = alias.simpleName.asString()
        val resolvedType = alias.type.resolve()
        val aliasIsFunc = resolvedType.isFunctionType
        val description = alias
            .getAnnotationOrNull("dev.thecampground.ui.annotation.CampgroundType")
            ?.getArgumentValueAsString("description") ?: "Not provided"

        if (!aliasIsFunc) {
            logger.warn("Typealiases that are not functions are not supported.")
            exitProcess(1)
        }

        // TODO: Null check
        val functionParameters = resolvedType.arguments.dropLast(1).map { it.type!!.resolve().declaration.simpleName.asString() }

        return CampgroundType(
            name = aliasName,
            description = description,
            properties = functionParameters,
            type = CampgroundTypeType.FUNCTION,
        )
    }
    inner class CampgroundUITypeVisitor() : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val def = generateTypeForClass(classDeclaration)
            collectedItems.add(classDeclaration to def)
        }

        override fun visitTypeAlias(typeAlias: KSTypeAlias, data: Unit) {
            val def = generateTypeForAlias(typeAlias)

            collectedItems.add(typeAlias to def)
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
