package org.beatonma.commons.app.ui.compose.components.image

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag

@Composable
fun ClickableIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tag: String? = null,
    onClick: () -> Unit,
) {
    val tagModifier = if (tag != null) Modifier.testTag(tag) else Modifier
    IconButton(
        onClick = onClick,
        modifier = tagModifier
    ) {
        Icon(
            icon,
            contentDescription,
            modifier,
        )
    }
}
