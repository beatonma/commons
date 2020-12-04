package org.beatonma.commons.theme.compose

private const val LAYER_STEP = 16F

/**
 * Standard values for zIndex modifier to ensure consistency when rendering views that may overlap.
 */
object Layer {
    const val Bottom = 0F
    const val Low = LAYER_STEP
    const val Middle = Low + LAYER_STEP
    const val High = Middle + LAYER_STEP
    const val Top = High + LAYER_STEP

    // Dialog/bottomsheet
    const val ModalScrim = Top + LAYER_STEP
    const val ModalSurface = ModalScrim + LAYER_STEP
    const val ModalContent = ModalSurface + LAYER_STEP

    const val AlwaysOnTopSurface = ModalContent + LAYER_STEP
    const val AlwaysOnTopContent = AlwaysOnTopSurface + LAYER_STEP
}
