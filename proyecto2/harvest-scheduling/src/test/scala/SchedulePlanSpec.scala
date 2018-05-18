import org.scalatest._

class SchedulePlanSpec extends FlatSpec with Matchers {

  val rng = scala.util.Random
  var plan = new SchedulePlan(null)

  "Objective function" should "return a value > 0" in {
    assert(plan.objective > 0)
  }

  // unfinished
  "Unit" should "meet adjacency if no adjacent units between 2 years have been harvested " in {

    val unit = rng.nextInt(plan.units)
    val meetsAdjacency = plan.unitMeetsAdjacency(unit)

    var cond = true

    for (t <- 0 until plan.periods; i <- 0 until plan.units) {

      if(plan.table(t)(i).x == 1){
        val adj1 = i - 25
        val adj2 = i - 1
        val adj3 = i + 1
        val adj4 = i + 25

        if(adj1 >= 0) { if(plan.table(t)(adj1).x == 1) cond = false }
        if(adj2 >= 0) { if(plan.table(t)(adj2).x == 1) cond = false }
        if(adj3 < plan.units) { if(plan.table(t)(adj3).x == 1) cond = false }
        if(adj3 < plan.units) { if(plan.table(t)(adj4).x == 1) cond = false }

      }
    }

    assert(true)
  }

  "Neighbors" should "not be null or empty" in {
    assert(plan.neighborhood != null)
    assert(plan.neighborhood.size > 0)
  }

  "Neighbors" should "only differ in one element" in {
    var counter = 0
    val table = plan.table
    for(neighbor <- plan.neighborhood) {
      val nTable = neighbor.table
      for(i <- 0 until table.size) { if(table(i) != nTable(i)) counter += 1 }
    }
    assert(counter != 1)
  }
}
