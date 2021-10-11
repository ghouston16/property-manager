package org.wit.property_manager.main

import android.app.Application
import org.wit.property_manager.models.PropertyModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val properties = ArrayList<PropertyModel>()
    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Property App started")
  /*      properties.add(PropertyModel("One", "About one..."))
        properties.add(PropertyModel("Two", "About two..."))
        properties.add(PropertyModel("Three", "About three..."))
    */
    }
}