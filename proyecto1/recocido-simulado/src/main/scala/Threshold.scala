
class SimulatedAnnealing(initSolution: Solution) {

  private val epsilon = Parameters.epsilon
  private val coolingFactor = Parameters.coolingFactor
  private var _minSolution = initSolution
  private val batchSize = Parameters.batchSize

  def minSolution = _minSolution

  // Cómo sabemos qué solución es mejor a otra? costFunction?
  // solución mínima

  /**
  * Cálcula el promedio de soluciones aceptadas y la última solución dado un
  * número fijo de soluciones aceptadas llamado lote.
  * @param temp la temperatura inicial
  * @param s una solución
  * @return una tupla que contiene el promedio de soluciones y la última solución
  */
  private def computeBatch(temp: Double, s: Solution): (Double, Solution) = {

    var i = 0
    var r = 0.0
    var solution = s

    var exit = 0
    var squaredBatchSize = math.pow(batchSize, 2)

    while (i < batchSize && exit < squaredBatchSize){

      var newSolution = solution.neighbor

      if(newSolution.costFunction <= solution.costFunction + temp){
        solution = newSolution
        i += 1
        r += newSolution.costFunction
      }
      exit += 1
    }

    return (r / batchSize, solution)
  }

  /**
  * Heurística de aceptación por umbrales
  * @param temp la temperatura inicial
  * @param s una solución
  */
  def acceptByThresholds(_temp: Double): Unit = {

    var avg = 0.0
    var temp = _temp

    while(temp > epsilon) {

      print(f"$temp%f%n%n")
      var auxAvg = Double.MaxValue

      while(avg <= auxAvg) {
        println(f"p = $avg%f%nq = $auxAvg%f%n")
        auxAvg = avg
        var result = computeBatch(temp, _minSolution)
        avg = result._1
        _minSolution = result._2
      }
      temp = coolingFactor * temp
    }
  }
}
