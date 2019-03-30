package com.challenge.searchforflights.flightsresults.model.jsonparser

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * A wrapper of the normal add
 * Returns the underlying object to be used like a chain
 *
 * @param property the name of the property to be added
 * @param jsonElement the JsonElement to be added
 * @return the underlying object after addition
 */
fun JsonObject.addJsonElementProperty(property: String, jsonElement: JsonElement): JsonObject {
    add(property, jsonElement)
    return this
}

/**
 * A wrapper of the normal remove
 * Returns the underlying object to be used like a chain
 *
 * @param property the name of the property to be removed
 * @return the underlying object after removal
 */
fun JsonObject.removeProperty(property: String): JsonObject {
    remove(property)
    return this
}

/**
 * A wrapper of the normal remove to remove many properties at once.
 * Returns the underlying object to be used like a chain
 *
 * @param properties a List of String with the names of the properties to be removed
 * @return the underlying object after removal
 */
fun JsonObject.removeProperties(properties: List<String>): JsonObject {
    properties.map {
        remove(it)
    }
    return this
}

/**
 * Get the entire JsonObject based on its id
 *
 * @param objectId the id of the specific object you are looking for
 * @param defaultId the id used on the list of complex objects. Default = "Id"
 *
 * @return the underlying object
 */
fun JsonArray.getCopyOfObjectById(objectId: String, defaultId: String = "Id"): JsonObject? {
    return find { jsonElement ->
        jsonElement.asJsonObject.get(defaultId).asString == objectId
    }?.deepCopy()?.asJsonObject
}

/**
 * Get a list of full JsonElements based on a list of ids
 *
 * @param listOfIdsNodeName the list of ids you want to match
 * @param jsonArrayReference the list of full objects used as reference
 *
 * @return a list of full JsonElements matching the ids
 */
fun JsonObject.getListOfJsonElementBasedOnListOfIds(
    listOfIdsNodeName: String,
    jsonArrayReference: JsonArray
): List<JsonElement> =
    getAsJsonArray(listOfIdsNodeName)?.let {
        it.map { id -> jsonArrayReference.getCopyOfObjectById(id.asString) as JsonElement }
    } ?: emptyList()


/**
 * Convert a List of JsonElement into a JsonArray
 */
fun List<JsonElement>.toJsonArray(): JsonArray {
    val jsonArray = JsonArray()
    map { jsonArray.add(it) }
    return jsonArray
}

/**
 * Add a JsonArray as a new property to the underlying JsonObject based
 * on a list of ids using a JsonArray of full objects as reference
 *
 * @param propertyName the name of the new property
 * @param jsonArrayReference the JsonArray of full objects to be used as reference
 * @param nodeNameOfListId the name of the node with a list of ids you want to match
 *
 * @return the underlying object after addition
 */
fun JsonObject.addJsonArrayOfComplexObjectsFromIds(
    propertyName: String,
    jsonArrayReference: JsonArray,
    nodeNameOfListId: String
): JsonObject {
    add(
        propertyName,
        getListOfJsonElementBasedOnListOfIds(nodeNameOfListId, jsonArrayReference).toJsonArray()
    )
    return this
}

/**
 * Add a batch of JsonArray as a new property to the underlying JsonObject based
 * on a list of ids using a JsonArray of full objects as reference
 *
 * @param batch a List of JsonArrayBatchItem
 * @return the underlying object after additions
 */
fun JsonObject.addJsonArrayInBatch(batch: List<JsonArrayBatchItem>): JsonObject {
    batch.map {
        addJsonArrayOfComplexObjectsFromIds(
            it.propertyName,
            it.jsonArrayReference,
            it.nodeNameOfListId
        )
    }

    return this
}

/**
 * Add a batch of JsonObject as a new property to the underlying JsonObject based
 * on a list of ids using a JsonArray of full objects as reference
 *
 * @param batch a List of JsonObjectBatchItem
 * @return the underlying object after additions
 */
fun JsonObject.addJsonObjectsInBatch(batch: List<JsonObjectBatchItem>): JsonObject {
    batch.map { batchItem ->
        add(
            batchItem.propertyName,
            batchItem.jsonArrayReference.getCopyOfObjectById(
                get(batchItem.childNameWithId).asString
            )
        )
    }

    return this
}

/**
 * A data model that represents a item on a JsonObject batch
 *
 * @property propertyName the name of the new property
 * @property jsonArrayReference the JsonArray of full objects to be used as reference
 * @property childNameWithId the child name with the id you want to match
 */
data class JsonObjectBatchItem(
    val propertyName: String,
    val jsonArrayReference: JsonArray,
    val childNameWithId: String
)

/**
 * A data model that represents a item on a JsonArray batch
 *
 * @property propertyName the name of the new property
 * @property jsonArrayReference the JsonArray of full objects to be used as reference
 * @property nodeNameOfListId the name of the node with a list of ids you want to match
 */
data class JsonArrayBatchItem(
    val propertyName: String,
    val jsonArrayReference: JsonArray,
    val nodeNameOfListId: String
)