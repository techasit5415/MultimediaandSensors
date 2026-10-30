package com.example.multimediaandsensors.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.multimediaandsensors.R
import com.example.multimediaandsensors.sensor.StepCounterManager
import com.example.multimediaandsensors.ui.animation.LoadingAnimation

@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    onMusicClick: () -> Unit,
    onVideoClick: () -> Unit,
    onSensorClick: () -> Unit
) {
    val context = LocalContext.current
    val stepManager = remember { StepCounterManager(context) }

    DisposableEffect(Unit) {
        stepManager.start()
        onDispose {
            stepManager.stop()
        }
    }

    var headerVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        headerVisible = true
    }

    var expanded by remember { mutableStateOf(false) }
    val cardHeight by animateDpAsState(
        targetValue = if (expanded) 220.dp else 135.dp,
        label = "cardHeight"
    )

    var showMessage by remember { mutableStateOf(false) }

    var selected by remember { mutableStateOf(false) }
    val transition = updateTransition(
        targetState = selected,
        label = "cardTransition"
    )
    val scale by transition.animateFloat(
        label = "scale"
    ) { state ->
        if (state) 1.05f else 1f
    }
    val alpha by transition.animateFloat(
        label = "alpha"
    ) { state ->
        if (state) 1f else 0.85f
    }

    val goal = 10000
    val targetProgress = (stepManager.steps.toFloat() / goal).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 800),
        label = "progressAnimation"
    )

    var featureCardsVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AnimatedVisibility(
            visible = headerVisible,
            enter = fadeIn(animationSpec = tween(700)) + slideInVertically(
                initialOffsetY = { -50 },
                animationSpec = tween(700)
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Student Life",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "👋 Hello Student",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        val currentSteps = stepManager.steps
        val lottieRes = if (currentSteps >= 1000) R.raw.walking else R.raw.loading
        val lottieText = if (currentSteps >= 1000) "Walking Active ✨" else "Loading Activity..."

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoadingAnimation(
                modifier = Modifier.size(100.dp),
                rawRes = lottieRes
            )
            Text(
                text = lottieText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .clickable {
                    selected = !selected
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "👟 ${stepManager.steps} STEPS",
                    style = MaterialTheme.typography.titleLarge
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                if (expanded) {
                    Text(
                        text = "Goal: $goal steps | ${(animatedProgress * 100).toInt()}% completed",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Tap to toggle scale & opacity animation",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Collapse" else "Expand")
                    }
                    OutlinedButton(onClick = { showMessage = !showMessage }) {
                        Text(if (showMessage) "Hide Msg" else "Show Message")
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        AnimatedVisibility(
            visible = showMessage,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -30 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -30 })
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "🎉 Keep Going! Every step matters!",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dashboard Features",
                style = MaterialTheme.typography.titleMedium
            )
            Button(onClick = { featureCardsVisible = !featureCardsVisible }) {
                Text(if (featureCardsVisible) "Hide Features" else "Show Features")
            }
        }

        Spacer(Modifier.height(8.dp))

        AnimatedVisibility(
            visible = featureCardsVisible,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
            exit = fadeOut(animationSpec = tween(400)) + slideOutVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onCameraClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("📷 Camera")
                    }
                    Button(
                        onClick = onMusicClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🎵 Music")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onVideoClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🎥 Video")
                    }
                    Button(
                        onClick = onSensorClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("👟 Sensor")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
