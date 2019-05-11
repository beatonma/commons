package org.beatonma.commons.political.organisation

import org.beatonma.commons.ui.svg.*
import org.beatonma.lib.testing.kotlin.extensions.assertions.assertInstanceOf
import org.junit.Test

class PartyTest {

    @Test
    fun getPartySvg() {
        getPartySvg(CONSERVATIVE).assertInstanceOf(ConservativeSvg::class)
        getPartySvg(DUP).assertInstanceOf(DupSvg::class)
        getPartySvg(GREEN).assertInstanceOf(GreenSvg::class)
        getPartySvg(INDEPENDENT).assertInstanceOf(DefaultSvg::class)
        getPartySvg(LABOUR).assertInstanceOf(LabourSvg::class)
        getPartySvg(LABOUR_COOP).assertInstanceOf(LabourCoopSvg::class)
        getPartySvg(LIB_DEM).assertInstanceOf(LibDemSvg::class)
        getPartySvg(PLAID_CYMRU).assertInstanceOf(PlaidCymruSvg::class)
        getPartySvg(SDLP).assertInstanceOf(SdlpSvg::class)
        getPartySvg(SINN_FEIN).assertInstanceOf(SinnFeinSvg::class)
        getPartySvg(SNP).assertInstanceOf(SnpSvg::class)
        getPartySvg(UKIP).assertInstanceOf(UkipSvg::class)
        getPartySvg(UUP).assertInstanceOf(UupSvg::class)
        getPartySvg(SPEAKER).assertInstanceOf(DefaultSvg::class)
    }
}
