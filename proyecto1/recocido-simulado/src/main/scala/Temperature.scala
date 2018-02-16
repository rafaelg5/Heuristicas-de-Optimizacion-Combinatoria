import scala.math.abs

/**
* @constructor crea una nueva temperatura inicial
* @param ti el valor inicial
* @param instanceSize el tamaño del ejemplar
*/
class Temperature(ti: Double, instanceSize: Int){

  private val epsilonT = 0.0
  private val epsilonP = 0.0

  /**
  * Devuelve el porcentaje de las soluciones aceptadas
  * @param _s una solución
  */
  def percentageOfAccepted(_s: Solution) : Double = {
    var counter = 0
    var s = _s
    for(i <- 0 until instanceSize){
      val newSolution = s.neighbor()
      if(newSolution.f() <= s.f() + ti){
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
  */
  def binarySearch(s: Solution, percentage: Double, temp1: Double, temp2: Double) : Double = {

    val middleTemp = (temp1 + temp2) / 2
    if(temp2 - temp1 < epsilonT) { return middleTemp }

    val newPercentage = percentageOfAccepted(s)
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
  * @param percentage el porcentaje de soluciones vecinas que se desea aceptar
  */
  def initTemp(s: Solution, percentage: Double) : Double = {

    return 0.0
  }

}
