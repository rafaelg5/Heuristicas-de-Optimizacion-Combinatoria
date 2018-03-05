import scala.util.Random

/**
* @constructor crea una nueva solución
* @param solution un arreglo de enteros que representa la solución
*/
class Solution(solution: Array[Int], maxD: Double, weightA: Double, rng: Random) {

  def this(solution: Array[Int], rng: Random) = this(solution, -1, -1, rng: Random)

  private val _cities = solution
  private val _rng = rng
  private var maxDistance = maxD
  private var weightAvg = weightA

  /* Lista de aristas de la solución */
  private var edgeList = {

    var l = new Array[Edge](_cities.length - 1)
    for(i <- 1 until _cities.length) {
      l(i - 1) = new Edge(_cities(i - 1), _cities(i))
    }
    l
  }

  if(maxDistance == -1) { maxDistance = getMaxDistance }
  if(weightAvg == -1) { weightAvg = getWeightAvg }

  /* Obtiene la distancia máxima en la solución */
  private def getMaxDistance: Double = {
    val conn = DBConnection.open()
    val str = solution.mkString(",")
    val query = f"select max(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);";
    val rs = conn.createStatement().executeQuery(query)
    val d = rs.getDouble(1)
    DBConnection.close
    return d
  }

  /* Obtiene el promedio de pesos de la solución */
  private def getWeightAvg: Double = {
    val conn = DBConnection.open()
    val str = solution.mkString(",")
    val query = f"select avg(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);";
    val rs = conn.createStatement().executeQuery(query)
    val w = rs.getDouble(1)
    DBConnection.close
    return w
  }

  // Getters
  def cities = _cities
  def path = edgeList

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

    new Solution(neighborCities, maxDistance, weightAvg, _rng)
  }

  /*
  * Devuelve un valor de castigo para definir la función de peso aumentada
  */
  private def penalty = {
    var factor = 2.0
    factor * maxDistance
  }

  /*
  * Función de peso aumentada w'
  */
  private def increasedWeight(e: Edge): Double = {
    if (e.exists) e.distance else penalty
  }

  /**
  * Calcula la función de costo f: S --> R+ para la solución
  * @return la solución evaluada en la función de costo
  */
  def cost: Double = {
    var total = 0.0

    for(e <- edgeList) { total += increasedWeight(e) }

    total / ( weightAvg * (_cities.length - 1))
  }

  /**
  * Determina si la solución es factible o no
  * @return true si es factible, false e.o.c
  */
  def isFeasible: Boolean = {
    for(e <- edgeList) { if(!e.exists) return false }
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
    _cities.mkString(", ")
  }
}
