package org.wit.property_manager.views.propertylist
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
//import org.wit.property_manager.activities.PropertyMapsActivity
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.PropertyModel
import org.wit.property_manager.views.map.PropertyMapView
import org.wit.property_manager.views.property.PropertyView

class PropertyListPresenter(val view: PropertyListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getProperties() = app.properties.findAll()

    fun doAddProperty() {
        val launcherIntent = Intent(view, PropertyView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditProperty(property: PropertyModel) {
        val launcherIntent = Intent(view, PropertyView::class.java)
        launcherIntent.putExtra("property_edit", property)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowPropertiesMap() {
        val launcherIntent = Intent(view, PropertyMapView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getProperties() }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}