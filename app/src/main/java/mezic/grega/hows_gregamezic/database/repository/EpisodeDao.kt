package mezic.grega.hows_gregamezic.database.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import mezic.grega.hows_gregamezic.database.models.EpisodeModelDb

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM `EPISODES-TABLE` WHERE id = :showId")
    fun getAllShowsEpisodes(showId: String): List<EpisodeModelDb>

    @Insert
    fun insertAllEpisodes(episodes: List<EpisodeModelDb>)
}