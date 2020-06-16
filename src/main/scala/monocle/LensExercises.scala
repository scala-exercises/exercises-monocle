/*
 * Copyright 2017-2020 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package monoclelib

import monocle.Lens
import monocle.macros.GenLens
import monocle.macros.Lenses
import org.scalaexercises.definitions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

object LensHelper {
  case class Address(streetNumber: Int, streetName: String)

  val address      = Address(10, "High Street")
  val streetNumber = GenLens[Address](_.streetNumber)

  def neighbors(n: Int): List[Int] =
    if (n > 0) List(n - 1, n + 1) else List(n + 1)

  case class Person(name: String, age: Int, address: Address)
  val john = Person("John", 20, address)

  val addressLens = GenLens[Person](_.address)

  @Lenses
  case class Point(x: Int, y: Int)
  val p = Point(5, 3)

  @Lenses("_")
  case class OtherPoint(x: Int, y: Int)
  val op = OtherPoint(5, 3)

}

/** == Lens ==
 *
 * A [[http://julien-truffaut.github.io/Monocle/optics/lens.html Lens]] is an optic used to zoom inside a `Product`, e.g. `case class`, `Tuple`, `HList` or even `Map`.
 *
 * `Lenses` have two type parameters generally called `S` and `A`: `Lens[S, A]` where `S` represents the `Product` and `A` an element inside of `S`.
 *
 * Let’s take a simple case class with two fields:
 * {{{
 *   case class Address(strNumber: Int, streetName: String)
 * }}}
 *
 * We can create a `Lens[Address, Int]` which zooms from an `Address` to its field `strNumber` by supplying a pair of functions:
 *
 *  - `get: Address => Int`
 *  - `set: Int => Address => Address`
 *
 * {{{
 *   import monocle.Lens
 *   val strNumber = Lens[Address, Int](_.strNumber)(n => a => a.copy(strNumber = n))
 * }}}
 *
 * This case is really straightforward so we automated the generation of `Lenses` from case classes using a macro:
 * {{{
 *   import monocle.macros.GenLens
 *   val strNumber = GenLens[Address](_.strNumber)
 * }}}
 *
 * @param name lens
 */
object LensExercises extends AnyFlatSpec with Matchers with Section {

  import LensHelper._

  /**
   * Once we have a `Lens`, we can use the supplied get and set functions (nothing fancy!):
   * {{{
   *   val address = Address(10, "High Street")
   * }}}
   */
  def exerciseGetAndSet(res0: Int, res1: Address) = {

    streetNumber.get(address) should be(res0)
    streetNumber.set(5)(address) should be(res1)
  }

  /**
   * We can also `modify` the target of `Lens` with a function, this is equivalent to call `get` and then `set`:
   */
  def exerciseModify(res0: Address, res1: Int, res2: Address) = {
    streetNumber.modify(_ + 1)(address) should be(res0)
    val n = streetNumber.get(address)
    n should be(res1)
    streetNumber.set(n + 1)(address) should be(res2)
  }

  /**
   * We can push the idea even further, with `modifyF` we can update the target of a `Lens` in a context, cf `cats.Functor`:
   * {{{
   *   def neighbors(n: Int): List[Int] =
   *   if(n > 0) List(n - 1, n + 1) else List(n + 1)
   *
   *   import cats.implicits._ // to get Functor[List] instance
   * }}}
   */
  def exerciseModifyF(res0: List[Address], res1: List[Address]) = {
    import cats.implicits._ // to get Functor[List] instance
    streetNumber.modifyF(neighbors)(address) should be(res0)
    streetNumber.modifyF(neighbors)(Address(135, "High Street")) should be(res1)
  }

  // TODO find a way to work with Future
  /**
   * This would work with any kind of `Functor` and is especially useful in conjunction with asynchronous APIs, where one has the task to update a deeply nested structure with the result of an asynchronous computation:
   *
   * {{{
   * import scala.concurrent._
   * import scala.concurrent.ExecutionContext.Implicits._ // to get global ExecutionContext
   *
   * def updateNumber(n: Int): Future[Int] = Future.successful(n + 1)
   * }}}
   * {{{
   *   strNumber.modifyF(updateNumber)(address)
   *   // res9: scala.concurrent.Future[Address] = Future(<not completed>)
   * }}}
   *
   * Most importantly, `Lenses` compose together allowing to zoom deeper in a data structure:
   *
   * {{{
   *     case class Person(name: String, age: Int, address: Address)
   *     val john = Person("John", 20, address)
   *
   *     val addressLens = GenLens[Person](_.address)
   * }}}
   */
  def exerciseComposeLens(res0: Int, res1: Person) = {

    (addressLens composeLens streetNumber).get(john) should be(res0)
    (addressLens composeLens streetNumber).set(2)(john) should be(res1)
  }

