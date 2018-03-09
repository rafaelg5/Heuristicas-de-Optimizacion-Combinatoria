import scala.util.Random

/**
* @constructor crea una nueva solución
* @param solution un arreglo de enteros que representa la solución
*/
class Solution(solution: Array[Int], conns: Connections, rng: Random) {

  def this(solution: Array[Int], rng: Random) = this(solution, null, rng)
  
  private val _cities = solution
  private val _rng = rng
  private val factor = Parameters.factor
  private var connections = conns

  if(connections == null) { connections = new Connections(_cities) }

  def cities = _cities

  /**
  * Obtiene una solución vecina de manera aleatoria. La solución vecina solo
  * cambia en una arista de la solución.
  * @return la solución vecina
  */
  def neighbor: Solution = {

    var num = 0
    var num2 = 0
    do {
      num = _rng.nextInt(_cities.length)
      num2 = _rng.nextInt(_cities.length)
    } while(num == num2)

    var neighborSolution = _cities.clone
    swap(num, num2, neighborSolution)

    new Solution(neighborSolution, connections, _rng)
  }

  /**
  * Calcula la función de costo f: S --> R+ para la solución
  * @return la solución evaluada en la función de costo
  */
  def cost: Double = {

    var total = 0.0
    val maxDistance = connections.getMaxDistance
    val weightAvg = connections.getWeightAvg

    for(i <- 1 until _cities.length) {
      val d = connections.getDistance(_cities(i - 1), _cities(i))
      if(d == -1.0)
        total += factor * maxDistance
      else
        total += d
    }
    total / (weightAvg * (_cities.length - 1))
  }

  /**
  * Determina si la solución es factible o no
  * @return true si es factible, false e.o.c
  */
  def isFeasible: Boolean = {
    for(i <- 1 until _cities.length) {
      if(!connections.exists(_cities(i - 1), _cities(i)))
        return false
    }
    return true
  }

  /*
  * Intercambia dos elementos de un arreglo in-place
  */
  private def swap(i: Int, j: Int, arr: Array[Int]): Unit = {
    val element = arr(i)
    arr(i) = arr(j)
    arr(j) = element
  }

  /**
  * Representación en cadena de la solución
  * @return la cadena
  */
  override def toString = {
    _cities.mkString(",")
  }
}
