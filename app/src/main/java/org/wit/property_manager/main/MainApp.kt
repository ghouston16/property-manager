package org.wit.property_manager.main

import android.app.Application
import android.util.Property
import org.wit.property_manager.models.PropertyMemStore
import org.wit.property_manager.models.PropertyStore
import org.wit.property_manager.models.PropertyJSONStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var properties: PropertyStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        properties = PropertyJSONStore(applicationContext)
        i("Property started")
    }
}