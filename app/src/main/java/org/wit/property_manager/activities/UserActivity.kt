package org.wit.property_manager.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityUserBinding
import org.wit.property_manager.helpers.showImagePicker
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.UserModel
import org.wit.property_manager.models.Location
import timber.log.Timber
import timber.log.Timber.i


class UserActivity : AppCompatActivity() {
    // register callback for image picker
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityUserBinding
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var user = UserModel()
    // var location = Location(52.245696, -7.139102, 15f)
    //   val users = ArrayList<UserModel>()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        var edit = false
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        //   Timber.plant(Timber.DebugTree())
        i("User Activity started...")
        if (intent.hasExtra("user_edit")) {
            edit = true
            user = intent.extras?.getParcelable("user_edit")!!
            binding.userEmail.setText(user.email)
            binding.userPassword.setText(user.password)

            binding.btnAdd.setText(R.string.button_signup)
        }
        binding.btnAdd.setOnClickListener() {
            user.email = binding.userEmail.text.toString()
            user.password = binding.userPassword.text.toString()
            if (user.email.isEmpty()) {
                Snackbar.make(it, R.string.enter_userEmail, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.users.create(user.copy())
                } else {
                    app.users.create(user.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
       // registerImagePickerCallback()
        /*
        binding.userLocation.setOnClickListener {
            i ("Set Location Pressed")
        }
        binding.userLocation.setOnClickListener {
            //  var location = Location(52.245696, -7.139102, 15f)
            val location = Location(52.245696, -7.139102, 15f)
            if (user.zoom != 0f) {
                location.lat =  user.lat
                location.lng = user.lng
                location.zoom = user.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()

         */
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
/*
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            // image equals returned image
                            user.image = result.data!!.data!!
                            // use to get image to display
                            Picasso.get()
                                .load(user.image)
                                .into(binding.userImage)
                            binding.chooseImage.setText(R.string.change_user_image)
                            if (user.image != Uri.EMPTY) {
                                binding.chooseImage.setText(R.string.change_user_image)
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

    }

 */

}