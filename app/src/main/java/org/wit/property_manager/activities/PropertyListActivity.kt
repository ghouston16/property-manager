package org.wit.property_manager.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class PropertyListActivity : AppCompatActivity(), PropertyListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPropertyListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    val user = UserModel()
    var isAdmin = false
    val admin="gh@wit.ie"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadProperties()
       /*
        val propertyList = app.properties.findAll()
        for(x in propertyList) {
            Picasso.get().load(x.image).resize(200, 200).into(binding.imageIcon)
        }
        
        */
        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PropertyActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        when (item.itemId) {
            R.id.item_settings -> {
                var user = UserModel()
                user = intent.extras?.getParcelable("user")!!
                if(user.email==admin){
                    isAdmin=true
                }
                val launcherIntent = Intent(this, UserActivity::class.java)
                launcherIntent.putExtra("user_edit", user)
                launcherIntent.putExtra("isAdmin", isAdmin)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPropertyClick(property: PropertyModel) {
        val launcherIntent = Intent(this, PropertyActivity::class.java)
        launcherIntent.putExtra("property_edit", property)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadProperties() }
    }

    private fun loadProperties() {
        showProperties(app.properties.findAll())
    }

    fun showProperties (properties: List<PropertyModel>) {
        binding.recyclerView.adapter = PropertyAdapter(properties, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}