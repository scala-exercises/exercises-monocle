package monocle

import monocle.IsoHelper.Person
import org.scalacheck.Shapeless._
import org.scalaexercises.Test
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import shapeless.HNil


class IsoSpec extends FunSuite with Checkers {

  test("exercise person to tuple") {
    check(
      Test.testSuccess(
        IsoExercises.exercisePersonToTuple _,
        ("Zoe", 25) :: Person("Zoe", 25) :: HNil
      )
    )
  }

  test("exercise person to tuple apply") {
    check(
      Test.testSuccess(
        IsoExercises.exercisePersonToTupleApply _,
        Person("Zoe", 25) :: HNil
      )
    )
  }

  test("exercise list to vector") {
    check(
      Test.testSuccess(
        IsoExercises.exerciseListToVector _,
        Vector(1, 2, 3) :: HNil
      )
    )
  }

  test("exercise string to list") {
    check(
      Test.testSuccess(
        IsoExercises.exerciseStringToList _,
        "ello" :: HNil
      )
    )
  }

  test("exercise gen iso apply") {
    check(
      Test.testSuccess(
        IsoExercises.exerciseGenIsoApply _,
        "Hello" :: HNil
      )
    )
  }

  test("exercise gen iso fields") {
    check(
      Test.testSuccess(
        IsoExercises.exerciseGenIsoFields _,
        ("John", 42) :: HNil
      )
    )
  }

  test("exercise laws") {
    check(
     Test.testSuccess(
       IsoExercises.exerciseLaws _,
       true :: true:: HNil
     )
   )
  }
}