package org.beatonma.commons.app.featured

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.tooling.preview.Preview
import org.beatonma.commons.app.ui.compose.components.AmbientPartyTheme
import org.beatonma.commons.app.ui.compose.components.image.DEV_AVATAR
import org.beatonma.commons.app.ui.compose.components.partyWithTheme
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party

@Composable
@Preview("MemberWithPortrait")
private fun MemberPreview() {
    val partyWithTheme = partyWithTheme(Party(15, "Labour"))
    Providers(
        AmbientPartyTheme provides partyWithTheme
    ) {
        Member(
            member = MinimalMember(
                MemberProfile(
                    0,
                    name = "Mr Example",
                    portraitUrl = DEV_AVATAR,
                    party = AmbientPartyTheme.current.party,
                    constituency = null,
                    currentPost = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service"
                ),
                AmbientPartyTheme.current.party,
                constituency = null
            ),
            onClick = {},
            reason = null,
        )
    }
}

@Composable
@Preview("MemberWithoutPortrait")
private fun MemberNoPreviewPreview() {
    val partyWithTheme = partyWithTheme(Party(15, "Labour"))
    Providers(
        AmbientPartyTheme provides partyWithTheme
    ) {
        ProvideImageConfigs {
            Member(
                member = MinimalMember(
                    MemberProfile(
                        0,
                        name = "Mr Example",
                        party = AmbientPartyTheme.current.party,
                        constituency = null,
                        currentPost = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service"
//                        currentPost = "Shadow Minister of Silly Walks",
                    ),
                    AmbientPartyTheme.current.party,
                    constituency = null
                ),
                onClick = {},
                reason = null,
            )
        }
    }
}
