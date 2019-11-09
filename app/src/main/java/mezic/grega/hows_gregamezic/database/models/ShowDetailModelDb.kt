package mezic.grega.hows_gregamezic.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "show-detail-table")
data class ShowDetailModelDb(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "likesCount")
    val likesCount: Int

): Serializable