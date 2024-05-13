package ftel.android.compose_android_testapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.ftel.ptnetlibrary.dto.TraceHopDTO
import com.ftel.ptnetlibrary.services.IpConfigService
import com.ftel.ptnetlibrary.services.PingService
import com.ftel.ptnetlibrary.services.TracerouteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TracerouteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TracerouteScreen()
        }
    }
}


@Preview(
    showSystemUi = true
)
@Composable
fun TracerouteScreen() {
    var txtHost: String by remember {
        mutableStateOf("facebook.com")
    }
    val traceHopList by remember { mutableStateOf(ArrayList<TraceHopDTO>()) }
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = txtHost, onValueChange = { txtHost = it })
        Button(onClick = {
            coroutineScope.launch {
                val service = TracerouteService()
                val endpoint : TraceHopDTO = service.getEndPoint(txtHost)
                for(i in 1..255) {
                    val traceHop : TraceHopDTO = service.trace(txtHost, i)
                    traceHopList.add(traceHop)
//                    if(traceHop.ipAddress.equals(endpoint.ipAddress)) break
                }
            }
        }, shape = RectangleShape) {
            Text(text = "TRACE")
        }
        LazyColumn {
            items(traceHopList) {hop ->
                Text(text = "Domain: ${hop.domain}")
                Text(text = "IpAddress: ${hop.ipAddress}")
                Text(text = "Domain: ${hop.time}\n\n")
            }
        }
    }
}