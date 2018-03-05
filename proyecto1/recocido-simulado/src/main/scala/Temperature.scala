import scala.math.abs

/**
* @constructor crea una nueva temperatura inicial
* @param instanceSize el tamaño del ejemplar
*/
class Temperature(instanceSize: Int){

  private val epsilonT = Parameters.epsilonT
  private val epsilonP = Parameters.epsilonP

  /**
  * Devuelve el porcentaje de las soluciones aceptadas
  * @param _s una solución
  * @return el porcentaje
  */
  private def percentageOfAccepted(_s: Solution, ti: Double) : Double = {

    var counter = 0.0
    var s = _s

    for(i <- 0 until instanceSize){
      val newSolution = s.neighbor
      if(newSolution.cost <= s.cost + ti){
        counter += 1
        s = newSolution
      }
    }

    return counter / instanceSize
  }

  /**
  * Búsqueda binaria para la temperatura inicial
  * @param s una solución
  * @param percentage el porcentaje de soluciones vecinas que se desea aceptar
  * @param temp1 el primer valor de la temperatura entre la que se va a buscar
  * @param temp2 el segundo valor de la temperatura entre la que se va a buscar
  * @return el resultado de la búsqueda
  */
  private def binarySearch(s: Solution, percentage: Double, temp1: Double, temp2: Double) : Double = {

    val middleTemp = (temp1 + temp2) / 2

    if(temp2 - temp1 < epsilonT) { return middleTemp }

    val newPercentage = percentageOfAccepted(s, middleTemp)
    if(abs(percentage - newPercentage) < epsilonP) { return middleTemp }

    if(newPercentage > percentage) {
      return binarySearch(s, percentage, temp1, middleTemp)
    } else {
      return binarySearch(s, percentage, middleTemp, temp2)
    }
  }

  /**
  * Encuentra una temperatura inicial para aumentar la probabilidad de que la
  * heurística de aceptación por umbrales pueda desplazarse rápidamente por el
  * espacio de búsqueda
  * @param s una solución
  * @param ti la temperatura inicial ("aleatoria")
  * @param percentage el porcentaje de soluciones vecinas que se desea aceptar
  * @return la temperatura inicial
  */
  def initTemp(s: Solution, _ti: Double, percentage: Double) : Double = {

    var ti = _ti
    var newPercentage = percentageOfAccepted(s, ti)

    if(abs(percentage - newPercentage) <= epsilonP){ return ti }

    var temp1 : Double = 0.0
    var temp2 : Double = 0.0

    if(newPercentage < percentage) {
      while(newPercentage < percentage) {
        ti = 2 * ti
        newPercentage = percentageOfAccepted(s, ti)
      }
      temp1 = ti / 2
      temp2 = ti
    } else {
      while(newPercentage > percentage) {
        ti = ti / 2
        newPercentage = percentageOfAccepted(s, ti)
      }
      temp1 = ti
      temp2 = 2 * ti
    }

    return binarySearch(s, percentage, temp1, temp2)
  }
}
