package com.coding.meet.gaminiaikmp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor
import com.coding.meet.gaminiaikmp.theme.whiteColor
import gemini_ai_kmp_app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen() {
    Column(
        Modifier.fillMaxSize().background(lightBackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(Res.drawable.logo), contentDescription = "logo")
        Text(
            "Welcome To Gemini AI KMP App",
            
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold, color = whiteColor,
            textAlign = TextAlign.Center
        )
        Text(
            "Experience the power of AI in your Kotlin Multiplatform application!",
            
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold, color = whiteColor,
            textAlign = TextAlign.Center
        )

        LazyRow(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Image(
                    painter = painterResource(Res.drawable.jetpack),
                    contentDescription = "jetpack"
                )
            }
            item {
                Image(
                    painter = painterResource(Res.drawable.android),
                    contentDescription = "Android"
                )
            }
            item {
                Image(
                    painter = painterResource(Res.drawable.apple),
                    contentDescription = "iOS",
                )
            }
            item {
                Image(
                    painter = painterResource(Res.drawable.java),
                    contentDescription = "Desktop",
                )
            }
            item {
                Image(
                    painter = painterResource(Res.drawable.wasm),
                    contentDescription = "Web",
                )
            }
        }
    }
}