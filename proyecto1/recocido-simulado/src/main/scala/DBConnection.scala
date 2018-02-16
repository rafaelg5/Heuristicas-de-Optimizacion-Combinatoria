import java.sql._;

/** Manejador de la conexión a la base de datos */
object DBConnection {

  private var conn: Connection = null

  /**
   * Abre una conexión a la base de datos.
   * @return la conexión a la base de datos
   */
  def open(): Connection = {
    try {
      if (conn == null || conn.isClosed()) {
        getConn()
      }
    } catch {
      case ex: SQLException => {
        println(ex.toString())
      }
    }
    return conn
  }

  /**
   * Obtiene la conexión a la base de datos
   * @return la conexión a la base de datos
   */
  private def getConn():Connection = {
    try {
      Class.forName("org.sqlite.JDBC")
      conn = DriverManager.getConnection("jdbc:sqlite:./src/database/database.db")
      conn.setAutoCommit(false)
    } catch {
      case ex: ClassNotFoundException => {
        println(ex.toString())
      }

      case ex: SQLException => {
        println(ex.toString())
      }
    }
    return conn
  }

  /**
   * Cierra la conexión a la base de datos
   */
  def close(): Unit = {

    try {
      if (conn == null || conn.isClosed()) {

        println("Error al cerrar la conexión. No hay conexión abierta")
        return
      }
      conn.commit()
      conn.close()

    } catch {
      case ex: SQLException => {
        println(ex.toString())
      }
    }
  }
}
