package io.foxcapades.lib.json.validation.util

import io.foxcapades.lib.json4k.Json
import io.foxcapades.lib.json4k.JsonArray

internal fun Collection<*>.toJsonStringArr(): JsonArray {
  return Json.newArray(size).apply {
    this@toJsonStringArr.forEach { add(it as String) }
  }
}