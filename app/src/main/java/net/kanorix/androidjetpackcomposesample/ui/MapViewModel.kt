package net.kanorix.androidjetpackcomposesample.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.kanorix.androidjetpackcomposesample.data.Args
import net.kanorix.androidjetpackcomposesample.data.PatchRequest
import net.kanorix.androidjetpackcomposesample.data.PostRequest
import net.kanorix.androidjetpackcomposesample.data.PutRequest
import retrofit2.HttpException

val skyTree = LatLng(35.71011236307038, 139.8106637536605)

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    val cameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(skyTree, 10f))

    fun onClickPlace(latLng: LatLng, context: Context) {
        Log.d(this.javaClass.toString(), "Marked point")

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
        val addresses: List<Address>? =
            Geocoder(context, Locale.JAPAN).getFromLocation(latitude, longitude, 1)

        Log.d(this.javaClass.toString(), addresses?.map { it.toString() }.toString())

        // パラメーターを設定する
        val params = Bundle()
        params.putDouble("latitude", latitude)
        params.putDouble("longitude", longitude)

        // イベントをログに保存する
        FirebaseAnalytics.getInstance(context).logEvent("add_maker", params)

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

    private val _args = MutableLiveData<Args>()
    val args: LiveData<Args> = _args

    fun sendGetRequest(value1: String, value2: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.get(value1, value2)
                if (response.isSuccessful) {
                    _args.value = response.body()?.args
                    val responseBody = response.body()
                    Log.d("MapViewModel", "成功: $responseBody")
                } else {
                    Log.e("MapViewModel", "HTTPエラー: ${response.code()}")
                }
            } catch (e: HttpException) {
                Log.e("MapViewModel", "HTTPエラー: ${e.code()} ${e.message}")
            } catch (e: Throwable) {
                Log.e("MapViewModel", "エラー: ", e)
            }
        }
    }

    fun sendPostRequest(value1: String, value2: String) {
        val postRequest = PostRequest(value1, value2)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.post(postRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MapViewModel", "成功: $responseBody")
                } else {
                    Log.e("MapViewModel", "HTTPエラー: ${response.code()}")
                }
            } catch (e: HttpException) {
                Log.e("MapViewModel", "HTTPエラー: ${e.code()} ${e.message}")
            } catch (e: Throwable) {
                Log.e("MapViewModel", "エラー: ", e)
            }
        }
    }

    fun sendPatchRequest(value1: String, value2: String) {
        val patchRequest = PatchRequest(value1, value2)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.patch(patchRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MapViewModel", "成功: $responseBody")
                } else {
                    Log.e("MapViewModel", "HTTPエラー: ${response.code()}")
                }
            } catch (e: HttpException) {
                Log.e("MapViewModel", "HTTPエラー: ${e.code()} ${e.message}")
            } catch (e: Throwable) {
                Log.e("MapViewModel", "エラー: ", e)
            }
        }
    }

    fun sendPutRequest(value1: String, value2: String) {
        val putRequest = PutRequest(value1, value2)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.put(putRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MapViewModel", "成功: $responseBody")
                } else {
                    Log.e("MapViewModel", "HTTPエラー: ${response.code()}")
                }
            } catch (e: HttpException) {
                Log.e("MapViewModel", "HTTPエラー: ${e.code()} ${e.message}")
            } catch (e: Throwable) {
                Log.e("MapViewModel", "エラー: ", e)
            }
        }
    }

    fun sendDeleteRequest() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.delete()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MapViewModel", "成功: $responseBody")
                } else {
                    Log.e("MapViewModel", "HTTPエラー: ${response.code()}")
                }
            } catch (e: HttpException) {
                Log.e("MapViewModel", "HTTPエラー: ${e.code()} ${e.message}")
            } catch (e: Throwable) {
                Log.e("MapViewModel", "エラー: ", e)
            }
        }
    }
}
