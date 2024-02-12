package net.kanorix.androidjetpackcomposesample.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = viewModel()
) {
    val mapUiState by mapViewModel.uiState.collectAsState()

    val context = LocalContext.current
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth(),
        cameraPositionState = mapViewModel.cameraPositionState,
        onMapClick = { mapViewModel.onClickPlace(it, context) },
        onPOIClick = { mapViewModel.onClickPlaceOfInterest(it, context) }
    ) {
        for (marker in mapUiState.markers) {
            Marker(
                state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                title = marker.title,
            )
        }
    }
}