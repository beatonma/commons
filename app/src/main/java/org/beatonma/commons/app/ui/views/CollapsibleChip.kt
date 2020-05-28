package org.beatonma.commons.app.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.withStyledAttributes
import org.beatonma.commons.R
import org.beatonma.commons.databinding.ViewCollapsibleChipBinding
import org.beatonma.commons.kotlin.extensions.dial
import org.beatonma.commons.kotlin.extensions.sendMail
import org.beatonma.commons.kotlin.extensions.stringCompat
import org.beatonma.commons.kotlin.extensions.text

private const val TAG = "CollapsibleChip"

class CollapsibleChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    val binding: ViewCollapsibleChipBinding = ViewCollapsibleChipBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.view_collapsible_chip, this, false)
    )

    init {
        addView(binding.root)
        binding.motionLayout.setOnClickListener(getDefaultToggleClickListener())
        binding.cancelIcon.setOnClickListener { collapse() }

        context.withStyledAttributes(attrs, R.styleable.CollapsibleChip, defStyleAttr = defStyleAttr, defStyleRes = defStyleRes) {
            binding.chipText.text = text(context, R.styleable.CollapsibleChip_chipText, "app:chipText")
            binding.chipIcon.setImageResource(getResourceId(R.styleable.CollapsibleChip_chipIcon, 0))
        }
    }

    private fun collapse() = binding.motionLayout.transitionToStart()
    private fun expand() = binding.motionLayout.transitionToEnd()

    fun setText(text: CharSequence?) {
        binding.chipText.text = text
    }

    fun setText(@StringRes resId: Int) {
        binding.chipText.setText(resId)
    }

    fun setBackgroundTint(tint: Int) {
        binding.motionLayout.backgroundTintList = ColorStateList.valueOf(tint)
    }

    fun bind(action: ChipData) {
        binding.chipText.text = action.text
        binding.chipIcon.setImageResource(action.icon)
        if (action.iconTint != null) {
            binding.chipIcon.imageTintList = ColorStateList.valueOf(action.iconTint)
        }
        if (action.backgroundTint != null) {
            setBackgroundTint(action.backgroundTint)

        }
        setOnClickListener(action.action)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val wrappedListener = OnClickListener { view ->
            with(binding.motionLayout) {
                when (currentState) {
                    startState -> expand()
                    endState -> l?.onClick(view)
                }
            }
        }
        binding.motionLayout.setOnClickListener(wrappedListener)
        binding.chipIcon.setOnClickListener(wrappedListener)
    }

    private fun getDefaultToggleClickListener() = OnClickListener {
        with(binding.motionLayout) {
            when (currentState) {
                startState -> expand()
                endState -> collapse()
            }
        }
    }
}


data class ChipData(
    val text: String?,
    @DrawableRes val icon: Int,
    val iconTint: Int? = null,
    val backgroundTint: Int? = null,
    val action: ((View) -> Unit)?
) {
    companion object {
        fun forPhoneNumber(context: Context, phoneNumber: String) = ChipData(
            text = context.stringCompat(R.string.action_dial_phonenumber, phoneNumber),
            icon = R.drawable.ic_phone_call,
            action = { view -> view.context.dial(phoneNumber) }
        )

        fun forEmail(context: Context, address: String) = ChipData(
            text = context.stringCompat(R.string.action_write_email, address),
            icon = R.drawable.ic_email,
            action = { view -> view.context.sendMail(address) }
        )
    }
}
