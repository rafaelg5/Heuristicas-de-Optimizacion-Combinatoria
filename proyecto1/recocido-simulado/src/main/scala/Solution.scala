import scala.util.Random

/**
* @constructor crea una nueva solución
* @param solution un arreglo de enteros que representa la solución
* @param conns todas las conexiones existentes de las ciudades
* @param c el costo de la solución
*/
class Solution(solution: Array[Int], conns: Connections, rng: Random, c: Double) {

  def this(solution: Array[Int], rng: Random) = this(solution, null, rng, -1)

  private val _cities = solution
  private val _rng = rng
  private val factor = Parameters.factor
  private var connections = conns

  if(connections == null) { connections = new Connections(_cities) }
  /* Constantes para la función de costo */
  private val maxDistance = connections.getMaxDistance
  private val penalty = maxDistance * factor
  private val weightAvg = connections.getWeightAvg * (_cities.length - 1)
  
  private var _cost = c

  /* Si es la primera vez que crea una solución, calcula su costo */
  if(_cost == -1) {
    var total = 0.0

    for(i <- 1 until _cities.length) {
      val d = connections.getDistance(_cities(i - 1), _cities(i))
      if(d == 0.0)
        total += penalty
      else
        total += d
    }
    _cost = total / weightAvg
  }

  def cities = _cities

