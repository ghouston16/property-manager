package org.wit.property_manager.models


import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.property_manager.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE = "properties.json"

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<PropertyModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PropertyJSONStore(private val context: Context) : PropertyStore {

    var properties = mutableListOf<PropertyModel>()
    var userProperties = mutableListOf<PropertyModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PropertyModel> {
        logAll()
        return properties
    }

    override fun findAll(id: Long): MutableList<PropertyModel> {
        properties = findAll()
        for (property in properties){
            if (property.agent == id){
                userProperties.add(property)
            }
        }
        logAll()
        return userProperties
    }

    override fun findById(id: Long): PropertyModel? {
        TODO("Not yet implemented")
    }

    override fun create(property: PropertyModel) {
        property.id = generateRandomId()
        properties.add(property)
        userProperties.clear()
        serialize()
    }


    override fun update(property: PropertyModel) {
        val propertyList = findAll() as ArrayList<PropertyModel>
        var foundProperty: PropertyModel? = propertyList.find { p -> p.id == property.id }
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
        userProperties.clear()
        serialize()
    }
    override fun delete(property: PropertyModel) {
        properties.remove(property)
       // userProperties.remove(property)
        userProperties.clear()
        serialize()
        // todo - find how to pass id of property to be deleted
    }
    override fun deleteByUser(id: Long) {
        for (property in userProperties) {
            properties.remove(property)
        }
        userProperties.clear()
        serialize()
        // todo - find how to pass id of property to be deleted
    }

    override fun deleteAll() {
        properties.clear()
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(properties, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        properties = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        properties.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}