package com.example.yourdietbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.yourdietbuddy.ui.navigation.AppNavigation
import com.example.yourdietbuddy.ui.theme.YourDietBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourDietBuddyTheme {
                AppNavigation()
            }
        }
    }
}