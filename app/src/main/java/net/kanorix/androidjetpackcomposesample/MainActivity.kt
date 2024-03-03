package net.kanorix.androidjetpackcomposesample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import net.kanorix.androidjetpackcomposesample.ui.theme.AndroidJetpackComposeSampleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current;
            val navController = rememberNavController()

            // Navigationの変更時のイベントを設定する
            navController.addOnDestinationChangedListener { _, destination, _ ->
                // ルート名を取得する
                val screenName = destination.route ?: "unknown";

                // パラメーターを設定する
                val params = Bundle()
                params.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)

                // FirebaseのEventとして投げる
                FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
                Log.i("MainActivity", screenName)
            }
            AndroidJetpackComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(navController = navController)
                }
            }
        }
    }
}
