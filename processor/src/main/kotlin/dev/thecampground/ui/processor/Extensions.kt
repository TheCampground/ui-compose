package dev.thecampground.ui.processor

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

fun KSDeclaration.getAnnotationOrNull(packageName: String): KSAnnotation? {
    return this.annotations.firstOrNull { ann ->
        ann.annotationType
            .resolve()
            .declaration
            .qualifiedName
            ?.asString() == packageName
    }
}

fun KSValueParameter.getAnnotationOrNull(packageName: String): KSAnnotation? {
    return this.annotations.firstOrNull { ann ->
        ann.annotationType
            .resolve()
            .declaration
            .qualifiedName
            ?.asString() == packageName
    }
}

fun KSAnnotation.getArgumentValueAsString(argumentName: String): String? {
    return this.arguments.firstOrNull { it.name?.asString() == argumentName }?.value as? String
}