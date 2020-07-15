package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.network.retrofit.Contract
import java.time.LocalDate

@Entity(
    tableName = "elections"
)
data class Election(
    @ColumnInfo(name = "election_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "election_name") override val name: String,
    @ColumnInfo(name = "election_date") override val date: LocalDate,
    @ColumnInfo(name = "election_type") val electionType: String
): Parliamentdotuk,
    Named,
    Dated


data class ApiElection(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.ELECTION_TYPE) val electionType: String
) {
    fun toElection() = Election(
        parliamentdotuk = parliamentdotuk,
        name = name,
        date = date,
        electionType = electionType
    )
}
