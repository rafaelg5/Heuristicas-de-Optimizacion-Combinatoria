import scala.collection.mutable.HashMap
import java.sql.{Array=>SQLArray, _}

/**
* @constructor crea una nueva lista de conexiones para una lista de ciudades
* @param cities las ciudades
*/
class Connections(cities: Array[Int]) {

  private var conn: Connection = null

  try {
    Class.forName("org.sqlite.JDBC")
    conn = DriverManager.getConnection("jdbc:sqlite:./src/etc/database.db")
    conn.setAutoCommit(false)
  } catch {
    case ex: ClassNotFoundException => { println(ex.toString()) }
    case ex: SQLException => { println(ex.toString()) }
  }

  /*
  * Distancia máxima de las conexiones
  */
  private var maxDistance = {
    val str = cities.mkString(",")
    val query = f"select max(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);"
    val rs = conn.createStatement.executeQuery(query)
    rs.getDouble(1)
  }

  /*
  * Peso (distancia) promedio de las conexiones
  */
  private var weightAvg = {
    val str = cities.mkString(",")
    val query = f"select avg(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);"
    val rs = conn.createStatement.executeQuery(query)
    rs.getDouble(1)
  }

  private val connMatrix = Array.ofDim[Double](1092,1092)

  val rs = conn.createStatement.executeQuery("SELECT * FROM connections;")

  while (rs.next()) {
    val city1 = rs.getInt(1)
    val city2 = rs.getInt(2)
    val distance = rs.getDouble(3)
    connMatrix(city1 - 1)(city2 - 1) = distance
    connMatrix(city2 - 1)(city1 - 1) = distance
  }

  conn.commit
  conn.close

  /* Distancia máxima entre las ciudades */
  def getMaxDistance = maxDistance
  /* Peso promedio entre las ciudades */
  def getWeightAvg = weightAvg

  /**
  * Determina si existe una conexión entre dos ciudades
  * @param city1 la primera ciudad
  * @param city2 la segunda ciudad
  * @return true si existe dicha conexión
  */
  def exists(city1: Int, city2: Int): Boolean = {
    if(connMatrix(city1 - 1)(city2 - 1) == 0.0)
      return false
    return true
  }

  /**
  * Devuelve la distancia entre dos ciudades conectadas
  * @param city1 la primera ciudad
  * @param city2 la segunda ciudad
  * @return la distancia entre las ciudades o 0.0 si no están conectadas
  */
  def getDistance(city1: Int, city2: Int): Double = {
    return connMatrix(city1 - 1)(city2 - 1)
  }
}
