package com.example.week9.soal1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.week9.soal1.view.PokemonApp
import com.example.week9.ui.theme.Week9Theme

class Soal1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week9Theme {
                PokemonApp()
            }
        }
    }
}
