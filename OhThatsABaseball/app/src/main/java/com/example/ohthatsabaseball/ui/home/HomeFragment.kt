package com.example.ohthatsabaseball.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ohthatsabaseball.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.roundToInt

class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.ohthatsabaseball)
        homeViewModel.observeAcc().observe(this, Observer {
            val it2 = (it * 10000).roundToInt().toDouble() / 10000
            textView.text = "Top Speed: "+it2.toString() + " m/s^2"
        })

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION)

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            // Success! There's a magnetometer.
            deviceSensors.forEach {
                Log.i("ACC", it.name)
            }
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        } else {
            // Failure! No accelerometer
            Toast.makeText(context, "NO ACCELEROMETER :(", Toast.LENGTH_LONG).show()
        }



        return root
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        Log.i("onSensorChanged", "${event.values[0]} ${event.values[1]} ${event.values[2]} ")
        // Do something with this sensor value.
        var totalA =
            Math.sqrt((event.values[0] * event.values[0] +
                    event.values[1] * event.values[1] +
                    event.values[2] * event.values[2]).toDouble())
        Log.i("totalA", totalA.toString())

        homeViewModel.setAcc(totalA)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // XXX Write me.  get viewModel, observe something
        baseball.setOnClickListener {
            homeViewModel.resetAcc()
        }
    }
}