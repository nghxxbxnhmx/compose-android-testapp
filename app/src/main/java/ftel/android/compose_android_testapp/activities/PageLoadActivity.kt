package ftel.android.compose_android_testapp.activities


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.IOException
import kotlin.concurrent.thread
import com.ftel.ptnetlibrary.services.PageLoadService

class PageLoadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PageLoadView()
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun PageLoadView() {
    var pageLoadService = PageLoadService()
    var urlText by remember { mutableStateOf("hi.fpt.vn") }
    var pageLoadTime by remember { mutableStateOf("") }
    var listTimer by remember { mutableStateOf(ArrayList<Long>()) }
    var isExecuting = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = urlText,
            onValueChange = { urlText = it },
            label = { Text("URL") },
            trailingIcon = {
                IconButton(onClick = { urlText = "" }) {
                    Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                }
            }
        )


        Button(onClick = {
            thread {
                isExecuting.value = true
                listTimer.clear()
                repeat(10) {
                    val time = pageLoadService.pageLoadTimer(urlText)
                    pageLoadTime += "Url: $urlText - Load time: ${time}ms\n\n"

                }
                isExecuting.value = false
            }
        }, shape = RectangleShape, enabled = !isExecuting.value) {
            Text(
                text = "START",
                Modifier
                    .padding(0.dp)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = "\n\n$pageLoadTime",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )


    }
}

