object Main extends App {

  val rng = scala.util.Random
  rng.setSeed(Parameters.seed)

  val initSolution = rng.shuffle(Parameters.instance1.toSeq)
  var solution = new Solution(initSolution.toArray)

  val t = new Temperature(Parameters.instance1.length)
  var realInitTemp = t.initTemp(solution, Parameters.initTemp, Parameters.percentage)

  val sa = new SimulatedAnnealing(solution)
  sa.acceptByThresholds(realInitTemp)
  /*var factSol =  th.minSolution

  for(city <- factSol.cities) {
    println(city)
  }*/

}
