import java.sql._;

/**
* @constructor crea una nueva arista (conexión) entre dos ciudades.
* @param id_city_1 el identificador de la primera ciudad
* @param id_city_2 el identificador de la segunda ciudad
*/
class Edge(_id_city_1: Int, _id_city_2: Int){

  val dbConn = DBConnection
  val conn = dbConn.open()
  val query: String = "SELECT id_city_1, id_city_2, distance FROM connections WHERE id_city_1 =" + _id_city_1 + " AND id_city_2 = " + _id_city_2;
  val rs = conn.createStatement().executeQuery(query);

  private var _exists = false
  private var _distance = -1.0

  if(rs.isBeforeFirst()) {
    _distance = rs.getDouble(3)
    _exists = true
  }

  dbConn.close()

  // Getters
  def id_city_1 = _id_city_1
  def id_city_2 = _id_city_2
  def distance = _distance
  def exists = _exists
  
}
