package org.beatonma.commons

import org.beatonma.commons.core.ParliamentID
import java.time.LocalDate
import kotlin.random.Random

val anyID: ParliamentID get() = (1..10000).random()

val anyDate: LocalDate get() = LocalDate.of(
    (1950..2000).random(),
    (1..12).random(),
    (1..28).random()
)

val anyBool: Boolean get() = Random.nextBoolean()
