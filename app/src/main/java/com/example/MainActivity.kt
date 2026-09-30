package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.StudyDatabase
import com.example.data.repository.StudyRepository
import com.example.ui.RachaScreen
import com.example.ui.theme.RachaTheme
import com.example.ui.viewmodel.RachaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = StudyDatabase.getInstance(applicationContext)
        val repository = StudyRepository(database.studyDao())

        setContent {
            RachaTheme {
                val viewModel: RachaViewModel = viewModel(
                    factory = RachaViewModel.provideFactory(repository)
                )
                RachaScreen(viewModel = viewModel)
            }
        }
    }
}
