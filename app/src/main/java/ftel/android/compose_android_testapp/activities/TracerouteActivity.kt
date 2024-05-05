package ftel.android.compose_android_testapp.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftel.ptnetlibrary.services.PingService
import com.ftel.ptnetlibrary.services.TracerouteService
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TracerouteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TracerouteView()
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun TracerouteView() {
    var tracerouteService = TracerouteService()
    var addressText by remember { mutableStateOf("facebook.com") }
    var resultText by remember { mutableStateOf("Result here") }
    var mContext: Context = LocalContext.current
    var isExecutingCommand by remember { mutableStateOf(false) }
    var hopList by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = addressText,
            onValueChange = { addressText = it },
            label = { Text("Address") },
            trailingIcon = {
                IconButton(onClick = {
                    addressText = ""
                }) {
                    Icon(
                        Icons.Rounded.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Button(
            onClick = {
                resultText = ""
                var lastTraceIp = ""
                thread {
                    isExecutingCommand = true
                    hopList = ArrayList()
                    repeat(30) { i ->
                        val trace = tracerouteService.trace(addressText, i)
                        var line = ""
                        if(trace.time.toFloat() == -1f) {
                            line = "#Hop${hopList.size +1}  -  Time out!"
                        } else {
                            line = "#Hop${hopList.size +1}  -  ${trace.ipAddress}  -  Time: ${trace.time}ms"
                        }
                        line += "\n${trace.domain}"
                        if (lastTraceIp.equals(trace.ipAddress)) {
                            return@repeat
                        }

                        hopList = hopList + line
                        resultText += line

                        lastTraceIp = trace.ipAddress
                    }
                    isExecutingCommand = false
                }
            },
            shape = RectangleShape,
            enabled = !isExecutingCommand
        ) {
            Text(
                text = "LAUNCH",
                Modifier.padding(0.dp)
            )
        }
        LazyColumn(
            horizontalAlignment = Alignment.Start
        ) {
            items(hopList) { hop ->
                Text(
                    text = hop,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Divider()
            }
        }

    }
}