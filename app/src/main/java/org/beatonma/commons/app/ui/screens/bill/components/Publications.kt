package org.beatonma.commons.app.ui.screens.bill.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.util.openUrl
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.BottomCardText
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.Dialog
import org.beatonma.commons.compose.components.Tag
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.layout.ListOrEmpty
import org.beatonma.commons.compose.padding.endOfContent
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.systemui.navigationBarsPadding
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.compose.util.pluralResource
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationLink
import org.beatonma.commons.theme.AppIcon
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.themed.padding
import org.beatonma.commons.themed.titleMedium
import org.beatonma.commons.themed.titleSmall

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Publications(
    publications: List<BillPublication>,
    modifier: Modifier,
) {
    var focussed: BillPublication? by remember { mutableStateOf(null) }
    var dialogState by rememberExpandCollapseState()
    val collapsedItemCount = 3

    CollapsedColumn(
        publications,
        CollapsedColumn.simpleHeader(
            pluralResource(resId = R.plurals.bill_publications, quantity = publications.size)
        ),
        dialogState,
        { dialogState = it },
        collapsedItemCount = collapsedItemCount,
        modifier = modifier,
    ) { displayItems ->
        displayItems.take(collapsedItemCount).forEach { publication ->
            ListItem(
                text = {
                    Text(publication.data.title)
                },
                secondaryText = {
                    Text(publication.data.type dot publication.data.date.formatted())
                },
                modifier = Modifier.clickable { focussed = publication }
            )
        }
    }

    FocussedPublications(
        dialogState,
        { dialogState = it },
        publications,
        focussed,
        setFocus = { focussed = it },
    )

    LaunchedEffect(focussed) {
        if (focussed != null) dialogState = ExpandCollapseState.Expanded
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FocussedPublications(
    state: ExpandCollapseState,
    onStateChange: (ExpandCollapseState) -> Unit,
    publications: List<BillPublication>,
    focussed: BillPublication?,
    setFocus: (BillPublication?) -> Unit,
) {
    BackHandler(enabled = focussed != null) {
        setFocus(null)
    }

    Dialog(
        state,
        onStateChange,
        alignment = Alignment.BottomCenter
    ) { progress ->
        BottomCardText(
            Modifier.navigationBarsPadding(left = false, right = false)
        ) {
            if (focussed == null) {
                Column {
                    Text(
                        pluralResource(
                            R.plurals.bill_publications,
                            quantity = publications.size
                        ),
                        style = typography.titleMedium
                    )

                    LazyColumn {
                        items(publications) { publication ->
                            PublicationListItem(
                                publication,
                                Modifier.clickable { setFocus(publication) }
                            )
                        }
                        endOfContent()
                    }
                }
            } else {
                FocussedPublication(focussed) { setFocus(null) }
            }
        }
    }
}

@Composable
private fun FocussedPublication(publication: BillPublication, defocus: () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(AppIcon.Back, null,
                Modifier
                    .clickable(onClick = defocus)
                    .padding(padding.IconLarge.copy(start = 0.dp))
            )

            Text("Links", style = typography.titleMedium)
        }

        ListOrEmpty(
            publication.links,
            empty = { ResourceText(R.string.bill_publication_links_empty) }
        ) { link ->
            PublicationLink(link)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PublicationListItem(publication: BillPublication, modifier: Modifier = Modifier) {
    ListItem(
        text = { Text(publication.data.title) },
        secondaryText = {
            Text(publication.data.type dot publication.data.date.formatted())
        },
        modifier = modifier
    )
}

@Composable
private fun PublicationLink(link: BillPublicationLink) {
    val label = when (link.contentType) {
        "application/pdf" -> "PDF"
        else -> null
    }

    Column(
        Modifier
            .clickable(onClick = openUrl(link.url))
            .fillMaxWidth()
            .padding(padding.VerticalListItem)
    ) {
        Text(link.title, style = typography.titleSmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
            withNotNull(label) {
                Tag(
                    it,
                    color = colors.error,
                    contentColor = colors.onError
                )
            }
            Text(link.url)
        }
    }
}
