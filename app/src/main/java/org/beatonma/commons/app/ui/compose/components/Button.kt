package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonConstants
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.theme.compose.theme.CommonsButtons

@OptIn(ExperimentalMaterialApi::class)
@Composable
inline fun CommonsOutlinedButton(
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionState: InteractionState = remember { InteractionState() },
    elevation: ButtonElevation? = null,
    shape: Shape = shapes.small,
    border: BorderStroke? = ButtonConstants.defaultOutlinedBorder,
    colors: ButtonColors = CommonsButtons.outlineButtonColors(),
    contentPadding: PaddingValues = ButtonConstants.DefaultContentPadding,
    noinline content: @Composable RowScope.() -> Unit,
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionState = interactionState,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    content = content
)
