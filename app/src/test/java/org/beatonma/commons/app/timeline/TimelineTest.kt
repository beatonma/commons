package org.beatonma.commons.app.timeline

import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test
import java.time.LocalDate

private class Sample(override val start: LocalDate?, override val end: LocalDate?): Periodic

class TimelineTest {
    private val epochStart = LocalDate.of(2015, 1, 5)
    private val now = LocalDate.of(2021, 3, 25)

    @Test
    fun item_isCorrect() {
        val item = Item(
            Sample(
                LocalDate.of(2015, 3, 5),
                LocalDate.of(2016, 5, 5)
            ),
            epochStart = epochStart,
            now = now,
        )

        item.startInEpoch shouldbe 2
        item.endInEpoch shouldbe 16
        item.durationMonths shouldbe 14
    }

    @Test
    fun group_isCorrect() {
        val group = Group(
            "sample",
            listOf(
                Item(
                    Sample(
                        LocalDate.of(2015, 3, 5),
                        LocalDate.of(2016, 5, 5)
                    ),
                    epochStart = epochStart,
                    now = now,
                ),
                Item(
                    Sample(
                        LocalDate.of(2017, 6, 5),
                        LocalDate.of(2019, 1, 5)
                    ),
                    epochStart = epochStart,
                    now = now,
                ),
                Item(
                    Sample(
                        LocalDate.of(2016, 3, 5),
                        LocalDate.of(2020, 1, 13)
                    ),
                    epochStart = epochStart,
                    now = now,
                ),
            )
        )

        group.startInEpoch shouldbe 2
        group.endInEpoch shouldbe 60
        group.durationMonths shouldbe 58
    }

    @Test
    fun decadesBetween_isCorrect() {
        decadesBetween(
            LocalDate.of(1979, 12, 1),
            LocalDate.of(1981, 1, 1)
        ) shouldbe listOf(Decade(1980, 1))

        decadesBetween(
            LocalDate.of(1979, 12, 1),
            LocalDate.of(1982, 3, 1)
        ) shouldbe listOf(Decade(1980, 1))

        decadesBetween(
            LocalDate.of(1975, 12, 1),
            LocalDate.of(2025, 12, 1)
        ) shouldbe listOf(
            Decade(1980, 49),
            Decade(1990, 169),
            Decade(2000, 289),
            Decade(2010, 409),
            Decade(2020, 529),
        )
    }

    @Test
    fun renderData_isCorrect() {
        val groups = listOf(
            Group(
                "sample",
                listOf(
                    Item(
                        Sample(
                            LocalDate.of(2015, 3, 5),
                            LocalDate.of(2016, 5, 5)
                        ),
                        epochStart = epochStart,
                        now = now,
                    ),
                    Item(
                        Sample(
                            LocalDate.of(2016, 3, 5),
                            LocalDate.of(2020, 1, 13)
                        ),
                        epochStart = epochStart,
                        now = now,
                    ),
                )
            ),
            Group(
                "same period sample",
                listOf(
                    Item(
                        Sample(
                            LocalDate.of(2015, 3, 5),
                            LocalDate.of(2016, 5, 5)
                        ),
                        epochStart = epochStart,
                        now = now,
                    ),
                    Item(
                        Sample(
                            LocalDate.of(2016, 3, 5),
                            LocalDate.of(2020, 1, 13)
                        ),
                        epochStart = epochStart,
                        now = now,
                    ),
                )
            ),
            Group(
                "different sample",
                listOf(
                    Item(
                        Sample(
                            LocalDate.of(2015, 3, 5),
                            LocalDate.of(2016, 5, 5)
                        ),
                        epochStart = epochStart,
                        now = now,
                    ),
                    Item(
                        Sample(
                            LocalDate.of(2016, 3, 5),
                            LocalDate.of(2021, 1, 13)
                        ),
                        epochStart = epochStart,
                        now = now,
                    ),
                )
            )
        )

        val renderData = RenderData(groups)
        renderData.startInEpoch shouldbe 2
        renderData.endInEpoch shouldbe 72
        renderData.durationMonths shouldbe 70
    }
}
