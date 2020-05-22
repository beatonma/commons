package org.beatonma.commons.test

import java.time.LocalDate

fun String.asDate(): LocalDate = LocalDate.parse(this)
