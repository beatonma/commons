package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.JunctionTable
import java.time.LocalDate

@Entity(tableName = "bill_stages")
data class BillStage(
    @ColumnInfo(name = "billstage_id") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "billstage_description") val description: String,
    @ColumnInfo(name = "billstage_house") val house: House,
    @ColumnInfo(name = "billstage_session") val sessionId: Int,
    @ColumnInfo(name = "billstage_sittings") val sittings: List<LocalDate>,
    @ColumnInfo(name = "billstage_sitting_latest") val latestSitting: LocalDate,
): Parliamentdotuk


@Entity(tableName = "bills_x_stages", primaryKeys = ["billId", "stageId"])
data class BillXStage(
    val billId: ParliamentID,
    val stageId: ParliamentID,
) : JunctionTable
