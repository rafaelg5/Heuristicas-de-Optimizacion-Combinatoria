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
    case ex: ClassNotFoundException => {
      println(ex.toString())
    }
    case ex: SQLException => {
      println(ex.toString())
    }
  }

  private var maxDistance = {
    val str = cities.mkString(",")
    val query = f"select max(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);"
    val rs = conn.createStatement.executeQuery(query)
    rs.getDouble(1)
  }

  private var weightAvg = {
    val str = cities.mkString(",")
    val query = f"select avg(distance) from connections where id_city_1 in ($str%s) and id_city_2 in ($str%s);"
    val rs = conn.createStatement.executeQuery(query)
    rs.getDouble(1)
  }

  private val connMap = HashMap.empty[(Int, Int), Double]

  val rs = conn.createStatement.executeQuery("SELECT * FROM connections;")

  while (rs.next()) {
    val city1 = rs.getInt(1)
    val city2 = rs.getInt(2)
    val distance = rs.getDouble(3)
    connMap += ((city1, city2) -> distance)
  }

  conn.commit
  conn.close

  def getMaxDistance = maxDistance
  def getWeightAvg = weightAvg

  def exists(city1: Int, city2: Int): Boolean = {
    if((connMap contains (city1, city2)) || (connMap contains (city2, city1)))
      return true
    return false
  }

  def getDistance(city1: Int, city2: Int): Double = {

    if(connMap contains (city1, city2))
      return connMap((city1, city2))

    if(connMap contains (city2, city1))
      return connMap((city2, city1))

    return -1.0
  }
}
