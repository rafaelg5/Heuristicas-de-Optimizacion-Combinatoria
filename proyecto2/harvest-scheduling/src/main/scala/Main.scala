
object Main extends App {

  val rng = scala.util.Random
  val p = Parameters

  val units = p.units
  val periods = p.periods
  val lowerBound = p.lowerBound
  val upperBound = p.upperBound

  val adjacencies = p.adjacencies
  val solution = Array.ofDim[Double](periods,units)

  //
  /*for (i <- 0 until periods; j <- 0 until units) {
    solution(i)(j) = round(rng.nextDouble).toInt
  }*/

  val schedule = new SchedulePlan(units, adjacencies, periods, solution)
  val bestSolution = TabuSearch.run(3000, schedule)
}
