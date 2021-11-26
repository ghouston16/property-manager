package org.wit.property_manager.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import org.wit.property_manager.R
import org.wit.property_manager.activities.PropertyActivity
import org.wit.property_manager.adapters.PropertyAdapter
import org.wit.property_manager.adapters.PropertyListener
import org.wit.property_manager.databinding.ActivityPropertyListBinding
//import org.wit.property_manager.databinding.imageIcon
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.models.UserModel
import timber.log.Timber.i

class PropertyListActivity : AppCompatActivity(), PropertyListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPropertyListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapsIntentLauncher : ActivityResultLauncher<Intent>
    var user = UserModel()
    var currentUser = UserModel()
    var isAdmin = false
    val admin = mutableListOf<String>("gh@wit.ie")
    var properties= PropertyModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)

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
        i("$currentUser")

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadProperties()
        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
      //  menu.getItem(2).isVisible = true
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        user = intent.extras?.getParcelable("user")!!
       // currentUser = user
        i("$user")
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PropertyActivity::class.java)
                    launcherIntent.putExtra("current_user", currentUser)
                i("Current User $currentUser")
                startActivityForResult(launcherIntent,0)
            }
        }
        when (item.itemId) {
            R.id.item_settings -> {
                val launcherIntent = Intent(this, UserActivity::class.java)
                launcherIntent.putExtra("user_edit", user).putExtra("current_user", currentUser)
                startActivityForResult(launcherIntent,0)
                Toast
                    .makeText(
                        app.applicationContext,
                        "Getting User Settings",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
        when (item.itemId) {
            R.id.item_deleteAll -> {
                if (isAdmin) {
                    app.properties.deleteAll()
                } else {
                    app.properties.deleteByUser(currentUser.id)
                }
                Toast
                    .makeText(
                        app.applicationContext,
                        "Properties Deleted",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                val launcherIntent = Intent(this, PropertyListActivity::class.java)
                launcherIntent.putExtra("current_user", currentUser)
                    .putExtra("user", currentUser)
                startActivityForResult(launcherIntent, 0)
            }

            R.id.item_map -> {
                val launcherIntent = Intent(this, PropertyMapsActivity::class.java)
                mapsIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPropertyClick(property: PropertyModel) {
        val launcherIntent = Intent(this, PropertyActivity::class.java)
        launcherIntent.putExtra("property_edit", property).putExtra("current_user", currentUser)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadProperties() }
    }

    private fun loadProperties() {
        if (currentUser.email == admin[0]) {
            isAdmin = true
            showProperties(app.properties.findAll())
        } else {
            showProperties(app.properties.findAll(currentUser.id))
        }
    }

    fun showProperties (properties: List<PropertyModel>) {
            binding.recyclerView.adapter = PropertyAdapter(properties, this)
            binding.recyclerView.adapter?.notifyDataSetChanged()
    }
    private fun registerMapCallback() {
        mapsIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}