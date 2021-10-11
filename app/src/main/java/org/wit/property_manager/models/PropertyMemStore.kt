package org.wit.property_manager.models

import timber.log.Timber.i

class PropertyMemStore: PropertyStore {
    val properties = ArrayList<PropertyModel>()

    override fun findAll(): List<PropertyModel>{
        return properties
    }
    override fun create(property:PropertyModel){
        properties.add(property)
        logAll()
    }
    fun logAll(){
        properties.forEach{ i("${it}")}
    }
}