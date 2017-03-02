package monocleex

import monocle.{PrismExercises, IsoExercises}
import org.scalaexercises.definitions._

/** Monocle is an optics library for Scala (and Scala.js) strongly inspired by Haskell Lens.
  *
  * @param name monocle
  */
object MonocleLib extends Library {
  override def owner = "scala-exercises"

  override def repository = "exercises-monocle"

  override def color = Some("#5B5988")

  override def sections = List(
    IsoExercises,
    LensExercises,
    PrismExercises
  )
  override def logoPath = "monocle"
}
