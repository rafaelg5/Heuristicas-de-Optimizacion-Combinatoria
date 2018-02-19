
class Threshold(batchSize: Int) {

  val epsilon = 0.03

  /**
  * Cálcula el promedio de soluciones aceptadas y la última solución dado un
  * número fijo de soluciones aceptadas llamado lote.
  * @param temp la temperatura inicial
  * @param s una solución
  */
  def computeBatch(temp: Double, _s: Solution): (Double, Solution) = {

    var i = 0
    var r = 0.0
    var s = _s

    while (i < batchSize){
      var newSolution = s.neighbor()
      if(newSolution.f() <= s.f() + temp){
        s = newSolution
        i += 1
        r += newSolution.f()
      }
    }

    return (r / batchSize, s)
  }

  /**
  * Heurística de aceptación por umbrales
  * @param temp la temperatura inicial
  * @param s una solución
  */
  def acceptByThresholds(_temp: Double, _s: Solution): Unit = {

    var avg = 0.0
    val coolingFactor = 0.5
    var s = _s
    var temp = _temp

    while(temp > epsilon) {
      var tmpAvg = 0.0

      do {
        tmpAvg = avg
        var result = computeBatch(temp, s)
        avg = result._1
        s = result._2
      } while(avg <= tmpAvg)
      
      temp = coolingFactor * temp
    }
  }
}
