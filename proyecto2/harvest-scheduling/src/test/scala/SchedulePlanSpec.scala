import org.scalatest._

class SchedulePlanSpec extends FlatSpec with Matchers {

  val rng = scala.util.Random
  var plan = new SchedulePlan(50,null,10,null)

  "Objective function" should "return a value > 0" in {
    assert(plan.cost > 0)
  }

  "Neighbor units" should "have an edge between unit and neighbor" in {
    val unit = rng.nextInt(plan.totalUnits)
    val neighborUnits = plan.getNeighborUnits(unit)
    for(neighbor <- neighborUnits) {
      assert(plan.adjacencyMatrix(unit)(neighbor) == 1)
    }
  }

  // unfinished
  "Unit" should "meet adjacency if no adjacent units between 2 years have been harvested " in {
    val unit = rng.nextInt(plan.totalUnits)
    val meetsAdjacency = plan.unitMeetsAdjacency(unit)
    val neighborUnits = plan.getNeighborUnits(unit)

    for(neighbor <- neighborUnits) {
      assert(plan.adjacencyMatrix(unit)(neighbor) == 1)
    }

    assert(true)
  }

  "Neighbors" should "not be null or empty" in {
    assert(plan.getNeighborPlans != null)
    assert(plan.getNeighborPlans.size > 0)
  }

  "Neighbors" should "only differ in one element" in {
    var counter = 0
    val table = plan.table
    for(neighbor <- plan.getNeighborPlans) {
      val nTable = neighbor.table
      for(i <- 0 until table.size) { if(table(i) != nTable(i)) counter += 1 }
    }
    assert(counter != 1)
  }
}
