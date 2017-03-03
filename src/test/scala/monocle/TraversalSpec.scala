package monocle


import monocle.TraversalHelper.Point
import org.scalacheck.Shapeless._
import org.scalaexercises.Test
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import shapeless.HNil


class TraversalSpec extends FunSuite with Checkers {

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
        Point("bottom-left",5,5) :: HNil
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

}