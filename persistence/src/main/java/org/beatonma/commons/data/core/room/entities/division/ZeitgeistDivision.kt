package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.ZeitgeistReason
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent
import java.time.LocalDate

@Entity(tableName = "zeitgeist_divisions")
data class ZeitgeistDivision(
    @ColumnInfo(name = "zeitgeist_division_id") @PrimaryKey override val id: ParliamentID,
    @ColumnInfo(name = "zeitgeist_division_reason") override val reason: ZeitgeistReason,
    @ColumnInfo(name = "zeitgeist_division_priority") override val priority: Int = 50,
    @ColumnInfo(name = "zeitgeist_division_title") val title: String,
    @ColumnInfo(name = "zeitgeist_division_date") val date: LocalDate,
    @ColumnInfo(name = "zeitgeist_division_passed") val passed: Boolean,
    @ColumnInfo(name = "zeitgeist_division_house") val house: House,
) : ZeitgeistContent
