package org.beatonma.commons.app.ui.components

import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.themed.titleSmall

@Composable
fun EmptyList(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.empty_list),
) {
    Text(message, modifier, style = typography.titleSmall)
}
