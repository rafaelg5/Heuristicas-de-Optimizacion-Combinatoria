import org.scalatest.FunSuite

class SolutionTest extends FunSuite {
  test("test neighbor()"){

    val rng = scala.util.Random
    val sol = new Array[Int](150)

    for(i <- 0 until sol.length) { sol(i) = 1 + rng.nextInt(1093) }

    val initSolution = rng.shuffle(sol.toSeq)

    val solution = new Solution(initSolution.toArray, rng)
    val neighbor = solution.neighbor

    assert(solution !== neighbor)
    assert(solution.cities !== neighbor.cities)
    assert(solution.cities.deep !== neighbor.cities.deep)

    var c = 0
    for(i <- 0 until sol.length) { if(solution.cities(i) != neighbor.cities(i)) c += 1 }
    assert(c === 2)
  }

  test("test costFunction()") {
    
  }
}
