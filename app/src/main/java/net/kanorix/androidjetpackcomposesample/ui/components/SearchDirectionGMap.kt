package net.kanorix.androidjetpackcomposesample.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import net.kanorix.androidjetpackcomposesample.service.Route
import net.kanorix.androidjetpackcomposesample.ui.ViewType

@Composable
fun SearchDirectionGMap(
    selectedViewOption: ViewType,
    markerLatLng: LatLng,
    searchLatLng: LatLng,
    directionRoutes: List<Route>,
    modifier: Modifier = Modifier
) {
    // カメラポジションの初期値を指定
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerLatLng, 13f)
    }

    // latLng が変わるたびにカメラの位置を更新
    LaunchedEffect(markerLatLng) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(markerLatLng, 13f))
    }
    LaunchedEffect(searchLatLng) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(searchLatLng, 13f))
    }
    LaunchedEffect(directionRoutes) {
        val location = directionRoutes.firstOrNull()?.legs?.firstOrNull()?.startLocation
        if (location != null) {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.lat,
                        location.lng
                    ), 13f
                )
            )
        }
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        when (selectedViewOption) {
            ViewType.MARKER -> {
                MarkerInfoWindow(
                    state = MarkerState(position = markerLatLng),
                    title = "Title marker",
                    snippet = "Snippet marker"
                )
            }

            ViewType.SEARCH -> {
                MarkerInfoWindow(
                    state = MarkerState(position = searchLatLng),
                    title = "Title search",
                    snippet = "Snippet search"
                )
            }

            ViewType.DIRECTION -> {
                val colors = listOf(Color(84, 145, 245), Color(240, 254, 203), Color(188, 142, 185))
                directionRoutes.forEachIndexed { index, route ->
                    val paths = decodeAllStepsPolyline(route)
                    if (paths.isNotEmpty()) {
                        Polyline(
                            points = paths,
                            color = colors[index],
                            width = 14f
                        )
                    }
                }
            }
        }
    }
}

private fun decodeAllStepsPolyline(route: Route): List<LatLng> {
    return route.legs.flatMap { leg ->
        leg.steps.flatMap { step ->
            decodePolyline(step.polyline.points)
        }
    }
}

private fun decodePolyline(encoded: String?): List<LatLng> {
    if (encoded == null) {
        return listOf()
    }
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
        poly.add(p)
    }

    return poly
}