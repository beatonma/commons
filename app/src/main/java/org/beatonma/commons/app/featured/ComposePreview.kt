package org.beatonma.commons.app.featured

import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.compose.components.DEV_AVATAR
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party

@Composable
@Preview("MemberWithPortrait")
private fun MemberPreview() {
    provideParty(party = Party(15, "Labour")) {
        provideImageConfigs {
            Member(
                member = MinimalMember(
                    MemberProfile(
                        0,
                        name = "Mr Example",
                        portraitUrl = DEV_AVATAR,
                        party = PartyAmbient.current.first,
                        constituency = null,
                        currentPost = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service"
                    ),
                    PartyAmbient.current.first,
                    constituency = null
                ),
                onClick = {},
                reason = null,
            )
        }
    }
}

@Composable
@Preview("MemberWithoutPortrait")
private fun MemberNoPreviewPreview() {
    provideParty(party = Party(15, "Labour")) {
        provideImageConfigs {
            Member(
                member = MinimalMember(
                    MemberProfile(
                        0,
                        name = "Mr Example",
                        party = PartyAmbient.current.first,
                        constituency = null,
                        currentPost = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service"
//                        currentPost = "Shadow Minister of Silly Walks",
                    ),
                    PartyAmbient.current.first,
                    constituency = null
                ),
                onClick = {},
                reason = null,
            )
        }
    }
}
