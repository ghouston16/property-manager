package org.wit.property_manager.models

interface PropertyStore {
    fun findAll(): List<PropertyModel>
    fun create(property: PropertyModel)
}