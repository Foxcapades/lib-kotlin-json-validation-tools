package io.foxcapades.lib.json.validation.element

import io.foxcapades.lib.json.validation.errs.ErrorMessages
import io.foxcapades.lib.json.validation.errs.ValidationBuilder
import io.foxcapades.lib.json.validation.path.JPath
import io.foxcapades.lib.json4k.*

fun JsonElement?.requireArray(path: JPath, errs: ValidationBuilder)
: JsonArray =
  if (this == null)
    errs.append(path, ErrorMessages.ValueIsRequired, Json.newArray(0))
  else if (type == JsonElementType.Null)
    errs.append(path, ErrorMessages.ValueIsNull, Json.newArray(0))
  else if (type != JsonElementType.Array)
    errs.append(path, ErrorMessages.ValueNotAnArray, Json.newArray(0))
  else
    this as JsonArray

fun JsonElement?.requireObject(path: JPath, errs: ValidationBuilder)
: JsonObject =
  if (this == null)
    errs.append(path, ErrorMessages.ValueIsRequired, Json.newObject(0))
  else if (type == JsonElementType.Null)
    errs.append(path, ErrorMessages.ValueIsNull, Json.newObject(0))
  else if (type != JsonElementType.Object)
    errs.append(path, ErrorMessages.ValueNotAnObject, Json.newObject(0))
  else
    this as JsonObject

fun JsonElement?.requireString(path: JPath, errs: ValidationBuilder): String =
  if (this == null)
    errs.append(path, ErrorMessages.ValueIsRequired, "")
  else if (type == JsonElementType.Null)
    errs.append(path, ErrorMessages.ValueIsNull, "")
  else if (type != JsonElementType.String)
    errs.append(path, ErrorMessages.ValueNotAString, "")
  else
    (this as JsonString).stringValue()

fun JsonElement?.requireNonBlankString(path: JPath, errs: ValidationBuilder)
: String =
  if (this == null)
    errs.append(path, ErrorMessages.ValueIsRequired, "")
  else if (type == JsonElementType.Null)
    errs.append(path, ErrorMessages.ValueIsNull, "")
  else if (type != JsonElementType.String)
    errs.append(path, ErrorMessages.ValueNotAString, "")
  else
    with((this as JsonString).stringValue()) { ifBlank {
      errs.append(path, ErrorMessages.ValueStringBlank, "")
    } }
