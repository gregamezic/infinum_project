package mezic.grega.hows_gregamezic

data class Show(
    val id: Int,
    val name: String,
    val year: String,
    val description: String,
    val episodes: List<Episode>
)