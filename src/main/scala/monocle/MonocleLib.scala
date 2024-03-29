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

import org.scalaexercises.definitions._

/**
 * Monocle is an optics library for Scala (and Scala.js) strongly inspired by Haskell Lens.
 *
 * @param name
 *   monocle
 */
object MonocleLib extends Library {
  override def owner = "scala-exercises"

  override def repository = "exercises-monocle"

  override def color = Some("#5B5988")

  override def sections =
    List(
      IsoExercises,
      LensExercises,
      PrismExercises,
      OptionalExercises,
      TraversalExercises
    )
  override def logoPath = "monocle"
}
