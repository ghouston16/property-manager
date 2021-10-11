package org.wit.property_manager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityPropertyBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel
import timber.log.Timber
import timber.log.Timber.i

class PropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding
    var property = PropertyModel()
 //   val properties = ArrayList<PropertyModel>()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
     //   Timber.plant(Timber.DebugTree())
        i("Property Activity started...")

        binding.btnAdd.setOnClickListener() {
            property.title = binding.propertyTitle.text.toString()
            property.description =binding.propertyDescription.text.toString()
            if (property.title.isNotEmpty()) {
                app.properties.add(property.copy())
                i("add Button Pressed: ${property}")
                for (i in app.properties.indices)
                { i("Property[$i]:${this.app.properties[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                        .show()
            }
        }
    }
}