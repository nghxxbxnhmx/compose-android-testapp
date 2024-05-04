package ftel.android.compose_android_testapp.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ftel.ptnetlibrary.services.PingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingScreen()
        }
    }

}

@Preview(
    showSystemUi = true
)
@Composable
fun PingScreen() {
    var txtHost by remember {
        mutableStateOf("facebook.com")
    }
    var lblResult by remember {
        mutableStateOf("")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = txtHost, onValueChange = { txtHost = it })
        Button(onClick = {
            coroutineScope.launch {
                val p = PingService().execute(txtHost)

                lblResult = "${p.address}\n${p.ip}\n${String.format("%.2f", p.time)}"
            }
        }) {
            Text(text = "LAUNCH")
        }
        Text(text = lblResult)
    }
}