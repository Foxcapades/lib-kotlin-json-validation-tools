package io.foxcapades.lib.json.validation.path

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame

@DisplayName("Int based root element")
internal class IntJPathTest : JPathContractTest() {
  override val expectedExtendString = "[123]"

  override val expectedRootString = "[123]"

  override fun createTarget(): JPath = NewJPathElement(123)

  @Nested
  @DisplayName("get path json")
  inner class GetPathJson {

    @Test
    @DisplayName("returns the expected json value")
    fun t0() {
      val path = NewJPathElement(32).extendMulti("taco", 33, "bell", 26, 0)
      val expect = """[32,"taco",33,"bell",26,0]"""

      assertEquals(expect, path.getPathJson().toJsonString())
    }
  }

  @Nested
  @DisplayName("NewJPathElement(int)")
  inner class NewJPathElement {

    @Test
    @DisplayName("multiple calls with the same input return the same output instance")
    fun t0() {
      val expected = NewJPathElement(69)
      val actual   = NewJPathElement(69)

      assertSame(expected, actual)
    }
  }

  @Nested
  @DisplayName("equals")
  inner class Equals {

    @Test
    @DisplayName("single path elements return false when the input values are different")
    fun t0() {
      val a = NewJPathElement(12)
      val b = NewJPathElement(13)

      assertNotEquals(a, b)
    }

    @Test
    @DisplayName("multi-path elements return false when the paths differ")
    fun t1() {
      val a = NewJPathElement(1).extendMulti(1, 2, 3, 4)
      val b = NewJPathElement(1).extendMulti(1, 3, 2, 4)

      assertNotEquals(a, b)
    }
  }
}