  /**
   * = Other Ways of Lens Composition =
   *
   * Is possible to compose few `Lenses` together by using `compose`:
   */
  def exerciseComposeMacro(res0: Person) = {
    val compose = GenLens[Person](_.name).set("Mike") compose GenLens[Person](_.age).modify(_ + 1)

    compose(john) shouldBe res0
  }

  /**
   * Same but with the simplified macro based syntax:
   * {{{
   * import monocle.macros.syntax.lens._
   *
   * john.lens(_.name).set("Mike").lens(_.age).modify(_ + 1)
   * }}}
   *
   * (All `Setter` like optics offer `set` and `modify` methods that returns an `EndoFunction` (i.e. `S => S`) which means that we can compose modification using basic function composition.)
   *
   * Sometimes you need an easy way to update `Product` type inside `Sum` type - for that case you can compose `Prism` with `Lens` by using `some`:
   */
  def exerciseLensPrismComposition(res0: Option[Int]) = {
    import monocle.std.option.some
    import monocle.macros.GenLens

    case class B(c: Int)
    case class A(b: Option[B])

    val c = GenLens[B](_.c)
    val b = GenLens[A](_.b)

    (b composePrism some composeLens c).getOption(A(Some(B(1)))) shouldBe res0
  }

  /**
   * == Lens Generation ==
   *
   * `Lens` creation is rather boiler platy but we developed a few macros to generate them automatically. All macros are defined in a separate module (see [[http://julien-truffaut.github.io/Monocle/modules.html modules]]).
   * {{{
   *   import monocle.macros.GenLens
   *   val age = GenLens[Person](_.age)
   * }}}
   *
   * `GenLens` can also be used to generate `Lens` several level deep:
   */
  def exerciseLensGeneration(res0: Person) =
    GenLens[Person](_.address.streetName).set("Iffley Road")(john) should be(res0)

  /**
   * For those who want to push `Lenses` generation even further, we created `@Lenses` macro annotation which generate `Lenses` for all fields of a case class. The generated `Lenses` are in the companion object of the case class:
   *
   * {{{
   * import monocle.macros.Lenses
   *
   * @Lenses case class Point(x: Int, y: Int)
   * val p = Point(5, 3)
   * }}}
   */
  def exerciseLensMacroAnn(res0: Int, res1: Point) = {
    Point.x.get(p) shouldBe res0
    Point.y.set(0)(p) shouldBe res1
  }

  /**
   * You can also add a prefix to `@Lenses` in order to prefix the generated `Lenses`:
   * {{{
   * @Lenses("_") case class OtherPoint(x: Int, y: Int)
   * val op = OtherPoint(5, 3)
   * }}}
   */
  def exerciseLensMacroAnnPrefix(res0: Int) =
    OtherPoint._x.get(op) shouldBe res0

  /**
   * == Laws ==
   *
   * A `Lens` must satisfy all properties defined in `LensLaws` from the `core` module. You can check the validity of your own Lenses` using `LensTests` from the `law` module.
   *
   * In particular, a `Lens` must respect the `getSet` law which states that if you get a value `A` from `S` and `set` it back in, the result is an object identical to the original one. A side effect of this law is that `set` must only update the `A` it points to, for example it cannot increment a counter or modify another value.
   *
   * On the other hand, the `setGet` law states that if you `set` a `value`, you always `get` the same value back. This law guarantees that `set` is actually updating a value `A` inside of `S`.
   *
   */
  def exerciseLaws(res0: Boolean, res1: Boolean) = {
    val streetNumber = Lens[Address, Int](_.streetNumber)(n => a => a.copy(streetNumber = n))

    def getSet[S, A](l: Lens[S, A], s: S): Boolean =
      l.set(l.get(s))(s) == s

    def setGet[S, A](l: Lens[S, A], s: S, a: A): Boolean =
      l.get(l.set(a)(s)) == a

    getSet(streetNumber, address) should be(res0)

    setGet(streetNumber, address, 20) should be(res1)
  }
}
