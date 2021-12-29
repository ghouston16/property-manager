package org.wit.property_manager.views.propertylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import org.wit.property_manager.adapters.PropertyListener
import org.wit.property_manager.databinding.ActivityPropertyListBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.R
import org.wit.property_manager.adapters.PropertyAdapter
import org.wit.property_manager.models.PropertyModel

class PropertyListView : AppCompatActivity(), PropertyListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPropertyListBinding
    lateinit var presenter: PropertyListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = PropertyListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadProperties()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddProperty() }
            R.id.item_map -> { presenter.doShowPropertiesMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPropertyClick(property: PropertyModel) {
        presenter.doEditProperty(property)

    }

    private fun loadProperties() {
        binding.recyclerView.adapter = PropertyAdapter(presenter.getProperties(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}