/**
* @constructor crea una nueva solución
* @param solution un arreglo de enteros que representa la solución
*/
class Solution(solution: Array[Int], _rng: scala.util.Random) {

  private var _cities = solution
  private val rng = _rng

  def cities = _cities

  def neighbor(): Solution = {
    return null
  }

  def f(): Double = {
    return 0
  }
}
