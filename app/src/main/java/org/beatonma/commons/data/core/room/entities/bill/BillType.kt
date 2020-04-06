package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(
    tableName = "bill_types"
)
data class BillType(
    @field:Json(name = "name") @ColumnInfo(name = "billtype_name") @PrimaryKey val name: String,
    @field:Json(name = "description") @ColumnInfo(name = "billtype_description") val description: String?,
)
