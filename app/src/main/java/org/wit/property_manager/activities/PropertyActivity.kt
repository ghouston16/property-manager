package org.wit.property_manager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        var edit = false
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        //   Timber.plant(Timber.DebugTree())
        i("Property Activity started...")
        if (intent.hasExtra("property_edit")) {
            edit = true
            property = intent.extras?.getParcelable("property_edit")!!
            binding.propertyTitle.setText(property.title)
            binding.propertyDescription.setText(property.description)
            binding.btnAdd.setText(R.string.save_property)
        }
        binding.btnAdd.setOnClickListener() {
            property.title = binding.propertyTitle.text.toString()
            property.description = binding.propertyDescription.text.toString()
            if (property.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_property_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.properties.update(property.copy())
                } else {
                    app.properties.create(property.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_property, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}