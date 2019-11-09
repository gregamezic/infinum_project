package mezic.grega.hows_gregamezic.database.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import mezic.grega.hows_gregamezic.database.models.ShowDetailModelDb

@Dao
interface ShowDetailDao {

    @Query("SELECT * FROM `show-detail-table` WHERE id = :id")
    fun getShowDetail(id: String): ShowDetailModelDb

    @Insert
    fun insertShowDetail(showDetail: ShowDetailModelDb)

}