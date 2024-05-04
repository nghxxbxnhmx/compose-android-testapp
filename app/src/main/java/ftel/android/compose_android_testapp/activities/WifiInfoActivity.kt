package ftel.android.compose_android_testapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ftel.ptnetlibrary.services.IpConfigService

class WifiInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WifiInfoScreen()
        }
    }
}


@Preview(
    showSystemUi = true
)
@Composable
fun WifiInfoScreen() {
    val wifiService = IpConfigService()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "IP Address: ${wifiService.getIpAddress(true)}")
        Text(text = "Subnet Mask: ${wifiService.getSubnetMask()}")
        Text(text = "Gateway: ${wifiService.getGateway()}")
        Text(text = "Device MAC: ${wifiService.getDeviceMAC()}")
        Text(text = "Modem MAC: ${wifiService.getBSSID()}")
        Text(text = "SSID: ${wifiService.getSSID()}")
    }
}