package net.kanorix.androidjetpackcomposesample.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchDirectionService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") start: String,
        @Query("destination") end: String,
        @Query("waypoints") waypoints: String,
        @Query("key") apiKey: String,
    ): Response<DirectionsResponse>
}

data class DirectionsResponse(
    val status: String,
    val routes: List<Route>
)

data class Route(
    val summary: String,
    val legs: List<Leg>,
    val overviewPolyline: Polyline
)

data class Leg(
    val steps: List<Step>,
    val distance: Distance,
    val duration: Duration,
    val startAddress: String,
    val endAddress: String,
    val startLocation: Location,
    val endLocation: Location
)

data class Step(
    val distance: Distance,
    val duration: Duration,
    val htmlInstructions: String,
    val startLocation: Location,
    val endLocation: Location,
    val polyline: Polyline,
    val travelMode: String
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)