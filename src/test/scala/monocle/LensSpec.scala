/*
 *  scala-exercises - exercises-monocle
 *  Copyright (C) 2015-2019 47 Degrees, LLC. <http://www.47deg.com>
 *
 */

package monoclelib

import LensHelper.{Address, Person, Point}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.ScalacheckShapeless._
import org.scalaexercises.Test
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import shapeless.HNil

class LensSpec extends AnyFunSuite with Checkers {

  implicit val addressArbitrary = Arbitrary {
    for {
      streetName   <- Gen.identifier
      streetNumber <- Gen.posNum[Int]
    } yield Address(streetNumber, streetName)
  }

  implicit val personArbitrary = Arbitrary {
    for {
      name    <- Gen.identifier
      age     <- Gen.posNum[Int]
      address <- addressArbitrary.arbitrary
    } yield Person(name, age, address)
  }

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
        Address(11, "High Street") :: 10 :: Address(11, "High Street") :: HNil
      )
    )
  }

  test("exercise modifyF") {
    check(
      Test.testSuccess(
        LensExercises.exerciseModifyF _,
        List(Address(9, "High Street"), Address(11, "High Street")) :: List(
          Address(134, "High Street"),
          Address(136, "High Street")
        ) :: HNil
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

  test("exercise compose macro") {
    check(
      Test.testSuccess(
        LensExercises.exerciseComposeMacro _,
        Person("Mike", 21, LensHelper.address) :: HNil
      )
    )
  }

  test("exercise clens prism composition") {
    check(Test.testSuccess(LensExercises.exerciseLensPrismComposition _, Option(1) :: HNil))
  }

  test("exercise lens generation") {
    check(
      Test.testSuccess(
        LensExercises.exerciseLensGeneration _,
        Person("John", 20, Address(10, "Iffley Road")) :: HNil
      )
    )
  }

  test("exercise lens macro annotation") {
    check(Test.testSuccess(LensExercises.exerciseLensMacroAnn _, 5 :: Point(5, 0) :: HNil))
  }

  test("exercise lens macro annotation prefix") {
    check(Test.testSuccess(LensExercises.exerciseLensMacroAnnPrefix _, 5 :: HNil))
  }

  test("exercise laws") {
    check(
      Test.testSuccess(
        LensExercises.exerciseLaws _,
        true :: true :: HNil
      )
    )
  }
}
