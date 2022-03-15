package org.beatonma.commons.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.HdrWeak
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.ui.graphics.vector.ImageVector
import org.beatonma.commons.compose.UiIcon
import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.core.LordsVoteType
import org.beatonma.commons.core.VoteType

object AppIcon {
    // Generic UI
    val ArrowRight get() = Icons.Default.KeyboardArrowRight
    val Back get() = Icons.Default.ArrowBack
    val Close get() = UiIcon.Close
    val CloseCircle get() = Icons.Default.Cancel
    val Check get() = Icons.Default.Done
    val DropDown get() = UiIcon.DropDown
    val Edit get() = Icons.Default.Edit
    val Email get() = Icons.Default.Email
    val Facebook get() = Icons.Default.Facebook
    val Link get() = Icons.Default.Link
    val Loading get() = Icons.Default.HdrWeak
    val Pdf get() = Icons.Default.PictureAsPdf
    val Phone get() = Icons.Default.Phone
    val Search get() = Icons.Default.Search
    val Undo get() = Icons.Default.Undo

    // Social
    val Comment get() = Icons.Default.Comment
    val ThumbUp get() = Icons.Default.ThumbUp
    val ThumbDown get() = Icons.Default.ThumbDown
    val Division get() = Icons.Default.HowToVote
    val Bill get() = Icons.Default.Assignment

    // Commons data
    val Featured get() = Icons.Default.Star
    val Trending get() = Icons.Default.Whatshot
    val VoteAye get() = Icons.Default.AddCircle
    val VoteNo get() = Icons.Default.RemoveCircle
}


val DivisionVoteType.icon: ImageVector
    get() = when (this) {
        is LordsVoteType -> icon
        is VoteType -> icon
    }

val LordsVoteType.icon: ImageVector
    get() = when (this) {
        LordsVoteType.content -> AppIcon.VoteAye
        LordsVoteType.not_content -> AppIcon.VoteNo
    }

val VoteType.icon: ImageVector
    get() = when (this) {
        VoteType.AyeVote -> AppIcon.VoteAye
        VoteType.NoVote -> AppIcon.VoteNo
        else -> AppIcon.CloseCircle
    }
