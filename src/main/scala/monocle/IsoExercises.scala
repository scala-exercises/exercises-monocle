package monocle

import monocle.macros.GenIso
import org.scalatest._
import org.scalaexercises.definitions._


object IsoHelper {
  case class Person(name: String, age: Int)

  val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)){case (name, age) => Person(name, age)}

  def listToVector[A] = Iso[List[A], Vector[A]](_.toVector)(_.toList)

  def vectorToList[A] = listToVector[A].reverse

  val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))

  case class MyString(s: String)
  case class Foo()
  case object Bar

}

/** == Iso ==
  *
  * An [[http://julien-truffaut.github.io/Monocle/optics/iso.html `Iso`]] is an optic which converts elements of type `S` into elements of type `A` without loss.
  *
  * Consider a case class Person with two fields:
  *
  * {{{
  *   case class Person(name: String, age: Int)
  * }}}
  *
  * @param name iso
  */
object IsoExercises extends FlatSpec with Matchers with Section {

  import IsoHelper._

  /**
    * `Person` is equivalent to a tuple `(String, Int)` and a tuple `(String, Int)` is equivalent to `Person`. So we can create an `Iso` between `Person` and `(String, Int)` using two total functions:
    *
    *  - `get: Person => (String, Int)`
    *  - `reverseGet (aka apply): (String, Int) => Person`
    *
    * {{{
    *   import monocle.Iso
    *   val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)){case (name, age) => Person(name, age)}
    * }}}
    */
  def exercisePersonToTuple(res0: (String, Int) , res1: Person) = {

    personToTuple.get(Person("Zoe", 25)) should be(res0)
    personToTuple.reverseGet(("Zoe", 25)) should be(res1)
  }

  /**
    * Or simply:
    */
  def exercisePersonToTupleApply(res0: Person) = {

    personToTuple(("Zoe", 25)) should be(res0)

  }

  /**
    * Another common use of `Iso` is between collection. `List` and `Vector` represent the same concept, they are both an ordered sequence of elements but they have different performance characteristics. Therefore, we can define an `Iso` between a `List[A]` and a `Vector[A]`:
    * {{{
    *   def listToVector[A] = Iso[List[A], Vector[A]](_.toVector)(_.toList)
    * }}}
    */
  def exerciseListToVector(res0: Vector[Int]) = {

    listToVector.get(List(1,2,3)) should be(res0)

  }

  /**
    * We can also `reverse` an `Iso` since it defines a symmetric transformation:
    * {{{
    *   def vectorToList[A] = listToVector[A].reverse
    *     // vectorToList: [A]=> monocle.PIso[Vector[A],Vector[A],List[A],List[A]]
    * }}}
    */
  def exerciseVectorToList(res0: List[Int]) = {

    vectorToList.get(Vector(1,2,3)) should be(res0)

  }

  /**
    * `Iso` are also convenient to lift methods from one type to another, for example a `String` can be seen as a `List[Char]` so we should be able to transform all functions `List[Char] => List[Char]` into `String => String`:
    * {{{
    *   val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))
    * }}}
    */
  def exerciseStringToList(res0: String) = {

    stringToList.modify(_.tail)("Hello") should be(res0)

  }

  /**
    * We defined several macros to simplify the generation of Iso between a case class and its Tuple equivalent. All macros are defined in a separate module (see modules).
    * {{{
    *     case class MyString(s: String)
    *     case class Foo()
    *     case object Bar
    *
    *     import monocle.macros.GenIso
    * }}}
    *
    * First of all, `GenIso.apply` generates an `Iso` for `newtype` i.e. case class with a single type parameter:
    */
  def exerciseGenIsoApply(res0: String) = {

    GenIso[MyString, String].get(MyString("Hello")) should be(res0)

  }

  /**
    * Then, `GenIso.unit` generates an `Iso` for object or case classes with no field:
    *
    * {{{
    * GenIso.unit[Foo]
    * // res8: monocle.Iso[Foo,Unit] = monocle.PIso$$anon$10@280a5b3b
    * GenIso.unit[Bar.type]
    * // res9: monocle.Iso[Bar.type,Unit] = monocle.PIso$$anon$10@5520ac34
    * }}}
    *
    * Finally, `GenIso.fields` is a whitebox macro which generalise `GenIso.apply` to all case classes:
    */
  def exerciseGenIsoFields(res0: (String, Int)) = {

    GenIso.fields[Person].get(Person("John", 42)) should be(res0)

  }

  /**
    * Be aware that whitebox macros are not supported by all IDEs.
    *
    * == Laws ==
    *
    * An `Iso` must satisfy all properties defined in `IsoLaws` from the core module. You can check the validity of your own `Iso` using `IsoTests` from the law module.
    *
    * In particular, an Iso must verify that `get` and `reverseGet` are inverse. This is done via `roundTripOneWay` and `roundTripOtherWay` laws:
    *
    */

  def exerciseLaws(res0 : Boolean, res1 : Boolean) =
  {
	  personToTuple.get(Person("Zoe", 25))
	  def roundTripOneWay[S, A](i: Iso[S, A], s: S): Boolean =
		  i.reverseGet(i.get(s)) == s

	  def roundTripOtherWay[S, A](i: Iso[S, A], a: A): Boolean =
		  i.get(i.reverseGet(a)) == a

	  roundTripOneWay(personToTuple, Person("Zoey",25)) should be (res0)

	  roundTripOtherWay(personToTuple,("Zoe",52)) should be (res1)
  }


}
