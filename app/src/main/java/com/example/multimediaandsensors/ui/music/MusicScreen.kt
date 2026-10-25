package com.example.multimediaandsensors.ui.music

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.multimediaandsensors.R

@Composable
fun MusicScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.music)
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Student Music",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "🎵",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Student Song",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    mediaPlayer?.let { player ->
                        if (player.isPlaying) {
                            player.pause()
                            isPlaying = false
                        } else {
                            player.start()
                            isPlaying = true
                        }
                    }
                }
            ) {
                Text(if (isPlaying) "Pause" else "Play")
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = {
                    mediaPlayer?.let { player ->
                        player.pause()
                        player.seekTo(0)
                        isPlaying = false
                    }
                }
            ) {
                Text("Stop")
            }
        }

        Spacer(Modifier.height(40.dp))

        OutlinedButton(onClick = onBack) {
            Text("Back to Home")
        }
    }
}
