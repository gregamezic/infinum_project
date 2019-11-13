package mezic.grega.hows_gregamezic.database.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mezic.grega.hows_gregamezic.database.models.EpisodeModelDb

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM `EPISODES-TABLE` WHERE show_id = :showId")
    fun getAllShowsEpisodes(showId: String): List<EpisodeModelDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEpisodes(episodes: List<EpisodeModelDb>)
}