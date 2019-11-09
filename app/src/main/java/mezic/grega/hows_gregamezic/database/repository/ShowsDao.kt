package mezic.grega.hows_gregamezic.database.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import mezic.grega.hows_gregamezic.database.models.ShowModelDb

@Dao
interface ShowsDao {

    @Query("SELECT * FROM `shows-table`")
    fun getAllShows(): List<ShowModelDb>

    @Insert
    fun insertAllShows(show: List<ShowModelDb>)
}