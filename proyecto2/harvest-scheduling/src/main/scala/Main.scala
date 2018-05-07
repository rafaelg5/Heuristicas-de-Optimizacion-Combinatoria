
object Main extends App {

  val rng = scala.util.Random
  val p = Parameters
  val solution = p.initial_solution

  val schedule = new SchedulePlan(solution)
  val bestSolution = TabuSearch.run(3000, schedule)
}
