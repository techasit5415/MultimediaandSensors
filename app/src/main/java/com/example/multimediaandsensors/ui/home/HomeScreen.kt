package com.example.multimediaandsensors.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    onMusicClick: () -> Unit,
    onVideoClick: () -> Unit,
    onSensorClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Student Life",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "👤 Multimedia App",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onCameraClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📷 Camera Filter")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onMusicClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎵 Music Player")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onVideoClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎥 Video Player")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onSensorClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("👟 Step Counter")
        }
    }
}
