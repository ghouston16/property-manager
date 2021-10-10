package org.wit.property_manager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.property_manager.databinding.ActivityPropertyBinding

import timber.log.Timber
import timber.log.Timber.i

class PropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //introduce logging
        Timber.plant(Timber.DebugTree())
        Timber.i("Placemark Activity started..")
        setContentView(R.layout.activity_property)

        binding.btnAdd.setOnClickListener() {
            val propertyTitle = binding.propertyTitle.text.toString()
            if (propertyTitle.isNotEmpty()) {
                i("add Button Pressed: $propertyTitle")
            } else {
                Snackbar
                        .make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                        .show()
            }
        }
    }
}