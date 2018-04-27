import scala.util.Random

/**
* @constructor crea un nuevo plan o programa para el aprovechamiento de recursos
* forestales
*/
class SchedulePlan(units: Int, unitsAdjacency: Array[Array[Int]], periods: Int, solution: Array[Array[Double]]) {

  /* Volumen (m^3) por hectárea por edad de los árboles
  * Las edades van de 15 años a 40
  * índice 0 = 15, índice 1 = 16, ..., índice 26 = 40
  */
  val timberVolume = Parameters.timberVolume

  /* Devuelve el volumen por hectárea dada la edad */
  private def getTimberVolume(age: Int) = timberVolume(age - 15)

  /* params de la unidad: area, ingreso neta, edad promedio de áboles,
  * volumen de la tala, costo de talar una unidad i en el periodo t
  */

  def unitMeetsAdjacency(unit: Int):Boolean = true

  def getNeighbors:Array[SchedulePlan] = null

  def cost: Double = 0.0

}
