/**
* @constructor crea una nueva solución
* @param solution un arreglo de enteros que representa la solución
*/
class Solution(solution: Array[Int]) {

  private val _cities = solution
  private val _rng = scala.util.Random
  private var distanceSum: Double = _

  private val connectedPairs = {
    val buf = scala.collection.mutable.ArrayBuffer.empty[Edge]

    for(i <- 1 until _cities.length) {
      val e = new Edge(_cities(i - 1), _cities(i))
      if(e.exists) {
        buf += e
        distanceSum += e.distance
      }
    }
    buf.toArray
  }

  // Getters
  def cities = _cities

  /**
  * Obtiene una solución vecina de manera aleatoria. La solución vecina solo
  * cambia en una arista de la solución.
  * @return la solución vecina
  */
  def neighbor: Solution = {

    val num = _rng.nextInt(_cities.length)
    val num2 = if (num + 1 == cities.length) num - 1 else if (num == 0) 1 else num + 1

    var neighborCities = _cities.clone
    swap(num, num2, neighborCities)

    return new Solution(neighborCities)
  }

  /*
  * Devuelve el promedio de pesos de los pares conectados
  */

  // si no hay pares? |Es| = 0?
  private def weightAvg = {
    if (connectedPairs.length == 0) 0 else distanceSum / connectedPairs.length
  }

  /*
  * Devuelve la distancia máxima que existe en la solución
  */
  private def maxDistance = {
    var max = 0.0
    for(connection <- connectedPairs) {
      if(connection.distance > max) max = connection.distance
    }
    max
  }

  /*
  * Devuelve un valor de castigo para definir la función de peso aumentada
  */
  private def penalty = {
    var factor = 3.0
    factor * maxDistance
  }

  /*
  * Función de peso aumentada w'
  */
  private def increasedWeightF(u: Int, v:Int): Double = {
    val e = new Edge(u, v)
    if (e.exists) e.distance else penalty
  }

  /**
  * Calcula la función de costo f: S --> R+ para la solución
  * @return la solución evaluada en la función de costo
  */
  def costFunction(): Double = {
    // ¿permutación? f siempre diferente
    //val permutation = _rng.shuffle(_cities.toSeq).toArray
    var total = 0.0

    for(i <- 1 until _cities.length) {
      total += increasedWeightF(_cities(i - 1), _cities(i))
    }
    // weightAvg (|S| - 1) ???
    total / weightAvg
  }

  /*
  * Intercambia dos elementos de un arreglo in-place
  */
  private def swap(i: Int, j: Int, arr: Array[Int]): Unit = {
    val element = arr(i)
    arr(i) = arr(j)
    arr(j) = element
  }
}
