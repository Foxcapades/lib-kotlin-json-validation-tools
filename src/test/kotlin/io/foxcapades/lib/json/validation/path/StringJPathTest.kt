package io.foxcapades.lib.json.validation.path

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame

internal class StringJPathTest : JPathContractTest() {
  override val expectedExtendString = ".hello"

  override val expectedRootString = "hello"

  override fun createTarget() = NewJPathElement("hello")

  @Nested
  @DisplayName("get path json")
  inner class GetPathJson {

    @Test
    @DisplayName("returns the expected json value")
    fun t0() {
      val path = NewJPathElement("nope").extendMulti("taco", 33, "bell", 26, 0)
      val expect = """["nope","taco",33,"bell",26,0]"""

      assertEquals(expect, path.getPathJson().toJsonString())
    }
  }

  @Nested
  @DisplayName("NewJPathElement(int)")
  inner class NewJPathElement {

    @Test
    @DisplayName("multiple calls with the same input return the same output instance")
    fun t0() {
      val expected = NewJPathElement("yes")
      val actual   = NewJPathElement("yes")

      assertSame(expected, actual)
    }
  }

  @Nested
  @DisplayName("equals")
  inner class Equals {

    @Test
    @DisplayName("single path elements return false when the input values are different")
    fun t0() {
      val a = NewJPathElement("yes")
      val b = NewJPathElement("no")

      assertNotEquals(a, b)
    }

    @Test
    @DisplayName("multi-path elements return false when the paths differ")
    fun t1() {
      val a = NewJPathElement("gravy").extendMulti(1, "2", 3, 4)
      val b = NewJPathElement("gravy").extendMulti(1, "3", 3, 4)

      assertNotEquals(a, b)
    }
  }
}