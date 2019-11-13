package mezic.grega.hows_gregamezic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import mezic.grega.hows_gregamezic.database.models.EpisodeModelDb
import mezic.grega.hows_gregamezic.database.models.ShowDetailModelDb
import mezic.grega.hows_gregamezic.database.models.ShowModelDb
import mezic.grega.hows_gregamezic.database.repository.EpisodeDao
import mezic.grega.hows_gregamezic.database.repository.ShowDetailDao
import mezic.grega.hows_gregamezic.database.repository.ShowsDao

@Database(
    version = 1,
    entities = [
        ShowModelDb::class,
        ShowDetailModelDb::class,
        EpisodeModelDb::class
    ]
)

abstract class ShowDatabase: RoomDatabase() {

    abstract fun showsDao(): ShowsDao

    abstract fun showDetailDao(): ShowDetailDao

    abstract fun episodeDao(): EpisodeDao
}