package com.example.plantin.classification.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.plantin.ui.theme.PlantInTheme

class HistoryActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            PlantInTheme {
                HistoryScreen()
            }
        }
    }
}

