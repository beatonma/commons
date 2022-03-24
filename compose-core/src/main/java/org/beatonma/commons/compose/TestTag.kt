package org.beatonma.commons.compose

object TestTag {
    const val ModalScrim = "modal_scrim"

    const val Fab = "fab_bottomsheet_surface__fab"
    const val FabBottomSheet = "fab_bottomsheet_surface"

    const val SearchField = "search_field"

    const val Chip = "chip"

    const val FeedbackSurface = "feedback_surface"
    const val FeedbackMessage = "feedback_message"
    const val FeedbackText = "feedback_text"
    const val FeedbackNoText = "feedback_text__null"

    const val ValidatedText = "validated_text"
    const val ValidationCounter = "counter_text"

    const val LazyList = "lazy_list"
    const val CollapsingHeader = "collapsing_header"

    const val ShowMore = "more_content"

    // Text
    const val DateTime = "datetime"
    const val Date = "date"
    const val Time = "time"

    // Actions
    const val Confirm = "action_confirm"
    const val Cancel = "action_cancel"
    const val Submit = "action_submit"
    const val Clear = "action_clear"

    fun member(name: String) = "member_$name"
}
