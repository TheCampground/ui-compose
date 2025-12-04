package dev.thecampground.ui.processor

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
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File

class CampgroundUIComponentExampleProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    private var fileAlreadyGenerated = false
    private var collectedExamples = mutableMapOf<String, Pair<KSFunctionDeclaration, String>>()
    private var rawFunctionStrings: List<String>? = null

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("dev.thecampground.ui.annotation.CampgroundUIComponentExample", inDepth = true)
        val invalid = symbols.filter { !it.validate() }.toList()

        symbols
            .filter {  it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(CampgroundUIComponentExampleFunctionVisitor(), Unit) }

        if (!fileAlreadyGenerated) {
            generateExamplesFile()
            fileAlreadyGenerated = true
        }
        return invalid
    }
    private fun generateExamplesFile() {
        if (collectedExamples.isEmpty()) return

        val pkg = "dev.thecampground.ui.internal"
        val file = codeGenerator.createNewFile(
            Dependencies(
                aggregating = false,
                sources = collectedExamples.values.map { it.first.containingFile!! }.toTypedArray()),
            pkg,
            "CampgroundUIExamples",
            "kt"
        )

        val mapExprs = collectedExamples.map { (name, text) ->
            CodeBlock.of("\n%S to %S\n", name, text.second)
        }

        val typeDefinitions = PropertySpec.builder(
            "exampleDefinitions",
            Map::class.asClassName().parameterizedBy(
                STRING,
                STRING
            )
        ).initializer(
            mapExprs.joinToString(prefix = "mapOf(\n", postfix = "\n)") { "%L" },
            *mapExprs.toTypedArray()
        ).build()

        val objType = TypeSpec.objectBuilder("CampgroundUIExamples")
            .addProperty(typeDefinitions)
            .build()

        val fileSpec = FileSpec.builder(pkg, "CampgroundUIExamples")
            .addType(objType)
            .build()

        file.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }
    private fun collectFuncString(path: String, start: Int): String {
        // Lol
        val realStart = start-1

        val str = StringBuilder()

        if (rawFunctionStrings == null) {
            rawFunctionStrings = File(path).readLines()
        }

        var openingStatements = 0
        // Worlds simplest function grabber.
        for (i in realStart..<(rawFunctionStrings?.size ?: 0)) {
            val line = rawFunctionStrings?.get(i) ?: "0"
            val newLine = if (i < (rawFunctionStrings?.size ?: 0)) "\n" else ""
            str.append("${line}$newLine")

            openingStatements += line.count { chr -> chr == '{' }
            openingStatements -= line.count { chr -> chr == '}' }
            if (openingStatements == 0) break
        }

        val sep = str.toString().split("\n").toMutableList()

        sep.removeFirst()
        sep.removeLast()
        return sep.joinToString("\n")
    }
    private fun getFuncText(func: KSFunctionDeclaration): String {
        val fileLocation = (func.location as FileLocation)
        val filePath = fileLocation.filePath
        val startLineNumber = fileLocation.lineNumber

        return collectFuncString(filePath, startLineNumber)
    }
    inner class CampgroundUIComponentExampleFunctionVisitor() : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val text = getFuncText(function)

            collectedExamples[function.simpleName.asString()] = function to text
        }
    }
}

class CampgroundUIComponentExampleProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return CampgroundUIComponentExampleProcessor(environment.codeGenerator, environment.logger)
    }
}
