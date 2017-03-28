package monocle

import monocle.law.OptionalLaws
import org.scalaexercises.definitions._
import org.scalatest._


object OptionalHelper {


  val head = Optional[List[Int], Int] {
    case Nil => None
    case x :: xs => Some(x)
  }{ a => {
    case Nil => Nil
    case x :: xs => a :: xs
    }
  }

  val xs = List(1, 2, 3)
  val ys = List.empty[Int]

}

/** == Optional ==
  *
  * An [[http://julien-truffaut.github.io/Monocle/optics/optional.html `Optional`]]  is an Optic used to zoom inside a `Product`, e.g. `case class`, `Tuple`, `HList` or even Map. Unlike the `Lens`, the element that the `Optional` focuses on may not exist.
  *
  * `Optionals` have two type parameters generally called `S` and `A`: `Optional[S, A]` where `S` represents the `Product` and `A` an optional element inside of `S`.
  *
  * Let’s take a simple list with integers.
  *
  * We can create an `Optional[List[Int], Int]` which zooms from a `List[Int]` to its potential head by supplying a pair of functions:
  *
  *  -  `getOption: List[Int] => Option[Int]`
  *  -  `set: Int => List[Int] => List[Int]`
  *
  * {{{
  *   import monocle.Optional
  *
  *   val head = Optional[List[Int], Int] {
  *     case Nil => None
  *     case x :: xs => Some(x)
  *   }{ a => {
  *     case Nil => Nil
  *     case x :: xs => a :: xs
  *     }
  *  }
  * }}}
  *
  * @param name optional
  */
object OptionalExercises extends FlatSpec with Matchers with Section {

  import OptionalHelper._

  /**
    * Once we have an `Optional`, we can use the supplied `nonEmpty` function to know if it matches:
    *
    * {{{
    *   val xs = List(1, 2, 3)
    *   val ys = List.empty[Int]
    * }}}
    */
  def exerciseNonEmpty(res0: Boolean , res1: Boolean) = {

    head.nonEmpty(xs) should be(res0)
    head.nonEmpty(ys) should be(res1)
  }

  /**
    * We can use the supplied `getOrModify` function to retrieve the target if it matches, or the original value:
    *
    * {{{
    *   head.getOrModify(xs)
    *     // res2: scalaz.\/[List[Int],Int] = \/-(1)
    *
    *   head.nonEmpty(ys)
    *     // res3: scalaz.\/[List[Int],Int] = -\/(List())
    * }}}
    *
    * The function `getOrModify` is mostly used for polymorphic optics. If you use monomorphic optics, use function `getOption`
    *
    * We can use the supplied `getOption` and set functions:
    */
  def exerciseGetOption(res0: Option[Int], res1: List[Int], res2: Option[Int], res3: List[Int]) = {

    head.getOption(xs) should be(res0)
    head.set(5)(xs) should be(res1)
    head.getOption(ys) should be(res2)
    head.set(5)(ys) should be(res3)
  }

  /**
    * We can also modify the target of `Optional` with a function:
    */
  def exerciseModify(res0: List[Int], res1: List[Int]) = {

    head.modify(_ + 1)(xs) should be(res0)
    head.modify(_ + 1)(ys) should be(res1)
  }

  /**
    * Or use `modifyOption` / `setOption` to know if the update was successful:
    */
  def exerciseModifyOption(res0: Option[List[Int]], res1: Option[List[Int]]) = {

    head.modifyOption(_ + 1)(xs) should be(res0)
    head.modifyOption(_ + 1)(ys) should be(res1)
  }

  /**
    * == Laws ==
    *
    * An `Optional` must satisfy all properties defined in `OptionalLaws` in core module. You can check the validity of your own `Optional` using `OptionalTests` in law module.
    *
    * `getOptionSet` states that if you `getOrModify` a value `A` from `S` and then `set` it back in, the result is an object identical to the original one.
    *
    * `setGetOption` states that if you `set` a value, you always `getOption` the same value back.
    */
    def exerciseLaws(res0 : Boolean, res1 : Boolean) =
    {
      val head = Optional[List[Int], Int] {
          case Nil => None
          case x :: xs => Some(x)
        }
        {
          a => {
            case Nil => Nil
            case x :: xs => a :: xs
          }
        }
      class OptionalLaws[S, A](optional: Optional[S, A]) {

         def getOptionSet(s: S): Boolean =
           optional.getOrModify(s).fold(identity, optional.set(_)(s)) == s

         def setGetOption(s: S, a: A): Boolean =
           optional.getOption(optional.set(a)(s)) == optional.getOption(s).map(_ => a)

      }
      new OptionalLaws(head).getOptionSet(List(1,2,3)) should be (res0)

      new OptionalLaws(head).setGetOption(List(1,2,3),20) should be (res1)

    }



}
