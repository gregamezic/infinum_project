package mezic.grega.hows_gregamezic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shows")
data class Show(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String

): Serializable