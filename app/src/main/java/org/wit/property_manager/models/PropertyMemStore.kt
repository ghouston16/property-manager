package org.wit.property_manager.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PropertyMemStore: PropertyStore {
    val properties = ArrayList<PropertyModel>()

    override fun findAll(): List<PropertyModel>{
        return properties
    }
    override fun create(property:PropertyModel){
        property.id = getId()
        properties.add(property)
        logAll()
    }

    override fun update(property: PropertyModel) {
        var foundProperty: PropertyModel? = properties.find { p -> p.id == property.id }
        if (foundProperty != null) {
            foundProperty.title = property.title
            foundProperty.description = property.description
            logAll()
        }
    }
    fun logAll(){
        properties.forEach{ i("${it}")}
    }
}