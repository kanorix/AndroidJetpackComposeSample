package net.kanorix.androidjetpackcomposesample.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import net.kanorix.androidjetpackcomposesample.BuildConfig
import net.kanorix.androidjetpackcomposesample.service.GeocodingResponse
import net.kanorix.androidjetpackcomposesample.service.GeocodingService
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://maps.googleapis.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
private val service: GeocodingService = retrofit.create(GeocodingService::class.java)

@Composable
fun SearchGeocode(
    latLng: LatLng,
    modifier: Modifier = Modifier,
    setSearchResult: (LatLng) -> Unit
) {
    Box(modifier.padding(5.dp)) {
        Column {
            Row {
                var searchKey by remember { mutableStateOf("Tokyo Sta.") }
                OutlinedTextField(
                    value = searchKey,
                    modifier = modifier.padding(horizontal = 16.dp),
                    onValueChange = { searchKey = it },
                )
                OutlinedButton(
                    modifier = modifier.padding(horizontal = 16.dp),
                    onClick = {
                        search(searchKey) { it ->
                            val searchResult = it?.results?.map {
                                LatLng(it.geometry.location.lat, it.geometry.location.lng)
                            }?.first() ?: LatLng(35.69855, 139.79886)

                            setSearchResult(searchResult)
                        }
                    }) {
                    Text(text = "Search")
                }
                Column {
                    Text(
                        text = "※いっぱい検索するとお金がかかるよ",
                        color = Color(80, 80, 80),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Thin,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Text(
                        text = "${latLng.latitude} ${latLng.longitude}",
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

private fun search(searchKey: String, onSearchSucceeded: (GeocodingResponse?) -> Unit) {
    runBlocking {
        val response = async {
            service.getLatLng(
                searchKey,
                BuildConfig.MAPS_GEOCODING_API_KEY
            )
        }.await().body()
        onSearchSucceeded(response)
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchGeocodePreview() {
    SearchGeocode(
        latLng = LatLng(35.69855, 139.79886),
        setSearchResult = {},
        modifier = Modifier.width(1000.dp)
    )
}