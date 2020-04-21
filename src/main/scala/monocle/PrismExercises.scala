/*
 *  scala-exercises - exercises-monocle
 *  Copyright (C) 2015-2019 47 Degrees, LLC. <http://www.47deg.com>
 *
 */

package monoclelib

import monocle.Prism
import org.scalaexercises.definitions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

object PrismHelper {
  sealed trait Json
  case object JNull                     extends Json
  case class JStr(v: String)            extends Json
  case class JNum(v: Double)            extends Json
  case class JObj(v: Map[String, Json]) extends Json

  val jStr = Prism.partial[Json, String] { case JStr(v) => v }(JStr)

  import monocle.std.double.doubleToInt // Prism[Double, Int] defined in Monocle

  val jNum: Prism[Json, Double] = Prism.partial[Json, Double] { case JNum(v) => v }(JNum)

  val jInt: Prism[Json, Int] = jNum composePrism doubleToInt

  import monocle.macros.GenPrism
  val rawJNum: Prism[Json, JNum] = GenPrism[Json, JNum]
}

/** == Prism ==
 *
 * A [[http://julien-truffaut.github.io/Monocle/optics/prism.html `Prism`]] is an optic used to select part of a `Sum` type (also known as `Coproduct`), e.g. `sealed trait` or `Enum`.
 *
 * `Prisms` have two type parameters generally called `S` and `A`: `Prism[S, A]` where `S` represents the `Sum` and `A` a part of the Sum.
 *
 * Let’s take a simplified `Json` encoding:
 *
 * {{{
 *   sealed trait Json
 *   case object JNull extends Json
 *   case class JStr(v: String) extends Json
 *   case class JNum(v: Double) extends Json
 *   case class JObj(v: Map[String, Json]) extends Json
 * }}}
 *
 * We can define a `Prism` which only selects `Json` elements built with a `JStr` constructor by supplying a pair of functions:
 * - `getOption: Json => Option[String]`
 * - `reverseGet (aka apply): String => Json`
 *
 * {{{
 *   import monocle.Prism
 *
 *   val jStr = Prism[Json, String]{
 *     case JStr(v) => Some(v)
 *     case _       => None
 *   }(JStr)
 * }}}
 *
 * It is common to create a `Prism` by pattern matching on constructor, so we also added `partial` which takes a `PartialFunction`:
 *
 * {{{
 *   val jStr = Prism.partial[Json, String]{case JStr(v) => v}(JStr)
 * }}}
 *
 * @param name prism
 */
object PrismExercises extends AnyFlatSpec with Matchers with Section {

  import PrismHelper._

  /**
   * We can use the supplied `getOption` and `apply` methods as constructor and pattern matcher for `JStr`:
   */
  def exerciseGetOptionAndApply(res0: JStr, res1: Option[String], res2: Option[String]) = {

    jStr("hello") should be(res0)

    jStr.getOption(JStr("Hello")) should be(res1)

    jStr.getOption(JNum(3.2)) should be(res2)
  }

  /**
   * A `Prism` can be used in a pattern matching position:
   *
   * {{{
   *   def isLongString(json: Json): Boolean = json match {
   *     case jStr(v) => v.length > 100
   *     case _       => false}
   * }}}
   *
   * We can also use `set` and `modify` to update a `Json` only if it is a `JStr`:
   */
  def exerciseSetAndModify(res0: JStr, res1: JStr) = {

    jStr.set("Bar")(JStr("Hello")) should be(res0)

    jStr.modify(_.reverse)(JStr("Hello")) should be(res1)

  }

  /**
   * If we supply another type of `Json`, `set` and `modify` will be a no operation:
   */
  def exerciseSetAndModify2(res0: JNum, res1: JNum) = {

    jStr.set("Bar")(JNum(10)) should be(res0)

    jStr.modify(_.reverse)(JNum(10)) should be(res1)

  }

  /**
   * If we care about the success or failure of the update, we can use `setOption` or `modifyOption`:
   */
  def exerciseModifyOption(res0: Option[JStr], res1: Option[JNum]) = {

    jStr.modifyOption(_.reverse)(JStr("Hello")) should be(res0)

    jStr.modifyOption(_.reverse)(JNum(10)) should be(res1)

  }

  /**
   * As all other optics `Prisms` compose together:
   *
   * {{{
   *   import monocle.std.double.doubleToInt // Prism[Double, Int] defined in Monocle
   *
   *   val jNum: Prism[Json, Double] = Prism.partial[Json, Double]{case JNum(v) => v}(JNum)
   *
   *   val jInt: Prism[Json, Int] = jNum composePrism doubleToInt
   * }}}
   */
  def exerciseCompose(res0: JNum, res1: Option[Int], res2: Option[Int], res3: Option[String]) = {

    jInt(5) should be(res0)

    jInt.getOption(JNum(5.0)) should be(res1)

    jInt.getOption(JNum(5.2)) should be(res2)

    jInt.getOption(JStr("Hello")) should be(res3)

  }

  /**  == Prism Generation ==
   *
   * Generating `Prisms` for subclasses is fairly common, so we added a macro to simplify the process. All macros are defined in a separate module (see [[http://julien-truffaut.github.io/Monocle/modules.html modules]]).
   *
   * {{{
   *   import monocle.macros.GenPrism
   *
   *   val rawJNum: Prism[Json, JNum] = GenPrism[Json, JNum]
   * }}}
   */
  def exercisePrismGeneration(res0: Option[JNum], res1: Option[JNum]) = {

    rawJNum.getOption(JNum(4.5)) should be(res0)

    rawJNum.getOption(JStr("Hello")) should be(res1)

  }

  /**
   * If you want to get a `Prism[Json, Double]` instead of a `Prism[Json, JNum]`, you can compose `GenPrism` with `GenIso` (see `Iso` [[http://julien-truffaut.github.io/Monocle/optics/iso.html documentation]]):
   *
   * {{{
   *   import monocle.macros.GenIso
   *
   *   val jNum: Prism[Json, Double] = GenPrism[Json, JNum] composeIso GenIso[JNum, Double]
   *   val jNull: Prism[Json, Unit] = GenPrism[Json, JNull.type] composeIso GenIso.unit[JNull.type]
   * }}}
   *
   * A [[https://github.com/julien-truffaut/Monocle/issues/363 ticket]] currently exists to add a macro to merge these two steps together.
   *
   * == Prism Laws ==
   *
   * A `Prism` must satisfy all properties defined in `PrismLaws` from the core module. You can check the validity of your own `Prisms` using `PrismTests` from the `law` module.
   *
   * In particular, a `Prism` must verify that `getOption` and `reverseGet` allow a full round trip if the Prism matches i.e. if `getOption` returns a `Some`.
   *
   *
   */
  def exerciseLaws(res0: Boolean, res1: Boolean) = {
    val jStr = Prism.partial[Json, String] { case JStr(v) => v }(JStr)

    def partialRoundTripOneWay[S, A](p: Prism[S, A], s: S): Boolean =
      p.getOption(s) match {
        case None    => true // nothing to prove
        case Some(a) => p.reverseGet(a) == s
      }

    def partialRoundTripOtherWay[S, A](p: Prism[S, A], a: A): Boolean =
      p.getOption(p.reverseGet(a)) == Some(a)

    partialRoundTripOneWay(jStr, JStr("Hi")) should be(res0)

    partialRoundTripOtherWay(jStr, "Hi") should be(res1)

  }

}
