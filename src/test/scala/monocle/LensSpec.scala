package monocle

import monocleex.LensExercises
import monocleex.LensHelper.{Person, Address}
import org.scalacheck.Shapeless._
import org.scalaexercises.Test
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import shapeless.HNil


class LensSpec extends FunSuite with Checkers {

  test("exercise get and set") {
    check(
      Test.testSuccess(
        LensExercises.exerciseGetAndSet _,
        10 :: Address(5, "High Street") :: HNil
      )
    )
  }

  test("exercise modify") {
    check(
      Test.testSuccess(
        LensExercises.exerciseModify _,
        Address(11,"High Street") :: 10 :: Address(11, "High Street") :: HNil
      )
    )
  }

  test("exercise modifyF") {
    check(
      Test.testSuccess(
        LensExercises.exerciseModifyF _,
        List(Address(9,"High Street"), Address(11,"High Street")) :: List(Address(134,"High Street"), Address(136,"High Street")) :: HNil
      )
    )
  }

  test("exercise compose lens") {
    check(
      Test.testSuccess(
        LensExercises.exerciseComposeLens _,
        10 :: Person("John", 20, Address(2, "High Street")) :: HNil
      )
    )
  }

  test("exercise lens generation") {
    check(
      Test.testSuccess(
        LensExercises.exerciseLensGeneration _,
        Person("John", 20, Address(10, "Iffley Road")) :: HNil
      )
    )
  }

}