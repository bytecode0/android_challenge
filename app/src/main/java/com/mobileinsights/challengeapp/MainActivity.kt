package com.mobileinsights.challengeapp

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobileinsights.challengeapp.ui.theme.ChallengeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetNbaTeams()
                }
            }
        }
    }
}

@Composable
fun GetNbaTeams(modifier: Modifier = Modifier) {
    val amiiboList = remember { mutableStateOf<List<AmiiboItem>>(listOf()) }
    Column {
        Button(onClick = {
            asyncGetHttpRequest(
                endpoint = "https://www.amiiboapi.com/api/amiibo/",
                onSuccess = {
                    amiiboList.value = it.response.amiibo
                },
                onError = {
                    Log.d("ERROR", it.message.toString())
                }
            )
        }) {
            Text(
                text = "Get Amiibos",
                modifier = modifier
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(amiiboList.value) { item ->
                val imageBitmap = remember {
                    mutableStateOf<Bitmap?>(null)
                }
                asyncImageRequest(
                    endpoint = item.image,
                    onSuccess = { apiResponse ->
                        imageBitmap.value = apiResponse.response
                    },
                    onError = {

                    }
                )
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                    ) {
                        Column {
                            imageBitmap.value?.let {
                                Image(
                                    painter = BitmapPainter(it.asImageBitmap()),
                                    contentDescription = null, // Provide a description if needed
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp) // Adjust the height as needed
                                )
                            }
                            Text(
                                text = "Amiibo Series: ${item.amiiboSeries}",
                                modifier = modifier
                            )
                            Text(
                                text = "Name: ${item.name}",
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChallengeAppTheme {
        GetNbaTeams()
    }
}