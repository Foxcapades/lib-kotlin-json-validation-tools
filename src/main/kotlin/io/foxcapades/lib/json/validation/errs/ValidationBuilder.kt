package io.foxcapades.lib.json.validation.errs

import io.foxcapades.lib.json.validation.path.JPath
import io.foxcapades.lib.json.validation.util.toJsonStringArr
import io.foxcapades.lib.json4k.Json
import io.foxcapades.lib.json4k.JsonObject

sealed interface ValidationBuilder {

  fun append(path: JPath, error: String)

  fun <T> append(path: JPath, error: String, returnValue: T): T

  fun isEmpty(): Boolean

  fun toJson(): JsonObject

  operator fun contains(path: JPath): Boolean
}

fun NewValidationBuilder(): ValidationBuilder = ValidationBuilderImpl()

private data class ValidationBuilderImpl(
  val map: MutableMap<JPath, KeyedError> = HashMap(1)
) : ValidationBuilder {

  override fun append(path: JPath, error: String) {
    with(map[path]) {
      if (this == null)
        map[path] = NewKeyedError(path, error)
      else
        this.appendError(error)
    }
  }

  override fun <T> append(path: JPath, error: String, returnValue: T): T {
    append(path, error)
    return returnValue
  }

  override fun isEmpty() = map.isEmpty()

  override fun toJson() = Json.newObject(1).apply {
    set("byKey", Json.newArray(map.size).apply {
      map.values.forEach { add(it.toJson()) }
    })
  }

  override fun contains(path: JPath) = path in map
}