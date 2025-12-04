package dev.thecampground.ui.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponent(val uniqueName: String, val description: String = "No description provided")


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponentExample

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIType(val description: String = "Not provided.")

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponentProp(val description: String = "No description provided.")