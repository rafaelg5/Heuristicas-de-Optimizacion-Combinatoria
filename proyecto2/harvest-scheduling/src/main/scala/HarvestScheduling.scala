import scala.util.Random;

class HarvestScheduling(initialSolution: Array[Array[ForestUnit]], s: Int) extends Runnable {

  private val plan = initialSolution
  private val seed = s
  private val rng = new Random(seed)

  def run() {
    val startTime = System.nanoTime
    val dec = Array.ofDim[Int](Parameters.Periods, Parameters.Units)

    /* Asignar las variables de decisión para que cumplan la restricción de
       singularidad */
    for(i <- 0 until Parameters.Units) {
      val pos = rng.nextInt(Parameters.Periods)
      val prob = rng.nextFloat
      if(prob < 0.5) dec(pos)(i) = 1
    }

    val schedule = new SchedulePlan(plan, dec)
    val objective = schedule.objective
    println(f"Inicial ($seed%d): $objective%.2f")

    val bestSolution = TabuSearch.run(Parameters.Iterations, schedule, seed)
    val bestObjective = bestSolution.objective

    println(f"Mejor solución ($seed%d): $bestObjective%.2f")
    val endTime = System.nanoTime
    val totalTime = (endTime - startTime) / 1000000000.0
    println(f"Tiempo total ($seed%d): $totalTime%.2f\n")
  }

}
