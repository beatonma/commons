package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.ZeitgeistReason
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent
import java.time.LocalDateTime

@Entity(tableName = "zeitgeist_bills")
data class ZeitgeistBill(
    @ColumnInfo(name = "zeitgeist_bill_id") @PrimaryKey override val id: ParliamentID,
    @ColumnInfo(name = "zeitgeist_bill_reason") override val reason: ZeitgeistReason,
    @ColumnInfo(name = "zeitgeist_bill_priority") override val priority: Int,
    @ColumnInfo(name = "zeitgeist_bill_title") val title: String,
    @ColumnInfo(name = "zeitgeist_bill_lastupdate") val lastUpdate: LocalDateTime,
) : ZeitgeistContent
