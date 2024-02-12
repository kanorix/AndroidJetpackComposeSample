package net.kanorix.androidjetpackcomposesample.ui

data class MapUiState(
    val currentMarker: MakerInfo? = null,
    val markers: List<MakerInfo> = listOf(),
)

data class MakerInfo(
    val title: String,
    val address: String = "",
    val latitude: Double,
    val longitude: Double,
)