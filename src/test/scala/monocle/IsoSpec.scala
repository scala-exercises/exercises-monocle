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

import IsoHelper.Person
import org.scalacheck.ScalacheckShapeless._
import org.scalaexercises.Test
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import shapeless.HNil

class IsoSpec extends AnyFunSuite with Checkers {

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
        true :: true :: HNil
      )
    )
  }
}
