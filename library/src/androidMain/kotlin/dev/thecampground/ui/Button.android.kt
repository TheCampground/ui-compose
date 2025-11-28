package dev.thecampground.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
internal actual fun ButtonFeedbackTest(feedback: InputTouchFeedback?) {
    val haptic = LocalHapticFeedback.current

    haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
}