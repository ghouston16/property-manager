package org.wit.property_manager.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityPropertyBinding
import org.wit.property_manager.helpers.showImagePicker
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.models.Location
import org.wit.property_manager.models.UserModel
import timber.log.Timber
import timber.log.Timber.i


class PropertyActivity : AppCompatActivity() {
    // register callback for image picker
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityPropertyBinding
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    var property = PropertyModel()
    val user = UserModel()
    var edit = false
    var currentUser = UserModel()
    var isAdmin = false
    val admin = mutableListOf<String>("gh@wit.ie")
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp
        if (intent.hasExtra("current_user")) {
            currentUser = intent.extras?.getParcelable("current_user")!!
        }
        if (currentUser.email == admin[0]) {
            isAdmin = true
        }
        i("Current user: $currentUser is Admin = $isAdmin")
        //   Timber.plant(Timber.DebugTree())
        i("Property Activity started...")
        if (intent.hasExtra("property_edit")) {
            edit = true
            property = intent.extras?.getParcelable("property_edit")!!
            binding.propertyTitle.setText(property.title)
            binding.propertyDescription.setText(property.description)
            binding.propertyType.setText(property.type)
            binding.propertyStatus.setText(property.status)
            // property.agent = property.agent
            Picasso.get()
                .load(property.image)
                .into(binding.propertyImage)
            if (property.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_property_image)
            }
            binding.btnAdd.setText(R.string.save_property)
        }
        binding.btnAdd.setOnClickListener() {
            property.title = binding.propertyTitle.text.toString()
            property.description = binding.propertyDescription.text.toString()
            property.type = binding.propertyType.text.toString()
            property.status = binding.propertyStatus.text.toString()
            property.agent = currentUser.id
            property.image = property.image
            if (property.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_property_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.properties.update(property.copy())
                    Toast
                        .makeText(
                            app.applicationContext,
                            "Property Updated",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    app.properties.create(property.copy())
                    Toast
                        .makeText(
                            app.applicationContext,
                            "Property Added",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                val launcherIntent = Intent(this, PropertyListActivity::class.java)
                    launcherIntent.putExtra("current_user", currentUser).putExtra("user", currentUser)
                startActivityForResult(launcherIntent, 0)
            }
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
        binding.propertyLocation.setOnClickListener {
            i("Set Location Pressed")
        }
        binding.propertyLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (property.zoom != 0f) {
                location.lat = property.lat
                location.lng = property.lng
                location.zoom = property.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        /*
        binding.btnDelete.setOnClickListener {
            app.properties.delete(property)
            val launcherIntent = Intent(this, PropertyListActivity::class.java)
                .putExtra("current_user", currentUser)
            startActivityForResult(launcherIntent, 0)
            Toast
                .makeText(
                    app.applicationContext,
                    "Property Removed",
                    Toast.LENGTH_SHORT
                )
                .show()

        }

         */
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_property, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
      //          app.properties.clearUserProperties()
                val launcherIntent = Intent(this, PropertyListActivity::class.java)
                launcherIntent.putExtra("current_user", currentUser)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            // image equals returned image
                            property.image = result.data!!.data!!
                            // use to get image to display
                            Picasso.get()
                                .load(property.image)
                                .into(binding.propertyImage)
                            binding.chooseImage.setText(R.string.add_property_image)
                            if (property.image != Uri.EMPTY) {
                                binding.chooseImage.setText(R.string.change_property_image)
                            }
                        } // end of if
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            property.lat = location.lat
                            property.lng = location.lng
                            property.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

}