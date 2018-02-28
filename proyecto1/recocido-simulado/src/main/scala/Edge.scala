import java.sql._;

/**
* @constructor crea una nueva arista (conexión) entre dos ciudades.
* @param id1 el identificador de la primera ciudad
* @param id2 el identificador de la segunda ciudad
*/
class Edge(id1: Int, id2: Int){

  val conn = DBConnection.open()
  val query = f"""SELECT id_city_1, id_city_2, distance FROM
  |connections WHERE (id_city_1 = $id1%d AND id_city_2 = $id2%d) OR
  |(id_city_1 = $id2%d AND id_city_2 = $id1%d);""".stripMargin.replaceAll("\n", " ")
  val rs = conn.createStatement().executeQuery(query);

  private var _exists = false
  private var _distance = -1.0

  if(rs.isBeforeFirst()) {
    _distance = rs.getDouble(3)
    _exists = true
  }

  DBConnection.close()

  // Getters
  def id_city_1 = id1
  def id_city_2 = id2
  def distance = _distance
  def exists = _exists

}
