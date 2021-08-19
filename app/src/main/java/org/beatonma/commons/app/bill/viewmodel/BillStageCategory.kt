package org.beatonma.commons.app.bill.viewmodel

enum class BillStageCategory {
    /**
     * House of Commons
     */
    Commons,

    /**
     * House of Lords
     */
    Lords,

    /**
     * See https://www.parliament.uk/about/how/laws/passage-bill/commons/coms-consideration-of-amendments/
     */
    ConsiderationOfAmendments,

    /**
     * See https://www.parliament.uk/about/how/laws/passage-bill/commons/coms-royal-assent/
     */
    RoyalAssent,
    ;
}
