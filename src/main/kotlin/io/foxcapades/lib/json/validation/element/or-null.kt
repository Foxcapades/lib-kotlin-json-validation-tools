@file:JvmName("OrNull")

package io.foxcapades.lib.json.validation.element

import io.foxcapades.lib.json.validation.errs.ErrorMessages
import io.foxcapades.lib.json.validation.errs.ValidationBuilder
import io.foxcapades.lib.json.validation.path.JPath
import io.foxcapades.lib.json4k.*
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Tests whether the receiver value is a non-null array and returns that array
 * if true, otherwise returns `null`.
 *
 * If the element is not `null`, but is also not a [JsonArray], an error will be
 * appended to the given [ValidationBuilder].
 *
 * @param path Path to the receiver element in the Json tree.
 *
 * @param errs Validation bundle.
 *
 * @receiver Json element to test.
 *
 * @return The receiver node cast to [JsonArray] if and only if the receiver
 * node is non-null and is of type array, otherwise returns `null`.
 */
fun JsonElement?.arrayOrNull(path: JPath, errs: ValidationBuilder)
: JsonArray? =
  if (this == null)
    null
  else if (type != JsonElementType.Array)
    errs.append(path, ErrorMessages.ValueNotAnArray, null)
  else
    this as JsonArray

/**
 * Tests whether the receiver value is a non-null decimal value and returns that
 * value if true, otherwise returns null.
 *
 * If the element is not `null`, but is also not numeric, an error will be
 * appended to the given [ValidationBuilder].
 *
 * @param path Path to the receiver element in the Json tree.
 *
 * @param errs Validation bundle.
 *
 * @receiver Json element to test.
 *
 * @return The receiver node value as a [BigDecimal] if and only if the receiver
 * node is non-null and is numeric, otherwise returns `null`.
 */
fun JsonElement?.bigDecimalOrNull(path: JPath, errs: ValidationBuilder)
: BigDecimal? =
  this?.let {
    when(it) {
      is JsonNumber  -> it.bigDecValue()
      is JsonInteger -> it.bigIntValue().toBigDecimal()
      else           -> errs.append(path, ErrorMessages.ValueNotNumeric, null)
    }
  }

fun JsonElement?.bigIntegerOrNull(path: JPath, errs: ValidationBuilder)
: BigInteger? =
  this?.let {
    when (it) {
      is JsonInteger -> it.bigIntValue()
      is JsonNumber  -> {
        try {
          it.bigDecValue().toBigIntegerExact()
        } catch (e: ArithmeticException) {
          errs.append(path, ErrorMessages.ValueNotIntegral, null)
        }
      }
      else           -> errs.append(path, ErrorMessages.ValueNotNumeric, null)
    }
  }

/**
 * Tests whether the receiver value is a non-null object and returns that object
 * if true, otherwise returns `null`.
 *
 * If the element is not `null`, but is also not a [JsonObject], an error will
 * be appended to the given [ValidationBuilder].
 *
 * @param path Path to the receiver element in the Json tree.
 *
 * @param errs Validation bundle.
 *
 * @receiver Json element to test.
 *
 * @return The receiver node cast to [JsonObject] if and only if the receiver
 * node is non-null and is of type object, otherwise returns `null`.
 */
fun JsonElement?.objectOrNull(path: JPath, errs: ValidationBuilder)
: JsonObject? =
  if (this == null)
    null
  else if (type != JsonElementType.Object)
    errs.append(path, ErrorMessages.ValueNotAnObject, null)
  else
    this as JsonObject

/**
 * Tests whether the receiver value is a non-null string and returns that string
 * value if true, otherwise returns `null`.
 *
 * If the element is not `null`, but is also not a [JsonString], an error will
 * be appended to the given [ValidationBuilder].
 *
 * @param path Path to the receiver element in the Json tree.
 *
 * @param errs Validation bundle.
 *
 * @receiver Json element to test.
 *
 * @return The string value of the receiver element if it is not null and is an
 * instance of [JsonString], else returns `null`.
 */
fun JsonElement?.stringOrNull(path: JPath, errs: ValidationBuilder): String? =
  if (this == null)
    null
  else if (type != JsonElementType.String)
    errs.append(path, ErrorMessages.ValueNotAString, null)
  else
    (this as JsonString).stringValue()

/**
 * Tests whether the receiver value is a non-null, non-blank string and returns
 * that string value if true, otherwise returns `null`.
 *
 * If the element is not `null`, but is also not a [JsonString], an error will
 * be appended to the given [ValidationBuilder].
 *
 * @param path Path to the receiver element in the Json tree.
 *
 * @param errs Validation bundle.
 *
 * @receiver Json element to test.
 *
 * @return The string value of the receiver element if it is not null, not
 * blank, and is an instance of [JsonString], else returns `null`.
 */
fun JsonElement?.nonBlankStringOrNull(path: JPath, errs: ValidationBuilder)
: String? =
  if (this == null)
    null
  else if (type != JsonElementType.String)
    errs.append(path, ErrorMessages.ValueNotAString, null)
  else
    with((this as JsonString).stringValue()) { ifBlank { null } }
