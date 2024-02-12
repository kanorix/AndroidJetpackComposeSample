package net.kanorix.androidjetpackcomposesample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.kanorix.androidjetpackcomposesample.ui.theme.AndroidJetpackComposeSampleTheme

@Composable
fun ListScreen(
    mapViewModel: MapViewModel = viewModel()
) {
    val mapUiState by mapViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
    ) {
        for (maker in mapUiState.markers) {
            ListCard(maker)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun ListCard(maker: MakerInfo) {
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.End),
                text = maker.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = maker.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun ListCardPreview() {
    AndroidJetpackComposeSampleTheme {
        ListCard(
            MakerInfo(
                title = "スカイツリー",
                address = "東京都...",
                latitude = 0.0,
                longitude = 0.0
            )
        )
    }
}