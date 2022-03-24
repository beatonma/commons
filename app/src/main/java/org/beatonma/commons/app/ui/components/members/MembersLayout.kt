package org.beatonma.commons.app.ui.components.members

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.ui.components.party.ProvidePartyImageConfig
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.themed.animation
import kotlin.random.Random

private sealed interface MemberContainer

/**
 * Represents a profile with a portrait image.
 */
@JvmInline
private value class LargeMember(val profile: MemberProfile) : MemberContainer

/**
 * Represents a group of profiles which do not have portrait images available.
 */
@JvmInline
private value class SmallMemberGroup(val profiles: List<MemberProfile>) : MemberContainer


@Composable
fun MembersLayout(
    profiles: List<MemberProfile>,
    modifier: Modifier = Modifier,
    showImages: Boolean = true,
    shuffle: Boolean = true,
    decoration: (@Composable (MemberProfile) -> Unit)? = null,
    onClick: (MemberProfile) -> Unit,
) {
    if (showImages) {
        MembersLayoutWithImages(profiles, modifier, shuffle, decoration, onClick)
    } else {
        CompactMembersLayout(profiles, modifier, decoration, onClick)
    }
}

/**
 * Render a group of [MemberProfile]s without any portrait images.
 */
@Composable
private fun CompactMembersLayout(
    profiles: List<MemberProfile>,
    modifier: Modifier,
    decoration: (@Composable (MemberProfile) -> Unit)?,
    onClick: (MemberProfile) -> Unit,
) {
    BaseMembersLayout(modifier) {
        items(profiles) { profile ->
            Profile(
                profile,
                showImages = false,
                decoration,
                onClick,
            )
        }
    }
}

/**
 * Render a group of [MemberProfile]s with portrait images where available.
 *
 * Members with portraits are displayed as items in a row.
 * Members without portraits are grouped together and displayed as columns, interspersed among
 * those that do have portraits.
 */
@Composable
private fun MembersLayoutWithImages(
    profiles: List<MemberProfile>,
    modifier: Modifier,
    shuffle: Boolean,
    decoration: (@Composable (MemberProfile) -> Unit)?,
    onClick: (MemberProfile) -> Unit,
) {
    fun <T> List<T>.maybeShuffled() = if (shuffle) this.shuffled() else this

    val groupSize = 3
    val orderedProfiles: List<MemberContainer> by remember(profiles) {
        val (small, large) =
            profiles
                .maybeShuffled()
                .partition { it.portraitUrl == null }

        val smallContainers = small.chunked(groupSize).map(::SmallMemberGroup)
        val largeContainers = large.map(::LargeMember)

        // Recombine the large and small containers
        val smallContainerPositions =
            smallContainers
                .map { Random.nextInt(0, largeContainers.size) }
                .sorted()

        val containers: MutableList<MemberContainer> = largeContainers.toMutableList()
        smallContainerPositions.forEachIndexed { index, position ->
            containers.add(position, smallContainers[index])
        }

        mutableStateOf(
            containers.toList()
        )
    }

    BaseMembersLayout(modifier) {
        orderedProfiles.fastForEach { container ->
            item(contentType = container::class) {
                MemberContainer(container, decoration, onClick)
            }
        }
    }
}

@Composable
private fun BaseMembersLayout(
    modifier: Modifier,
    content: LazyListScope.() -> Unit,
) {
    ProvidePartyImageConfig {
        LazyRow(modifier.animateContentSize(animation.spec())) {
            content()
        }
    }
}

@Composable
private fun MemberContainer(
    container: MemberContainer,
    decoration: @Composable ((MemberProfile) -> Unit)?,
    onClick: (MemberProfile) -> Unit,
) {
    when (container) {
        is LargeMember -> Profile(container.profile, true, decoration, onClick)
        is SmallMemberGroup -> MemberGroup(container, decoration, onClick)
    }
}

@Composable
private fun MemberGroup(
    group: SmallMemberGroup,
    decoration: @Composable ((MemberProfile) -> Unit)?,
    onClick: (MemberProfile) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        group.profiles.fastForEach { profile ->
            Profile(
                profile,
                true,
                decoration,
                onClick,
            )
        }
    }
}

@Composable
private fun Profile(
    profile: MemberProfile,
    showImages: Boolean,
    decoration: @Composable ((MemberProfile) -> Unit)?,
    onClick: (MemberProfile) -> Unit,
) {
    val resolvedOnClick = { onClick(profile) }
    val resolvedDecoration: (@Composable () -> Unit)? =
        decoration?.let { func ->
            { func(profile) }
        }

    MemberLayout(
        profile,
        onClick = resolvedOnClick,
        showImages = showImages,
        decoration = resolvedDecoration,
    )
}
