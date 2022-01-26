package io.foxcapades.lib.json.validation.path

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

@DisplayName("JPath Contract Test")
internal abstract class JPathContractTest {

  protected abstract val expectedExtendString: String

  protected abstract val expectedRootString: String

  protected abstract fun createTarget(): JPath

  @Nested
  @DisplayName("parent val")
  inner class ParentVal {

    @Test
    @DisplayName("if the current value is a root element, is null")
    fun t0() {
      val tgt = createTarget()

      assertNull(tgt.parent)
    }

    @Test
    @DisplayName("if the current value is not a root element, is not null")
    fun t1() {
      val par = createTarget()
      val tgt = par.extend(123)

      assertSame(par, tgt.parent)
    }
  }

  @Nested
  @DisplayName("rootString val")
  inner class RootString {

    @Test
    @DisplayName("contains the expected value")
    fun t0() {
      assertEquals(expectedRootString, createTarget().rootString)
    }
  }

  @Nested
  @DisplayName("extensionString val")
  inner class ExtensionString {

    @Test
    @DisplayName("contains the expected value")
    fun t0() {
      assertEquals(expectedExtendString, createTarget().extensionString)
    }
  }

  @Nested
  @DisplayName("get full path string")
  inner class GetFullPath {

    @Test
    @DisplayName("renders only the root path for a single level path")
    fun t0() {
      assertEquals(expectedRootString, createTarget().getFullPath())
    }

    @Test
    @DisplayName("renders the expected value for int extensions")
    fun t1() {
      val par = createTarget()
      val tgt = par.extend(10)

      assertEquals("$expectedRootString[10]", tgt.getFullPath())
    }

    @Test
    @DisplayName("renders the expected value for string extensions")
    fun t2() {
      val par = createTarget()
      val tgt = par.extend("hello")

      assertEquals("$expectedRootString.hello", tgt.getFullPath())
    }
  }

  @Nested
  @DisplayName("extend by string")
  inner class ContractExtend1 {

    @Test
    @DisplayName("returns a new child element")
    fun t0() {
      val par = createTarget()
      val tgt = par.extend("hi")

      assertSame(par, tgt.parent)
      assertNotEquals(par, tgt)
    }
  }

  @Nested
  @DisplayName("extend by int")
  inner class ContractExtend2 {

    @Test
    @DisplayName("returns a new child element")
    fun t0() {
      val par = createTarget()
      val tgt = par.extend(123)

      assertSame(par, tgt.parent)
      assertNotEquals(par, tgt)
    }
  }

  @Nested
  @DisplayName("extend by multiple values")
  inner class ContractExtend3 {

    @Test
    @DisplayName("returns a new child tree with all the expected intermediary elements")
    fun t0() {
      val par = createTarget()
      val tgt = par.extendMulti("hi", "hello", 123, 666)

      var cur = tgt
      for (i in 0 .. 3) {
        cur = cur.parent!!
      }

      assertSame(par, cur)
    }
  }

  @Nested
  @DisplayName("equals")
  inner class ContractEquals {

    @Test
    @DisplayName("single path elements return true when the input value is the same")
    fun t0() {
      val expect = createTarget()
      val actual = createTarget()

      assertEquals(expect, actual)
    }

    @Test
    @DisplayName("multi-path elements return true when the input values are the same")
    fun t1() {
      var expect = createTarget().extendMulti(1, 2, 3, 4)
      var actual = createTarget().extendMulti(1, 2, 3, 4)

      assertEquals(expect, actual)

      expect = createTarget().extendMulti("a", "b", "c", "d")
      actual = createTarget().extendMulti("a", "b", "c", "d")

      assertEquals(expect, actual)
    }
  }
}