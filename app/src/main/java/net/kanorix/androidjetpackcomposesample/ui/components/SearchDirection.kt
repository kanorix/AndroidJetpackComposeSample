package net.kanorix.androidjetpackcomposesample.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import net.kanorix.androidjetpackcomposesample.BuildConfig
import net.kanorix.androidjetpackcomposesample.service.SearchDirectionService
import net.kanorix.androidjetpackcomposesample.service.DirectionsResponse
import net.kanorix.androidjetpackcomposesample.service.Route
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://maps.googleapis.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
private val service: SearchDirectionService = retrofit.create(SearchDirectionService::class.java)

@Composable
fun SearchDirection(setSearchResult: (List<Route>) -> Unit/* searchRootKeys: List<String> */) {
    // TODO 一旦固定値で
    val start = LatLng(35.68565, 139.76163)
    val end = LatLng(35.68565, 139.76163)
    val wayPoints = listOf(
        LatLng(35.71333, 139.86238),
        LatLng(35.71052, 139.84133),
        LatLng(35.69855, 139.79886),
        LatLng(35.70413, 139.83938),
    )
    search(start, end, wayPoints, onSearchSucceeded = { it ->
        setSearchResult(it?.routes ?: listOf())
    })
}

private fun search(
    start: LatLng,
    end: LatLng,
    wayPoints: List<LatLng>,
    onSearchSucceeded: (DirectionsResponse?) -> Unit
) {
    runBlocking {
        val response = async {
            service.getDirections(
                parseToLatLngString(start),
                parseToLatLngString(end),
                "optimize:true|" + wayPoints.joinToString("|") { parseToLatLngString(it) },
                BuildConfig.MAPS_DIRECTION_API_KEY
            )
        }.await().body()
        Log.d("direction-result", response.toString())
        onSearchSucceeded(response)
    }
}

private fun parseToLatLngString(latLng: LatLng): String {
    return "${latLng.latitude},${latLng.longitude}"
}