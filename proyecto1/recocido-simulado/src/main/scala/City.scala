import java.sql._;

/**
* @constructor crea una nueva ciudad.
* @param _id el identificador en la base de datos de la ciudad
*/
class City(_id: Int){

  val dbConn = DBConnection
  val conn: Connection = dbConn.open()
  val query: String = "SELECT name, country, population, latitude, longitude FROM cities WHERE id =" + _id;
  val rs: ResultSet = conn.createStatement().executeQuery(query);

  private var _name = rs.getString(1)
  private var _country = rs.getString(2)
  private var _population = rs.getInt(3)
  private var _latitude = rs.getDouble(4)
  private var _longitude = rs.getDouble(5)

  dbConn.close()

  // Getters
  def id = _id
  def name = _name
  def country = _country
  def population = _population
  def latitude = _latitude
  def longitude = _longitude

}
