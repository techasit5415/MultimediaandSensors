package com.example.multimediaandsensors.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.sqrt

class StepCounterManager(
    private val context: Context
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var steps by mutableStateOf(0)
        private set

    private var initialSteps = -1
    private var lastAccelMagnitude = 9.8f

    fun start() {
        if (stepSensor != null) {
            sensorManager.registerListener(
                this,
                stepSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else if (accelerometer != null) {
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            if (initialSteps < 0) {
                initialSteps = totalSteps
            }
            steps = totalSteps - initialSteps
        } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER && stepSensor == null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val magnitude = sqrt(x * x + y * y + z * z)
            val delta = magnitude - lastAccelMagnitude
            lastAccelMagnitude = magnitude
            if (delta > 2.5f) {
                steps += 1
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
