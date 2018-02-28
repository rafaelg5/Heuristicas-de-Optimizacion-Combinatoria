/**
* @constructor crea una nueva solución
* @param solution un arreglo de enteros que representa la solución
*/
class Solution(solution: Array[Int]) {

  private val _cities = solution
  private val _rng = scala.util.Random
  private var distanceSum: Double = _

  val conn = DBConnection.open()

  /* Distancia máxima en la solución */
  private val maxDistance = {
    val str = solution.mkString(",")
    val query = f"select max(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);";
    val rs = conn.createStatement().executeQuery(query)
    rs.getDouble(1)
  }

  /* Promedio de peso de la solución */
  private val weightAvg = {
    val str = solution.mkString(",")
    val query = f"select avg(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);";
    val rs = conn.createStatement().executeQuery(query)
    rs.getDouble(1)
  }

  DBConnection.close

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
  * Devuelve un valor de castigo para definir la función de peso aumentada
  */
  private def penalty = {
    var factor = 2.0
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
  def cost(): Double = {
    var total = 0.0

    for(i <- 1 until _cities.length) {
      total += increasedWeightF(_cities(i - 1), _cities(i))
    }

    total / ( weightAvg * (_cities.length - 1))
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
