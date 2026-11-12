package com.example.week9.soal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.week9.soal2.view.MusicApp
import com.example.week9.ui.theme.Week9Theme

class Soal2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week9Theme {
                MusicApp()
            }
        }
    }
}