  /**
  * Obtiene una solución vecina de manera aleatoria. La solución vecina solo
  * cambia en una arista de la solución.
  * @return la solución vecina
  */
  def neighbor: Solution = {

    var num = 0; var num2 = 0
    while(num == num2) {
      num = _rng.nextInt(_cities.length)
      num2 = _rng.nextInt(_cities.length)
    }

    val tmp = num
    num = scala.math.min(num, num2)
    num2 = scala.math.max(tmp, num2)

    val neighborSolution = _cities.clone

    /* Calcular costo de vecino para no volver a calcularlo al crear un nuevo objeto */
    /* Se toman todos los posibles casos de acomodación de vecinos */

    /* El primer elemento y el último */
    if(num == 0 && num2 == _cities.length - 1) {

      var oD1 = connections.getDistance(cities(0), cities(1))
      var oD2 = connections.getDistance(cities(num2 - 1), cities(num2))
      if(oD1 == 0.0) oD1 = penalty
      if(oD2 == 0.0) oD2 = penalty

      var nD1 = connections.getDistance(cities(num2), cities(1))
      var nD2 = connections.getDistance(cities(num2 - 1), cities(0))
      if(nD1 == 0.0) nD1 = penalty
      if(nD2 == 0.0) nD2 = penalty

      val oldDistance = oD1 + oD2
      val newDistance = nD1 + nD2
      swap(num, num2, neighborSolution)
      val nCost = _cost + (newDistance - oldDistance) / weightAvg
      return new Solution(neighborSolution, connections, _rng, nCost)
    }

    /* El primer elemento y el segundo */
    if(num == 0 && num2 == 1) {

      var oD1 = connections.getDistance(cities(0), cities(1))
      var oD2 = connections.getDistance(cities(1), cities(2))
      if(oD1 == 0.0) oD1 = penalty
      if(oD2 == 0.0) oD2 = penalty


      var nD1 = connections.getDistance(cities(1), cities(0))
      var nD2 = connections.getDistance(cities(0), cities(2))
      if(nD1 == 0.0) nD1 = penalty
      if(nD2 == 0.0) nD2 = penalty

      val oldDistance = oD1 + oD2
      val newDistance = nD1 + nD2
      swap(num, num2, neighborSolution)
      val nCost = _cost + (newDistance - oldDistance) / weightAvg
      return new Solution(neighborSolution, connections, _rng, nCost)
    }

    /* El primer elemento y cualquiera */
    if(num == 0) {

      var oD1 = connections.getDistance(cities(0), cities(1))
      var oD2 = connections.getDistance(cities(num2 - 1), cities(num2))
      var oD3 = connections.getDistance(cities(num2), cities(num2 + 1))
      if(oD1 == 0.0) oD1 = penalty
      if(oD2 == 0.0) oD2 = penalty
      if(oD3 == 0.0) oD3 = penalty


      var nD1 = connections.getDistance(cities(num2), cities(1))
      var nD2 = connections.getDistance(cities(num2 - 1), cities(0))
      var nD3 = connections.getDistance(cities(0), cities(num2 + 1))
      if(nD1 == 0.0) nD1 = penalty
      if(nD2 == 0.0) nD2 = penalty
      if(nD3 == 0.0) nD3 = penalty

      val oldDistance = oD1 + oD2 + oD3
      val newDistance = nD1 + nD2 + nD3
      swap(num, num2, neighborSolution)
      val nCost = _cost + (newDistance - oldDistance) / weightAvg
      return new Solution(neighborSolution, connections, _rng, nCost)
    }

    /* El penúltimo elemento y el último */
    if(num == _cities.length - 2 && num2 == _cities.length - 1) {

      var oD1 = connections.getDistance(cities(_cities.length - 3), cities(_cities.length - 2))
      var oD2 = connections.getDistance(cities(_cities.length - 2), cities(_cities.length - 1))
      if(oD1 == 0.0) oD1 = penalty
      if(oD2 == 0.0) oD2 = penalty


      var nD1 = connections.getDistance(cities(_cities.length - 3), cities(_cities.length - 1))
      var nD2 = connections.getDistance(cities(_cities.length - 1), cities(_cities.length - 2))
      if(nD1 == 0.0) nD1 = penalty
      if(nD2 == 0.0) nD2 = penalty

      val oldDistance = oD1 + oD2
      val newDistance = nD1 + nD2
      swap(num, num2, neighborSolution)
      val nCost = _cost + (newDistance - oldDistance) / weightAvg
      return new Solution(neighborSolution, connections, _rng, nCost)
    }

    /* Cualquier elemento y el último */
    if(num2 == _cities.length - 1) {

      var oD1 = connections.getDistance(cities(num - 1), cities(num))
      var oD2 = connections.getDistance(cities(num), cities(num + 1))
      var oD3 = connections.getDistance(cities(_cities.length - 2), cities(_cities.length - 1))
      if(oD1 == 0.0) oD1 = penalty
      if(oD2 == 0.0) oD2 = penalty
      if(oD3 == 0.0) oD3 = penalty


      var nD1 = connections.getDistance(cities(num - 1), cities(_cities.length - 1))
      var nD2 = connections.getDistance(cities(_cities.length - 1), cities(num + 1))
      var nD3 = connections.getDistance(cities(_cities.length - 2), cities(num))
      if(nD1 == 0.0) nD1 = penalty
      if(nD2 == 0.0) nD2 = penalty
      if(nD3 == 0.0) nD3 = penalty

      val oldDistance = oD1 + oD2 + oD3
      val newDistance = nD1 + nD2 + nD3
      swap(num, num2, neighborSolution)
      val nCost = _cost + (newDistance - oldDistance) / weightAvg
      return new Solution(neighborSolution, connections, _rng, nCost)
    }

    /* Cualesquiera dos elementos excepto el primero y el último que estén contigüos */
    if(num2 - num == 1) {

      var oD1 = connections.getDistance(cities(num - 1), cities(num))
      var oD2 = connections.getDistance(cities(num), cities(num2))
      var oD3 = connections.getDistance(cities(num2), cities(num2 + 1))
      if(oD1 == 0.0) oD1 = penalty
      if(oD2 == 0.0) oD2 = penalty
      if(oD3 == 0.0) oD3 = penalty


      var nD1 = connections.getDistance(cities(num - 1), cities(num2))
      var nD2 = connections.getDistance(cities(num2), cities(num))
      var nD3 = connections.getDistance(cities(num), cities(num2 + 1))
      if(nD1 == 0.0) nD1 = penalty
      if(nD2 == 0.0) nD2 = penalty
      if(nD3 == 0.0) nD3 = penalty

      val oldDistance = oD1 + oD2 + oD3
      val newDistance = nD1 + nD2 + nD3
      swap(num, num2, neighborSolution)
      val nCost = _cost + (newDistance - oldDistance) / weightAvg
      return new Solution(neighborSolution, connections, _rng, nCost)
    }

    /* Cualesquiera dos elementos que no cumplan los casos anteriores */
    var oD1 = connections.getDistance(cities(num - 1), cities(num))
    var oD2 = connections.getDistance(cities(num), cities(num + 1))
    var oD3 = connections.getDistance(cities(num2 - 1), cities(num2))
    var oD4 = connections.getDistance(cities(num2), cities(num2 + 1))
    if(oD1 == 0.0) oD1 = penalty
    if(oD2 == 0.0) oD2 = penalty
    if(oD3 == 0.0) oD3 = penalty
    if(oD4 == 0.0) oD4 = penalty


    var nD1 = connections.getDistance(cities(num - 1), cities(num2))
    var nD2 = connections.getDistance(cities(num2), cities(num + 1))
    var nD3 = connections.getDistance(cities(num2 - 1), cities(num))
    var nD4 = connections.getDistance(cities(num), cities(num2 + 1))
    if(nD1 == 0.0) nD1 = penalty
    if(nD2 == 0.0) nD2 = penalty
    if(nD3 == 0.0) nD3 = penalty
    if(nD4 == 0.0) nD4 = penalty

    val oldDistance = oD1 + oD2 + oD3 + oD4
    val newDistance = nD1 + nD2 + nD3 + nD4
    swap(num, num2, neighborSolution)
    val nCost = _cost + (newDistance - oldDistance) / weightAvg
    return new Solution(neighborSolution, connections, _rng, nCost)
  }

  def cost = _cost

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
