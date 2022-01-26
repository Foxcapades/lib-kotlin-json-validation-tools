package io.foxcapades.lib.json.validation.errs

import io.foxcapades.lib.json.validation.path.JPath
import io.foxcapades.lib.json.validation.util.toJsonStringArr
import io.foxcapades.lib.json4k.Json
import io.foxcapades.lib.json4k.JsonObject

sealed interface KeyedError {

  val path: JPath

  val errors: List<String>

  fun appendError(error: String)

  fun toJson(): JsonObject
}

fun NewKeyedError(path: JPath, error: String): KeyedError =
  KeyedErrorImpl(path).also { it.appendError(error) }

private data class KeyedErrorImpl(
  override val path: JPath,
) : KeyedError {
  override val errors = ArrayList<String>(1)

  override fun appendError(error: String) {
    errors.add(error)
  }

  override fun toJson() = Json.newObject(2).also {
    it["path"] = path.getFullPath()
    it["errors"] = errors.toJsonStringArr()
  }
}