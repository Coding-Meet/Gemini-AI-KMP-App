package presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.lightBackgroundColor
import theme.whiteColor


@OptIn(ExperimentalResourceApi::class)
@Composable
fun WelcomeScreen() {
    Column(
        Modifier.fillMaxSize().background(lightBackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource("logo.png"), contentDescription = "logo")
        Text(
            "Welcome To Gemini AI KMP App",
            fontFamily = FontFamily.Cursive,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold, color = whiteColor,
            textAlign = TextAlign.Center
        )
        Text(
            "Experience the power of AI in your Kotlin Multiplatform application!",
            fontFamily = FontFamily.Cursive,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold, color = whiteColor,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource("jetpack.png"),
                contentDescription = "jetpack"
            )
            Image(
                painter = painterResource("android.png"),
                contentDescription = "Android"
            )
            Image(
                painter = painterResource("apple.png"),
                contentDescription = "iOS",
            )
            Image(
                painter = painterResource("java.png"),
                contentDescription = "Desktop",
            )
            Image(
                painter = painterResource("wasm.png"),
                contentDescription = "Web",
            )
        }
    }
}