/*
 * Copyright 2017-2020 47 Degrees <https://47deg.com>
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

import org.scalacheck.ScalacheckShapeless._
import org.scalaexercises.Test
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import shapeless.HNil

class OptionalSpec extends AnyFunSuite with Checkers {

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
