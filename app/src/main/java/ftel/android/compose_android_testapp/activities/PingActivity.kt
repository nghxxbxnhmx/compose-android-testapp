package ftel.android.compose_android_testapp.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftel.ptnetlibrary.services.PingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

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
//    val ping = PingService()
    var txtHost by remember {
        mutableStateOf("facebook.com")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    var resultText by remember { mutableStateOf("") }
    var isExecuting by remember { mutableStateOf(false) }
    var ping by remember { mutableStateOf(PingService()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = txtHost,
            onValueChange = { txtHost = it },
            label = { Text("URL") },
            trailingIcon = {
                IconButton(onClick = {
                    txtHost = ""
                }) {
                    Icon(
                        Icons.Rounded.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        )

        Button(
            onClick = {
                thread {
                    isExecuting = true
                    resultText = ""
                    for (i in 1..10) {
                        var singlePingResult =
                            ping.execute(address = txtHost)
                        resultText += "Ip Address: ${singlePingResult?.ip} - Time: ${singlePingResult?.time}ms\n\n"
                    }
                    isExecuting = false
                }
            },
            modifier = Modifier.padding(all = 8.dp),
            shape = RectangleShape,
            enabled = !isExecuting
        ) {
            Text(text = "Ping")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = "\n\n$resultText",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}