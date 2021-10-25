package org.wit.property_manager.models


import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.property_manager.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

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

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PropertyModel> {
        logAll()
        return properties
    }

    override fun create(property: PropertyModel) {
        property.id = generateRandomId()
        properties.add(property)
        serialize()
    }


    override fun update(property: PropertyModel) {
        // todo
    }
    override fun delete(property: PropertyModel) {
        // todo
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