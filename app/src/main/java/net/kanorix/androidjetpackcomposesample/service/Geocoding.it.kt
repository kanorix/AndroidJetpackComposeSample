package net.kanorix.androidjetpackcomposesample.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("maps/api/geocode/json")
    suspend fun getLatLng(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<GeocodingResponse>
}

data class GeocodingResponse(
    val results: List<GeocodeResult>,
    val status: String
)

data class GeocodeResult(
    val geometry: GeocodeGeometry
)

data class GeocodeGeometry(
    val location: GeocodeLocation
)

data class GeocodeLocation(
    val lat: Double,
    val lng: Double
)