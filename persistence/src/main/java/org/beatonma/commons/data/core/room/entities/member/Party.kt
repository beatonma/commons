package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk

@Entity(tableName = "parties")
data class Party(
    @ColumnInfo(name = "party_$PARLIAMENTDOTUK") @PrimaryKey
    override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "party_name") override val name: String,
) : Parliamentdotuk,
    Named

val NoParty = Party(-1, "Unknown party")
