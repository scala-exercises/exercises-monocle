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

import PrismHelper.{JNum, JStr}
import org.scalaexercises.Test
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers

import org.scalacheck.ScalacheckShapeless._
import shapeless.HNil

class PrismSpec extends AnyFunSuite with Checkers {

  test("exercise get option apply") {
    check(
      Test.testSuccess(
        PrismExercises.exerciseGetOptionAndApply _,
        JStr("hello") :: Option("Hello") :: Option.empty[String] :: HNil
      )
    )
  }

  test("exercise set and modify") {
    check(
      Test.testSuccess(
        PrismExercises.exerciseSetAndModify _,
        JStr("Bar") :: JStr("olleH") :: HNil
      )
    )
  }

  test("exercise set and modify 2") {
    check(
      Test.testSuccess(
        PrismExercises.exerciseSetAndModify2 _,
        JNum(10) :: JNum(10) :: HNil
      )
    )
  }

  test("exercise modifyOption") {
    check(
      Test.testSuccess(
        PrismExercises.exerciseModifyOption _,
        Option(JStr("olleH")) :: Option.empty[JNum] :: HNil
      )
    )
  }

  test("exercise compose") {
    check(
      Test.testSuccess(
        PrismExercises.exerciseCompose _,
        JNum(5.0) :: Option(5) :: Option.empty[Int] :: Option.empty[String] :: HNil
      )
    )
  }

  test("exercise prism generation") {
    check(
      Test.testSuccess(
        PrismExercises.exercisePrismGeneration _,
        Option(JNum(4.5)) :: Option.empty[JNum] :: HNil
      )
    )
  }

  test("exercise laws") {
    check(
      Test.testSuccess(
        PrismExercises.exerciseLaws _,
        true :: true :: HNil
      )
    )
  }
}
