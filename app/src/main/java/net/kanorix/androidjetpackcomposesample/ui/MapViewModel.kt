package net.kanorix.androidjetpackcomposesample.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

val skyTree = LatLng(35.71011236307038, 139.8106637536605)

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    val cameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(skyTree, 10f))

    fun onClickPlace(latLng: LatLng, context: Context) {
        Log.d(this.javaClass.toString(), "Marked point")

        val filteredMarkers = uiState.value.markers.filter {
            it.latitude != latLng.latitude || it.longitude != latLng.longitude
        }

        addMarker(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            context = context,
        )
    }

    fun onClickPlaceOfInterest(poi: PointOfInterest, context: Context) {
        Log.d(this.javaClass.toString(), poi.name)

        addMarker(
            latitude = poi.latLng.latitude,
            longitude = poi.latLng.longitude,
            title = poi.name,
            context = context,
        )
    }

    private fun addMarker(
        title: String? = null,
        latitude: Double,
        longitude: Double,
        context: Context,
    ) {
        // https://qiita.com/Tsubasa12/items/6f4a5e271e9e4f081182
        val addresses: List<Address>? = Geocoder(context, Locale.JAPAN)
            .getFromLocation(latitude, longitude, 1)

        Log.d(this.javaClass.toString(), addresses?.map { it.toString() }.toString())

        val address = addresses?.first()?.getAddressLine(0)!!.split("、")[1]
        val prefecture = addresses.first().adminArea ?: ""
        val currentMarker = MakerInfo(
            latitude = latitude,
            longitude = longitude,
            title = title ?: prefecture,
            address = address,
        )
        _uiState.update { currentState ->
            currentState.copy(
                markers = currentState.markers.plus(
                    currentMarker
                )
            )
        }
    }
}