package org.beatonma.commons.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import org.beatonma.commons.ClickAction
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.string

private const val REVERT_STATE_TIMEOUT = 1800L

private enum class State {
    UNSURE,
    CERTAIN,
    ;
}

/**
 * A [MaterialButton] that needs to be pressed twice before executing OnClickListener.
 * Alternatively, respond to each click with [setClickListeners].
 */
class DoubleCheckButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): MaterialButton(context, attrs, defStyleAttr) {
    private var state: State = State.UNSURE
        set(value) {
            field = value
            org.beatonma.commons.core.extensions.withNotNull(parent as? ViewGroup) {
                TransitionManager.beginDelayedTransition(it)
            }

            when (value) {
                State.UNSURE -> text = unsureButtonText
                State.CERTAIN -> {
                    text = certainButtonText
                    scheduleRevertState()
                }
            }
        }

    private var unsureButtonText: String? = null
    private var certainButtonText: String? = null

    private var unsureAction: ClickAction? = null
    private var certainAction: ClickAction? = null

    private var coroutineScope: CoroutineScope? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.DoubleCheckButton, defStyleAttr, defStyleRes) {
            unsureButtonText = string(context, R.styleable.DoubleCheckButton_doublecheck_unsureText)
            certainButtonText = string(context, R.styleable.DoubleCheckButton_doublecheck_certainText)
        }
        state = State.UNSURE
    }

    override fun setOnClickListener(l: OnClickListener?) {
        org.beatonma.commons.core.extensions.withNotNull(l) {
            unsureAction = {}
            certainAction = it::onClick
        }
    }

    fun setClickListeners(whenUnsure: ClickAction = {}, whenCertain: ClickAction) {
        unsureAction = whenUnsure
        certainAction = whenCertain
        super.setOnClickListener { view ->
            getActionForState(state)?.invoke(view)

            if (state == State.UNSURE) {
                state = State.CERTAIN
            }
        }
    }

    private fun getActionForState(state: State) = when (state) {
        State.UNSURE -> unsureAction
        State.CERTAIN -> certainAction
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    }

    override fun onDetachedFromWindow() {
        coroutineScope?.cancel()
        super.onDetachedFromWindow()
    }

    private fun scheduleRevertState() {
        coroutineScope?.launch {
            delay(REVERT_STATE_TIMEOUT)
            state = State.UNSURE
        }
    }
}
