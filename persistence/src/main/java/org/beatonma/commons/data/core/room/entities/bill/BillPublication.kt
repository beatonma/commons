package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import java.time.LocalDateTime


@Entity(tableName = "bill_publications")
data class BillPublicationData(
    @ColumnInfo(name = "billpub_id") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "billpub_title") val title: String,
    @ColumnInfo(name = "billpub_date") val date: LocalDateTime,
    @ColumnInfo(name = "billpub_type") val type: String,
) : Parliamentdotuk


@Entity(tableName = "bill_publication_links")
data class BillPublicationLink(
    @ColumnInfo(name = "billpublink_publication_id") @PrimaryKey val publicationId: ParliamentID,
    @ColumnInfo(name = "billpublink_title") val title: String,
    @ColumnInfo(name = "billpublink_url") val url: String,
    @ColumnInfo(name = "billpublink_content_type") val contentType: String,
)

data class BillPublication(
    @Embedded val publication: BillPublicationData,
    @Relation(parentColumn = "billpub_id", entityColumn = "billpublink_publication_id")
    val links: List<BillPublicationLink>,
)


@Entity(tableName = "bills_x_publications", primaryKeys = ["billId", "publicationId"])
data class BillXPublication(
    val billId: ParliamentID,
    val publicationId: ParliamentID,
)
