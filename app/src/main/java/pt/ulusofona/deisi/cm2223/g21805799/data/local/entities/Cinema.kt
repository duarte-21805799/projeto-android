package pt.ulusofona.deisi.cm2223.g21805799.data.local.entities

data class Cinema(
    val name: String,
    val latitude: Double,
    val longitude: Double
    ) {
    override fun toString(): String = name
}
