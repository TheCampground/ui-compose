package dev.thecampground.ui.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponent(val description: String = "No description provided")

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponentProp(val description: String = "No description provided.")