
object Main {

  val rng = scala.util.Random
  val p = Parameters

  val schedule = new SchedulePlan(p.initialSolution)

  val lol = new SchedulePlan(schedule.table)
  println(schedule.objective)
  schedule.neighborhood
  println(schedule.objective)
  //val bestSolution = TabuSearch.run(1, schedule)

}
