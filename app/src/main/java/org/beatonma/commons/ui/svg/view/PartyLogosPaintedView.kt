package org.beatonma.commons.ui.svg.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes

import org.beatonma.commons.R
import org.beatonma.commons.political.organisation.*

import org.beatonma.lib.graphic.paintedview.PaintedSvgView
import org.beatonma.lib.util.kotlin.extensions.hasFlag
import org.beatonma.lib.util.kotlin.extensions.log


/**
 * Created by Michael on 27/09/2017.
 * A PaintedSvgView that automatically cycles through all (active) party logos
 */


private const val LOGO_DURATION: Long = 2400

class PartyLogosPaintedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PaintedSvgView(context, attrs, defStyleAttr) {

    private val parties: List<String>
    private val partyCount: Int
    private var partyIndex: Int = 0

    private val nextLogoRunnable = Runnable(::nextLogo)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.PartyLogosPaintedView, defStyleAttr, 0).apply {
            val partyFlags = getInt(R.styleable.PartyLogosPaintedView_party, 0)
            parties = resolveParties(partyFlags).shuffled()
            recycle()
        }
        partyCount = parties.size
        nextLogo()
    }

    override fun onPaintFinished() {
        super.onPaintFinished()
        if (partyCount > 1) postDelayed(nextLogoRunnable, LOGO_DURATION)
    }

    private fun nextLogo() {
        partyIndex = (partyIndex + 1) % partyCount
        setSvgWithAnimation(getPartySvg(parties[partyIndex]))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(nextLogoRunnable)
    }

    companion object PartyFlagResolver {
        private const val FLAG_CONSERVATIVE = 1
        private const val FLAG_DUP = 2
        private const val FLAG_GREEN = 4
        private const val FLAG_INDEPENDENT = 8
        private const val FLAG_LABOUR = 16
        private const val FLAG_LABOUR_COOP = 32
        private const val FLAG_LIB_DEM = 64
        private const val FLAG_PLAID_CYMRU = 128
        private const val FLAG_SDLP = 256
        private const val FLAG_SINN_FEIN = 512
        private const val FLAG_SNP = 1024
        private const val FLAG_UKIP = 2048
        private const val FLAG_UUP = 4096

        fun resolveParties(flags: Int): List<String> {
            log("flags=$flags")
            if (flags == 0) return partyNames

            val list = mutableListOf<String>()
            if (flags.hasFlag(FLAG_CONSERVATIVE)) list.add(CONSERVATIVE)
            if (flags.hasFlag(FLAG_DUP)) list.add(DUP)
            if (flags.hasFlag(FLAG_GREEN)) list.add(GREEN)
            if (flags.hasFlag(FLAG_INDEPENDENT)) list.add(INDEPENDENT)
            if (flags.hasFlag(FLAG_LABOUR)) list.add(LABOUR)
            if (flags.hasFlag(FLAG_LABOUR_COOP)) list.add(LABOUR_COOP)
            if (flags.hasFlag(FLAG_LIB_DEM)) list.add(LIB_DEM)
            if (flags.hasFlag(FLAG_PLAID_CYMRU)) list.add(PLAID_CYMRU)
            if (flags.hasFlag(FLAG_SDLP)) list.add(SDLP)
            if (flags.hasFlag(FLAG_SINN_FEIN)) list.add(SINN_FEIN)
            if (flags.hasFlag(FLAG_SNP)) list.add(SNP)
            if (flags.hasFlag(FLAG_UKIP)) list.add(UKIP)
            if (flags.hasFlag(FLAG_UUP)) list.add(UUP)

            return list
        }
    }
}
