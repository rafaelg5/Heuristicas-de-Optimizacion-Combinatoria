object Main extends App {

  val rng = scala.util.Random
  var lastSol: Solution = _
  for(i <- 1 to 10) {

    val thread = new Thread {
      rng.setSeed(i)

      val initSolution = rng.shuffle(Parameters.instance1.toSeq)
      var solution = new Solution(initSolution.toArray)

      val t = new Temperature(Parameters.instance1.length)
      var realInitTemp = t.initTemp(solution, Parameters.initTemp, Parameters.percentage)

      val sa = new SimulatedAnnealing(solution)
      sa.acceptByThresholds(realInitTemp)
      lastSol = sa.minSolution

      for(city <- lastSol.cities) { print(city + " ") }
    }
    thread.start
    Thread.sleep(50)
  }
}
