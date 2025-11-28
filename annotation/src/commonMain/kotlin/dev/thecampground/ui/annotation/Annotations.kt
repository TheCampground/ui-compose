package dev.thecampground.ui.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponent()

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class CampgroundUIComponentProp(val description: String = "No description provided.")