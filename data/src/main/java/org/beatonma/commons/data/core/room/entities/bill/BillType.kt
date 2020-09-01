package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.data.core.interfaces.Named

@Entity(
    tableName = "bill_types"
)
data class BillType(
    @ColumnInfo(name = "billtype_name") @PrimaryKey override val name: String,
    @ColumnInfo(name = "billtype_description") val description: String?,
): Named
