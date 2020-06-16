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

import TraversalHelper.Point
import org.scalacheck.ScalacheckShapeless._
import org.scalaexercises.Test
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import shapeless.HNil

class TraversalSpec extends AnyFunSuite with Checkers {

  test("exercise traversal") {
    check(
      Test.testSuccess(
        TraversalExercises.exerciseTraversal _,
        List(0, 0, 0, 0, 0) :: List(2, 3, 4, 5, 6) :: HNil
      )
    )
  }

  test("exercise fold") {
    check(
      Test.testSuccess(
        TraversalExercises.exerciseFold _,
        List(1, 2, 3, 4, 5) :: Option(1) :: Option(4) :: false :: HNil
      )
    )
  }

  test("exercise smart construct") {
    check(
      Test.testSuccess(
        TraversalExercises.exerciseSmartConstruct _,
        Point("bottom-left", 5, 5) :: HNil
      )
    )
  }

  test("exercise modifyF") {
    check(
      Test.testSuccess(
        TraversalExercises.exerciseModifyF _,
        Map(1 -> "one", 2 -> "TWO", 3 -> "three", 4 -> "FOUR") :: HNil
      )
    )
  }

  test("exercise laws") {
    check(
      Test.testSuccess(
        TraversalExercises.exerciseLaws _,
        true :: true :: HNil
      )
    )
  }
}
