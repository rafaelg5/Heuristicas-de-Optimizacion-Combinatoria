import scala.util.Random

/**
* @constructor crea un nuevo plan o programa para el aprovechamiento de recursos
* forestales
*/
class SchedulePlan(solution: Array[Array[Cell]]) {

  /*
  * Función objetivo:
  * Max ΣΣ (Rev_it - Lc_it) x X_it x V_it
  * donde
  * i = unidad
  * t = periodo
  * Rev_it = ingreso por metro cúbico para la unidad i talada en el periodo t
  * Lc_it = costo de tala por metro cúbico para la unidad i talada en el periodo t
  * V_it = volumen por hectarea para la unidad i talada en el periodo t
  * X_it = variable de decisión (0,1) donde la unidad i es talada en el periodo t
  *
  */

  /* Restricciones:
  *
  * 1. Restricción de singularidad
  * Σ X_it <= 1 for i = 1, ..., units
  *
  * 2. Restricciones de adyacencia
  * Σ from t-2 to t+2 X_(i-1)t + Σ from t-2 to t+2 X_(i-25)t +
  * 4 x X_it x Σ from t-2 to t+2 X_(i-25)t + Σ from t-2 to t+2 X_(i-1)t <= 4
  *
  */


  /* Volumen (m^3) por hectárea por edad de los árboles
  * Las edades van de 15 años a 40
  * índice 0 = 15, índice 1 = 16, ..., índice 26 = 40
  */
  val timberVolume = Parameters.timberVolume

  private var _table = solution

  def table = _table

  def units = solution(0).length

  def periods = solution.length

  /* Devuelve el volumen por hectárea dada la edad */
  private def getTimberVolume(age: Int) = timberVolume(age - 15)

  def unitMeetsAdjacency(unit: Int):Boolean = true

  def neighborhood:Array[SchedulePlan] = null

  def cost: Double = 0.0

}
