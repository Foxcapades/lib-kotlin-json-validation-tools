package io.foxcapades.lib.json.validation.element

import io.foxcapades.lib.json.validation.errs.NewValidationBuilder
import io.foxcapades.lib.json.validation.path.NewJPathElement
import io.foxcapades.lib.json4k.Json
import io.foxcapades.lib.json4k.JsonElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("or-null.kt")
internal class OrNullTest {

  @Nested
  @DisplayName("objectOrNull")
  inner class ObjectOrNull {

    @Test
    @DisplayName("returns null if the input element is null")
    fun t1() {
      val input: JsonElement? = null
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.objectOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }

    @Test
    @DisplayName("returns null if the input element is not an object")
    fun t2() {
      val input: JsonElement = Json.newInt(22)
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.objectOrNull(path, errs))
      assertFalse(errs.isEmpty())
      assertTrue(path in errs)
    }

    @Test
    @DisplayName("returns the input element if it is an object")
    fun t3() {
      val input: JsonElement = Json.newObject(0)
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertSame(input, input.objectOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }
  }

  @Nested
  @DisplayName("arrayOrNull")
  inner class ArrayOrNull {

    @Test
    @DisplayName("returns null if the input element is null")
    fun t1() {
      val input: JsonElement? = null
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.arrayOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }

    @Test
    @DisplayName("returns null if the input element is not an array")
    fun t2() {
      val input: JsonElement = Json.newInt(22)
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.arrayOrNull(path, errs))
      assertFalse(errs.isEmpty())
      assertTrue(path in errs)
    }

    @Test
    @DisplayName("returns the input element if it is an array")
    fun t3() {
      val input: JsonElement = Json.newArray(0)
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertSame(input, input.arrayOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }
  }

  @Nested
  @DisplayName("stringOrNull")
  inner class StringOrNull {

    @Test
    @DisplayName("returns null if the input element is null")
    fun t1() {
      val input: JsonElement? = null
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.stringOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }

    @Test
    @DisplayName("returns null if the input element is not a string")
    fun t2() {
      val input: JsonElement = Json.newInt(22)
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.stringOrNull(path, errs))
      assertFalse(errs.isEmpty())
      assertTrue(path in errs)
    }

    @Test
    @DisplayName("returns the value of the input element if it is a string")
    fun t3() {
      val input: JsonElement = Json.newString("0")
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertSame("0", input.stringOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }
  }

  @Nested
  @DisplayName("nonBlankStringOrNull")
  inner class NonBlankStringOrNull {

    @Test
    @DisplayName("returns null if the input element is null")
    fun t1() {
      val input: JsonElement? = null
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.nonBlankStringOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }

    @Test
    @DisplayName("returns null if the input element is not a string")
    fun t2() {
      val input: JsonElement = Json.newInt(22)
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.nonBlankStringOrNull(path, errs))
      assertFalse(errs.isEmpty())
      assertTrue(path in errs)
    }

    @Test
    @DisplayName("returns null if the input element contains an empty string")
    fun t3() {
      val input: JsonElement = Json.newString("")
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.nonBlankStringOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }

    @Test
    @DisplayName("returns null if the input element contains a blank string")
    fun t4() {
      val input: JsonElement = Json.newString("   ")
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertNull(input.nonBlankStringOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }

    @Test
    @DisplayName("returns the value of the input element if it is a non-blank string")
    fun t5() {
      val input: JsonElement = Json.newString("0")
      val path = NewJPathElement(10)
      val errs = NewValidationBuilder()

      assertSame("0", input.nonBlankStringOrNull(path, errs))
      assertTrue(errs.isEmpty())
    }
  }
}