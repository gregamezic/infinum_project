package mezic.grega.hows_gregamezic.database.repository

import androidx.room.Room
import mezic.grega.hows_gregamezic.ShowApp
import mezic.grega.hows_gregamezic.database.ShowDatabase
import mezic.grega.hows_gregamezic.database.models.ShowModelDb
import java.util.concurrent.Executors

private const val FILENAME = "shows"

object ShowRepository {

    private var shows = mutableListOf<ShowModelDb>()

    val database = Room.databaseBuilder(
        ShowApp.instance,
        ShowDatabase::class.java,
        "ShowsDb"
    )
        .fallbackToDestructiveMigration()
        .build()


    // EXECUTOR
    private val executor = Executors.newSingleThreadExecutor()

    /**
     * GET ALL SHOWS
     */
    fun getShows(callback: (List<ShowModelDb>) -> Unit) {
        // Use database
        executor.execute {
            shows = database.showsDao().getAllShows().toMutableList()
            callback(shows.toList())
        }
    }
}