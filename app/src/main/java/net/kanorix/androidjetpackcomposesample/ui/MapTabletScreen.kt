package net.kanorix.androidjetpackcomposesample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import net.kanorix.androidjetpackcomposesample.service.Route
import net.kanorix.androidjetpackcomposesample.ui.components.RadioGroup
import net.kanorix.androidjetpackcomposesample.ui.components.RadioGroupData
import net.kanorix.androidjetpackcomposesample.ui.components.SearchDirection
import net.kanorix.androidjetpackcomposesample.ui.components.SearchDirectionGMap
import net.kanorix.androidjetpackcomposesample.ui.components.SearchGeocode
import net.kanorix.androidjetpackcomposesample.ui.components.SelectBox

// INFO: secrets.propertiesに以下を追加
//   MAPS_API_KEY={自分の環境のGoogleMapのAPIキー}
//   MAPS_GEOCODING_API_KEY={自分の環境で作成した、Geocoding専用のAPIキー}
//   MAPS_DIRECTION_API_KEY={自分の環境で作成した、Direction専用のAPIキー}
// 詳しくは、https://docs.google.com/document/d/1xOKXxFj8BBf0_w1QOIvhB-hvb-QU9lobfxEJDRjydHk/edit?usp=sharing の 「経路探索APIを呼び出せるようにする」セクションを参照

enum class ViewType {
    MARKER,
    SEARCH,
    DIRECTION,
}


@Composable
fun SearchDirectionScreen() {
    val viewOptions = listOf(
        RadioGroupData("マーカー", ViewType.MARKER),
        RadioGroupData("検索", ViewType.SEARCH),
        RadioGroupData("経路探索", ViewType.DIRECTION),
    )
    var selectedViewOption by remember { mutableStateOf(viewOptions.first().value) }

    // プリセット 座標固定
    val markerOptions = listOf("シンガポール", "東京", "ニューヨーク")
    val latLngByName = mapOf(
        Pair("シンガポール", LatLng(1.35, 103.87)),
        Pair("東京", LatLng(35.69855, 139.79886)),
        Pair("ニューヨーク", LatLng(40.6643, -73.9385))
    )
    var selectedMarkerOption by remember { mutableStateOf(markerOptions.first()) }
    val selectedMarkerLatLng by remember(selectedMarkerOption) {
        mutableStateOf(
            latLngByName.getOrDefault(
                selectedMarkerOption,
                LatLng(24.2867, 153.9807)
            )
        )
    }

    // 検索
    var searchLatLng: LatLng by remember { mutableStateOf(LatLng(35.69855, 139.79886)) }
    // 経路探索
    var directionRoutes: List<Route> by remember { mutableStateOf(listOf()) }

    Row {
        Column {
            RadioGroup(
                elements = viewOptions,
                selectedOption = selectedViewOption,
                onOptionSelected = { selectedViewOption = it }
            )
            when (selectedViewOption) {
                ViewType.MARKER -> {
                    SelectBox(
                        markerOptions,
                        selectedMarkerOption,
                        modifier = Modifier.width(300.dp),
                        onOptionSelected = { selectedMarkerOption = it }
                    )
                }

                ViewType.SEARCH -> {
                    SearchGeocode(
                        latLng = searchLatLng,
                        setSearchResult = { searchLatLng = it })
                }

                ViewType.DIRECTION -> {
                    SearchDirection(setSearchResult = { directionRoutes = it })
                }
            }

            SearchDirectionGMap(
                selectedViewOption = selectedViewOption,
                markerLatLng = selectedMarkerLatLng,
                searchLatLng = searchLatLng,
                directionRoutes = directionRoutes,
            )
        }
    }


}

