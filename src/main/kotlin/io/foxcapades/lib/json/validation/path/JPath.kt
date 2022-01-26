package io.foxcapades.lib.json.validation.path

import io.foxcapades.lib.json4k.Json
import io.foxcapades.lib.json4k.JsonArray
import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantLock

// region Definition

/**
 * Json Path Element
 *
 * Represents a single element in the path to a specific Json node in a Json
 * tree.
 *
 * Internal values may be either a [String] value or an [Int] value.
 *
 * @since v1.0.0
 */
sealed interface JPath {

  /**
   * Parent Json Path Element
   *
   * This value will only be `null` for the root element of the tree.
   */
  val parent: JPath?

  /**
   * The string representation of this value as if it were a root node in a
   * stringified Json path.
   *
   * For nodes wrapping a string value this will be that raw value.
   *
   * Example:
   * ```json
   * "foo"
   * ```
   *
   * For nodes wrapping an int value, this will be that value wrapped in square
   * brackets.
   *
   * Example:
   * ```json
   * [0]
   * ```
   */
  val rootString: String

  /**
   * The string representation of this value as if it were a child node in a
   * stringified Json path.
   *
   * For nodes wrapping a string value this will be that value with a `.`
   * character prepended.
   *
   * **Example**:
   * ```json
   * ".foo"
   * ```
   *
   * For nodes wrapping an int value, this will be that value wrapped in square
   * brackets.
   *
   * **Example**:
   * ```json
   * [0]
   * ```
   */
  val extensionString: String

  /**
   * Returns the full path to this [JPath] rendered as a single path
   * string.
   *
   * **Example**
   * ```json
   * "foo.bar[1].fizz[0][12].buzz"
   * ```
   *
   * @return The full stringified path to this [JPath].
   */
  fun getFullPath(): String

  /**
   * Returns a new [JPath] that wrapping the given value that is a child
   * of this element.
   *
   * @param element Value to wrap.
   *
   * @return A new child [JPath].
   */
  fun extend(element: String): JPath

  /**
   * Returns a new [JPath] that wrapping the given value that is a child
   * of this element.
   *
   * @param element Value to wrap.
   *
   * @return A new child [JPath].
   */
  fun extend(element: Int): JPath

  /**
   * Creates a series of child nodes, the first of which is a child of this
   * element, and each subsequent element a child of the previous.
   *
   * The last created element will be returned.
   *
   * All input values must be of one of the following types:
   *
   * * [String]
   * * [Int]
   *
   * If any element in the input array is not one of the above types, an
   * [IllegalArgumentException] will be thrown.
   *
   * **Example**:
   * ```
   * Root element: JPathElement("root")
   *
   * Input: ("hello", 12, 13, "goodbye")
   *
   * Returned element: JPathElement("goodbye")
   *
   * Full path: "root.hello[12][13].goodbye"
   * ```
   *
   * @param elements Ordered series of elements to create.
   *
   * @return The last created [JPath].
   */
  fun extendMulti(vararg elements: Any): JPath

  fun getPathJson(): JsonArray
}

// endregion

// region Caching

private val cacheLock = ReentrantLock()

private val cache = HashMap<Any, WeakReference<JPath>>(10)

private fun cleanCache() {
  cache.forEach { (k, v) ->
    if (v.get() == null)
      cache.remove(k)
  }
}

private fun getOrCreate(path: String) : JPath {
  cacheLock.lock()

  cleanCache()

  val tmp = cache[path]?.get()

  cacheLock.unlock()

  if (tmp == null) {
    val out = JPStringElement(path)
    cache[path] = WeakReference(out)
    return out
  }

  return tmp
}

private fun getOrCreate(path: Int) : JPath {
  cacheLock.lock()

  cleanCache()

  val tmp = cache[path]?.get()

  cacheLock.unlock()

  if (tmp == null) {
    val out = JPIntElement(path)
    cache[path] = WeakReference(out)
    return out
  }

  return tmp
}

// endregion

// region String Element

/**
 * Creates a new root [JPath] node wrapping the given string as its
 * value.
 *
 * The element created by this method will have a `null` value for its
 * [JPath.parent] value.
 *
 * @param element Value to wrap.
 *
 * @return A new, root [JPath] instance.
 */
fun NewJPathElement(element: String) = getOrCreate(element)

