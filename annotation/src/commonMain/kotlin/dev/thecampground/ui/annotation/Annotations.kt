package dev.thecampground.ui.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundComponent(val uniqueName: String, val description: String = "No description provided")


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundExample

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundType(val description: String = "Not provided.")

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundProp(val description: String = "No description provided.")