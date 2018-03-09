
class SimulatedAnnealing(initSolution: Solution) {

  private val epsilon = Parameters.epsilon
  private val coolingFactor = Parameters.coolingFactor
  private var _minSolution = initSolution
  private val batchSize = Parameters.batchSize

  def minSolution = _minSolution

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
    val m = (scala.math.log(batchSize) / scala.math.log(2)).toInt

    while (i < batchSize && exit < m){

      var newSolution = solution.neighbor
      if(newSolution.cost <= solution.cost + temp){
        solution = newSolution
        i += 1
        r += newSolution.cost
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
    var newSolution = _minSolution

    while(temp > epsilon) {

      var auxAvg = Double.MaxValue

      while(avg <= auxAvg) {
        auxAvg = avg
        var result = computeBatch(temp, newSolution)
        avg = result._1
        newSolution = result._2
        if(newSolution.cost < _minSolution.cost) _minSolution = newSolution
      }
      temp = coolingFactor * temp
    }
  }
}
