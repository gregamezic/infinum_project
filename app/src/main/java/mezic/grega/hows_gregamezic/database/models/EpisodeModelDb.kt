package mezic.grega.hows_gregamezic.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "episodes-table",
    foreignKeys = [
        ForeignKey(
            entity = ShowModelDb::class,
            parentColumns = ["id"],
            childColumns = ["show_id"],
            onDelete = CASCADE
        )
    ]
)
data class EpisodeModelDb(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "show_id")
    val show_id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "episodeNumber")
    val episodeNumber: String,

    @ColumnInfo(name = "season")
    val season: String

) : Serializable