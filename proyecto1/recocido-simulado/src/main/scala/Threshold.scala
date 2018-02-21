
class Threshold(_batchSize: Int, initSolution: Solution) {

  private val epsilon = 0.03
  private var minSolution = initSolution
  private val batchSize = _batchSize

  // Cómo sabemos qué solución es mejor a otra
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

    while (i < batchSize){
      var newSolution = solution.neighbor
      if(newSolution.costFunction <= solution.costFunction + temp){
        solution = newSolution
        i += 1
        r += newSolution.costFunction
      }
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
    val coolingFactor = 0.5
    var temp = _temp

    while(temp > epsilon) {
      var tmpAvg = 0.0

      do {
        tmpAvg = avg
        var result = computeBatch(temp, minSolution)
        avg = result._1
        minSolution = result._2
      } while(avg <= tmpAvg)

      temp = coolingFactor * temp
    }
  }
}
