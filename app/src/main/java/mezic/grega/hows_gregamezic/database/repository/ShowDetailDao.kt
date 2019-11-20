package mezic.grega.hows_gregamezic.database.repository

import androidx.room.*
import mezic.grega.hows_gregamezic.database.models.ShowDetailModelDb

@Dao
interface ShowDetailDao {

    @Query("SELECT * FROM `show-detail-table` WHERE id = :id")
    fun getShowDetail(id: String): ShowDetailModelDb

    @Query("SELECT `like` FROM `show-detail-table` WHERE id = :id")
    fun getShowLike(id: String): Int

    @Query("UPDATE `show-detail-table` SET `like` = :like WHERE id = :showId")
    fun updateLikes(showId: String, like: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertShowDetail(showDetail: ShowDetailModelDb)
}