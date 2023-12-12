package uk.ac.tees.mad.W9620754

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.material3.Text

class splashScreenActivity : AppCompatActivity() {

    private var count: Int = 0;
    private var textloder: String ="Loading"
    private lateinit var text:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
    }


    @Composable
    fun SplashScreen() {
        var textLoader by remember { mutableStateOf("Loading") }
        var count by remember { mutableStateOf(0) }

        LaunchedEffect(key1 = count) {
            delay(300)
            if (count < 2000) {
                textLoader += "."
                count += 300
            } else {
                startActivity(Intent(applicationContext,Login::class.java))
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.playprodigy),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )

            // Text for loading
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = textLoader,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}