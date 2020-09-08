package org.beatonma.commons.app.featured

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.kotlin.extensions.AvatarWithBorder
import org.beatonma.commons.theme.compose.Whitespace
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.CommonsTypography

@Composable
fun FeaturedContent(
    people: List<FeaturedMemberProfile> = listOf(),
    personOnClick: (MemberProfile) -> Unit,
) {
    CommonsTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            LazyColumnFor(people) {
                FeaturedPerson(it.profile, personOnClick)
            }
        }
    }
}

@Composable
private fun FeaturedPerson(
    person: MinimalMember,
    onClick: (MemberProfile) -> Unit,
    modifier: Modifier = Modifier,
) {
    val profile = person.profile

    Row(
        verticalGravity = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = Whitespace.List.Vertical.between)
            .fillMaxWidth()
            .clickable { onClick(profile) }
    ) {
        AvatarWithBorder(
            source = profile.portraitUrl,
            size = 72.dp,
            color = person.party.theme().primary,
            modifier = modifier.padding(Whitespace.Image.around),
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(profile.name, style = CommonsTypography.h6)
            OptionalText(profile.currentPost)
            OptionalText(person.party.name)
        }
    }
}

@Composable
private fun OptionalText(text: String?, modifier: Modifier = Modifier) {
    withNotNull(text) {
        Text(it, modifier = modifier)
    }
}

@Composable
@Preview(name = "FeaturedPersonPreview")
private fun FeaturedPersonPreview() {
    val party = Party(
        4,
        "Conservative"
    )
    val constituency = null

    FeaturedPerson(
        MinimalMember(
            profile = MemberProfile(
                1234,
                "Boris Johnson",
                active = true,
                party = party,
                constituency = constituency,
            ),
            party = party,
            constituency = constituency
        ),
        onClick = {}
    )
}
