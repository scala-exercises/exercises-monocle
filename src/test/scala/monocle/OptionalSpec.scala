/*
 * scala-exercises - exercises-monocle
 * Copyright (C) 2015-2016 47 Degrees, LLC. <http://www.47deg.com>
 */

package monocle

import org.scalacheck.Shapeless._
import org.scalaexercises.Test
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import shapeless.HNil

import scalaz.{-\/, \/-}

class OptionalSpec extends FunSuite with Checkers {

  test("exercise non empty") {
    check(
      Test.testSuccess(
        OptionalExercises.exerciseNonEmpty _,
        true :: false :: HNil
      )
    )
  }

  test("exercise getOption") {
    check(
      Test.testSuccess(
        OptionalExercises.exerciseGetOption _,
        Option(1) :: List(5, 2, 3) :: Option.empty[Int] :: List.empty[Int] :: HNil
      )
    )
  }

  test("exercise modify") {
    check(
      Test.testSuccess(
        OptionalExercises.exerciseModify _,
        List(2, 2, 3) :: List.empty[Int] :: HNil
      )
    )
  }

  test("exercise modifyOption") {
    check(
      Test.testSuccess(
        OptionalExercises.exerciseModifyOption _,
        Option(List(2, 2, 3)) :: Option.empty[List[Int]] :: HNil
      )
    )
  }

  test("exercise laws") {
    check(
      Test.testSuccess(
        OptionalExercises.exerciseLaws _,
        true :: true :: HNil
      )
    )
  }
}
