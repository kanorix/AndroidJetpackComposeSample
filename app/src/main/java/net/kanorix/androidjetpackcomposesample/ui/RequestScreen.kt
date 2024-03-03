package net.kanorix.androidjetpackcomposesample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun RequestScreen(
    mapViewModel: MapViewModel = viewModel()
) {
    val args by mapViewModel.args.observeAsState()

    // rememberとmutableStateOfを使用して状態を保持
    var value1 by remember { mutableStateOf("") }
    var value2 by remember { mutableStateOf("") }

    LaunchedEffect(args) {
        args?.let {
            value1 = it.value1
            value2 = it.value2
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        // Value1入力フィールド
        OutlinedTextField(
            value = value1,
            onValueChange = { value1 = it },
            label = { Text("Value1") },
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        // Value2入力フィールド
        OutlinedTextField(
            value = value2,
            onValueChange = { value2 = it },
            label = { Text("Value2") },
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        // GETリクエスト送信ボタン
        Button(onClick = {
            mapViewModel.sendGetRequest("Getしたよ1", "Getしたよ2")
        },
            modifier = Modifier.width(100.dp)
        ) {
            Text("GET")
        }

        // POSTリクエスト送信ボタン
        Button(onClick = {
            mapViewModel.sendPostRequest(value1, value2)
        },
        modifier = Modifier.width(100.dp)
        ) {
            Text("POST")
        }

        // PATCHリクエスト送信ボタン
        Button(onClick = {
            mapViewModel.sendPatchRequest(value1, value2)
        },
        modifier = Modifier.width(100.dp)
        ) {
            Text("PATCH")
        }

        // PUTリクエスト送信ボタン
        Button(onClick = {
            mapViewModel.sendPutRequest(value1, value2)
        },
        modifier = Modifier.width(100.dp)
        ) {
            Text("PUT")
        }

        // DELETEリクエスト送信ボタン
        Button(onClick = {
            mapViewModel.sendDeleteRequest()
            value1 = ""
            value2 = ""
        },
            modifier = Modifier.width(100.dp)
        ) {
            Text("DELETE")
        }
    }
}