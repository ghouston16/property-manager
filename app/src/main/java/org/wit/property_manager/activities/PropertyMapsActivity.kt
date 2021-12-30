package org.wit.property_manager.activities
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityPropertyMapsBinding
import org.wit.property_manager.databinding.ContentPropertyMapsBinding
import org.wit.property_manager.main.MainApp

class PropertyMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityPropertyMapsBinding
        private lateinit var contentBinding: ContentPropertyMapsBinding
        lateinit var map: GoogleMap

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            app = application as MainApp
            binding = ActivityPropertyMapsBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)

            contentBinding = ContentPropertyMapsBinding.bind(binding.root)
            contentBinding.mapView.onCreate(savedInstanceState)
            contentBinding.mapView.getMapAsync{
                map = it
                configureMap()
            }

        }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        val currentTitle: TextView = findViewById<TextView>(R.id.currentTitle)
        currentTitle.text = marker.title
        return false
    }
    fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.properties.findAll().forEach{
            val loc = LatLng(it.lat,it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }
    }