import scala.math.abs

/**
* @constructor crea una nueva temperatura inicial
* @param instanceSize el tamaño del ejemplar
* @param temp la temperatura inicial
*/
class Temperature(temp: Double, solution: Solution){

  private val epsilonT = Parameters.epsilonT
  private val epsilonP = Parameters.epsilonP
  private val value = temp
  private val s = solution

  /**
  * Devuelve el porcentaje de las soluciones aceptadas
  * @param _s una solución
  * @return el porcentaje
  */
  private def percentageOfAccepted(_s: Solution, t: Double) : Double = {

    var counter = 0.0
    var solution = s

    for(i <- 0 until 100){

      val newSolution = solution.neighbor

      if(newSolution.cost <= solution.cost + t){
        counter += 1.0
        solution = newSolution
      }
    }
    return counter / 100
  }

  /**
  * Búsqueda binaria para la temperatura inicial
  * @param s una solución
  * @param percentage el porcentaje de soluciones vecinas que se desea aceptar
  * @param temp1 el primer valor de la temperatura entre la que se va a buscar
  * @param temp2 el segundo valor de la temperatura entre la que se va a buscar
  * @return el resultado de la búsqueda
  */
  private def binarySearch(percentage: Double, temp1: Double, temp2: Double) : Double = {

    val middleTemp = (temp1 + temp2) / 2

    if(temp2 - temp1 < epsilonT) { return middleTemp }

    val newPercentage = percentageOfAccepted(s, middleTemp)
    if(abs(percentage - newPercentage) < epsilonP) { return middleTemp }

    if(newPercentage > percentage) {
      return binarySearch(percentage, temp1, middleTemp)
    } else {
      return binarySearch(percentage, middleTemp, temp2)
    }
  }

  /**
  * Encuentra una temperatura inicial para aumentar la probabilidad de que la
  * heurística de aceptación por umbrales pueda desplazarse rápidamente por el
  * espacio de búsqueda
  * @param s una solución
  * @param percentage el porcentaje de soluciones vecinas que se desea aceptar
  * @return la temperatura inicial
  */
  def initTemp(percentage: Double) : Double = {

    var t = value
    var newPercentage = percentageOfAccepted(s, t)

    if(abs(percentage - newPercentage) <= epsilonP){ return value }

    var temp1 = 0.0
    var temp2 = 0.0

    if(newPercentage < percentage) {

      while(newPercentage < percentage) {
        t *= 2
        newPercentage = percentageOfAccepted(s, t)
      }

      temp1 = t / 2
      temp2 = t
    } else {
      while(newPercentage > percentage) {
        t /= 2
        newPercentage = percentageOfAccepted(s, t)
      }
      temp1 = t
      temp2 = 2 * t
    }
    return binarySearch(percentage, temp1, temp2)
  }
}
