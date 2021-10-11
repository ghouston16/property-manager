package org.wit.property_manager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityPropertyListBinding
import org.wit.property_manager.databinding.CardPropertyBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel

class PropertyListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPropertyListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PropertyAdapter(app.properties)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PropertyActivity::class.java)
                startActivityForResult(launcherIntent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class PropertyAdapter constructor(private var properties: List<PropertyModel>) :
    RecyclerView.Adapter<PropertyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPropertyBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val property = properties[holder.adapterPosition]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    class MainHolder(private val binding : CardPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: PropertyModel) {
            binding.propertyTitle.text = property.title
            binding.description.text = property.description
        }
    }
}