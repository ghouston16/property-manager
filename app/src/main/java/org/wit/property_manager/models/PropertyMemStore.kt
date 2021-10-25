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

    override fun delete(property: PropertyModel) {
        properties.remove(property)
    }
    override fun update(property: PropertyModel) {
        var foundProperty: PropertyModel? = properties.find { p -> p.id == property.id }
        if (foundProperty != null) {
            foundProperty.title = property.title
            foundProperty.description = property.description
            foundProperty.type = property.type
            foundProperty.status = property.status
            foundProperty.agent = property.agent
            foundProperty.image = property.image
            foundProperty.lat = property.lat
            foundProperty.lng = property.lng
            foundProperty.zoom = property.zoom
            logAll()
        }
    }
    fun logAll(){
        properties.forEach{ i("${it}")}
    }
}