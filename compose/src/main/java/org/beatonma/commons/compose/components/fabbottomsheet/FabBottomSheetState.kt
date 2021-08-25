package org.beatonma.commons.compose.components.fabbottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.compose.animation.TwoState
import org.beatonma.commons.theme.compose.theme.animation

enum class FabBottomSheetState : TwoState<FabBottomSheetState> {
    Fab,
    BottomSheet,
    ;

    override fun toggle() = when (this) {
        Fab -> BottomSheet
        BottomSheet -> Fab
    }
}

@Composable
fun rememberFabBottomSheetState() = remember { mutableStateOf(FabBottomSheetState.Fab) }

@Composable
fun FabBottomSheetState.animateExpansionAsState(): State<Float> = animation.animateFloatAsState(
    when (this) {
        FabBottomSheetState.Fab -> 0F
        FabBottomSheetState.BottomSheet -> 1F
    }
)