private data class JPStringElement(
  val value: String,
  override val parent: JPath? = null
) : JPath {

  private val path by lazy { buildFullPath() }

  private val hash by lazy { path.hashCode() }

  override val rootString
    get() = value

  override val extensionString by lazy { ".$value" }

  override fun getFullPath() = path

  override fun extend(element: String): JPath =
    JPStringElement(element, this)

  override fun extend(element: Int): JPath =
    JPIntElement(element, this)

  override fun extendMulti(vararg elements: Any) = multiExtend(elements)

  override fun getPathJson(): JsonArray = buildJsonPathArray()

  override fun equals(other: Any?): Boolean {
    if (this === other)
      return true
    if (other !is JPStringElement)
      return false
    if (value != other.value)
      return false
    if (parent != other.parent)
      return false
    return true
  }

  override fun hashCode() = hash

  override fun toString(): String {
    return "JPathElement(path='$path')"
  }
}

// endregion

// region Int Element

/**
 * Creates a new root [JPath] node wrapping the given int as its
 * value.
 *
 * The element created by this method will have a `null` value for its
 * [JPath.parent] value.
 *
 * @param element Value to wrap.
 *
 * @return A new, root [JPath] instance.
 */
fun NewJPathElement(element: Int) = getOrCreate(element)

private data class JPIntElement(
  val value: Int,
  override val parent: JPath? = null
) : JPath {

  private val path by lazy { buildFullPath() }

  private val hash by lazy { path.hashCode() }

  override val rootString by lazy { "[$value]" }

  override val extensionString
    get() = rootString

  override fun getFullPath() = path

  override fun extend(element: String): JPath =
    JPStringElement(element, this)

  override fun extend(element: Int): JPath =
    JPIntElement(element, this)

  override fun extendMulti(vararg elements: Any) = multiExtend(elements)

  override fun getPathJson(): JsonArray = buildJsonPathArray()

  override fun equals(other: Any?): Boolean {
    if (this === other)
      return true
    if (other !is JPIntElement)
      return false
    if (value != other.value)
      return false
    if (parent != other.parent)
      return false
    return true
  }

  override fun hashCode() = hash

  override fun toString(): String {
    return "JPathElement(path='$path')"
  }
}

// endregion

// region Mixins

@Suppress("NOTHING_TO_INLINE")
private inline fun JPath.multiExtend(elements: Array<out Any>)
: JPath {
  var cur = this

  elements.forEach {
    cur = when (it) {
      is String -> cur.extend(it)
      is Int    -> cur.extend(it)
      else      -> throw IllegalArgumentException()
    }
  }

  return cur
}

@Suppress("NOTHING_TO_INLINE")
private inline fun JPath.buildFullPath(): String {
  val buff = buildJoinablePathArray()

  var size = 0

  buff.forEach { size += it.length }

  val out = StringBuilder(size)

  buff.forEach { out.append(it) }

  return out.toString()
}

@Suppress("NOTHING_TO_INLINE")
private inline fun JPath.buildJsonPathArray(): JsonArray {
  var depth = getDepth()
  val array = Json.newArray(depth)
  var cur   = this

  depth--

  val nl = Json.newNull()

  for (i in 0 .. depth)
    array.add(nl)

  for (i in depth downTo 1) {
    when (cur) {
      is JPIntElement    -> array[i] = cur.value
      is JPStringElement -> array[i] = cur.value
    }
    cur = cur.parent!!
  }

  when (cur) {
    is JPIntElement    -> array[0] = cur.value
    is JPStringElement -> array[0] = cur.value
  }

  return array
}

@Suppress("NOTHING_TO_INLINE")
private inline fun JPath.buildJoinablePathArray(): Array<String> {
  var depth = getDepth()
  val buff  = arrayOfNulls<String>(depth)
  var cur   = this

  depth--

  for (i in depth downTo 1) {
    buff[i] = cur.extensionString
    cur = cur.parent!!
  }

  buff[0] = cur.rootString

  @Suppress("UNCHECKED_CAST")
  return buff as Array<String>
}

@Suppress("NOTHING_TO_INLINE")
private inline fun JPath.getDepth(): Int {
  var depth = 1
  var cur   = this

  while (cur.parent != null) {
    cur = cur.parent!!
    depth++
  }

  return depth
}

// endregion