package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.JunctionTable
import java.time.LocalDateTime

@Entity(tableName = "bills")
data class BillData(
    @ColumnInfo(name = "billdata_id") @PrimaryKey val id: ParliamentID,
    @ColumnInfo(name = "billdata_title") val title: String,
    @ColumnInfo(name = "billdata_last_update") val lastUpdate: LocalDateTime,
    @ColumnInfo(name = "billdata_description") val description: String?,
    @ColumnInfo(name = "billdata_is_act") val isAct: Boolean,
    @ColumnInfo(name = "billdata_is_defeated") val isDefeated: Boolean,
    @ColumnInfo(name = "billdata_withdrawn_at") val withdrawnAt: LocalDateTime?,
    @ColumnInfo(name = "billdata_type_id") val billTypeId: ParliamentID,
    @ColumnInfo(name = "billdata_session_id") val sessionIntroducedId: ParliamentID,
    @ColumnInfo(name = "billdata_current_stage_id") val currentStageId: ParliamentID,
)


@Entity(tableName = "parliamentary_sessions")
data class ParliamentarySession(
    @ColumnInfo(name = "session_id") @PrimaryKey val id: ParliamentID,
    @ColumnInfo(name = "session_name") val name: String,
)

@Entity(tableName = "bills_x_sessions", primaryKeys = ["sessionId", "billId"])
data class BillXSession(
    val billId: ParliamentID,
    val sessionId: ParliamentID,
) : JunctionTable


data class Bill(
    val data: BillData,
    val type: BillType,
    val currentStage: BillStage,
    val stages: List<BillStage>,
    val sessionIntroduced: ParliamentarySession,
    val sessions: List<ParliamentarySession>,
    val publications: List<BillPublication>,
    val sponsors: List<BillSponsor>,
) : Votable, Commentable {
    @Ignore
    override val parliamentdotuk: ParliamentID = data.id
}


@Entity(tableName = "bill_types")
data class BillType(
    @ColumnInfo(name = "billtype_id") @PrimaryKey val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "billtype_name") val name: String,
    @ColumnInfo(name = "billtype_description") val description: String,
    @ColumnInfo(name = "billtype_category") val category: String,
)
