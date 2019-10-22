package mezic.grega.hows_gregamezic.shows.dummy

import mezic.grega.hows_gregamezic.episodes.dummy.Episode

data class Show(
    val id: Int,
    val name: String,
    val year: String,
    val description: String,
    val episodes: MutableList<Episode> = mutableListOf()
)