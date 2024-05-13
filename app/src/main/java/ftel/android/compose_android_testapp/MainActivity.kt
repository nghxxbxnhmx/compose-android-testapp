package ftel.android.compose_android_testapp

import android.Manifest
import android.content.Intent
import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import ftel.android.compose_android_testapp.activities.PingActivity
import ftel.android.compose_android_testapp.activities.TracerouteActivity
import ftel.android.compose_android_testapp.activities.WifiInfoActivity
import ftel.android.compose_android_testapp.dto.ButtonInfoDTO
import ftel.android.compose_android_testapp.utils.PermissionHelper


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            MaterialTheme {
                MainView()
                checkLocationPermission()
            }
        }
    }

    private fun checkLocationPermission() {
        PermissionHelper.showPermissionDialog(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            2002,
            "Location Permission",
            "This app requires access to your location to function properly."
        ) { allow ->
            if (allow) {
                // Nếu người dùng cho phép, bạn có thể thực hiện các hành động cần thiết ở đây
            } else {
                // Nếu người dùng từ chối, bạn có thể xử lý theo cách mong muốn
            }
        }
    }
}


@Preview(
    showSystemUi = true
)
@Composable
fun MainView() {
    val mContext = LocalContext.current
    val buttons = listOf(
        ButtonInfoDTO("Wifi Info", WifiInfoActivity::class.java, true),
        ButtonInfoDTO("Ping", PingActivity::class.java, true),
        ButtonInfoDTO("Traceroute", TracerouteActivity::class.java, true),
    )

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(buttons) { buttonInfo ->
            Button(
                onClick = {
                    mContext.startActivity(Intent(mContext, buttonInfo.composeActivityClass))
                },
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp),
                enabled = buttonInfo.enable
            ) {
                Text(
                    text = buttonInfo.text,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}