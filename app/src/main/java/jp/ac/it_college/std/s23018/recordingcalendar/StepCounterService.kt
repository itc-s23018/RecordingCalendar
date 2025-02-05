package jp.ac.it_college.std.s23018.recordingcalendar

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.StepEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.getCurrentDate
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StepCounterService: Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var totalSteps = 0
    private var previousTotalSteps = 0
    private var currentSteps = 0

    @Inject
    lateinit var repository: RecordRepository

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)

        loadPreviousSteps()
    }

    override fun onSensorChanged(event: SensorEvent?) {
       if(event?.sensor?.type == Sensor.TYPE_STEP_COUNTER){
           totalSteps = event.values[0].toInt()
           currentSteps = totalSteps - previousTotalSteps

           saveSteps(currentSteps)
       }
    }

    private fun saveSteps(stepCount: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val date = getCurrentDate().substring(0,10)
            val existingStep = repository.getStepByDate(date)

            if(existingStep == null){
                repository.insertStep(StepEntity(date= date, step = stepCount))
            } else {
                repository.insertStep(StepEntity(date= date, step = existingStep.step + stepCount))
            }
        }
    }

    private fun loadPreviousSteps() {
        CoroutineScope(Dispatchers.IO).launch {
            val date = getCurrentDate().substring(0,10)
            val existingStep = repository.getStepByDate(date)
            previousTotalSteps = existingStep?.step ?: 0
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)

    }

}