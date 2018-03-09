import org.scalatest.FunSuite

class SolutionTest extends FunSuite {

  val rng = scala.util.Random
  rng.setSeed(rng.nextLong)

  test("test neighbor()"){

    val initSolution1 = rng.shuffle(Parameters.instance1.toSeq)
    val initSolution2 = rng.shuffle(Parameters.instance2.toSeq)

    val solution1 = new Solution(initSolution1.toArray, rng)
    val solution2 = new Solution(initSolution2.toArray, rng)

    val neighbor1 = solution1.neighbor
    val neighbor2 = solution2.neighbor

    assert(solution1 !== neighbor1)
    assert(solution2 !== neighbor2)

    assert(solution1.cities !== neighbor1.cities)
    assert(solution2.cities !== neighbor2.cities)

    assert(solution1.cities.deep !== neighbor1.cities.deep)
    assert(solution2.cities.deep !== neighbor2.cities.deep)

    var c1 = 0
    var c2 = 0

    for(i <- 0 until solution1.cities.length) {
      if(solution1.cities(i) != neighbor1.cities(i)) c1 += 1
    }

    for(i <- 0 until solution2.cities.length) {
      if(solution2.cities(i) != neighbor2.cities(i)) c2 += 1
    }

    assert(c1 === 2)
    assert(c2 === 2)
  }

  test("test costFunction()") {
    assert(true)
  }
}
