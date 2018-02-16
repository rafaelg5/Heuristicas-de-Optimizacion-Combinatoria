import org.scalatest.FunSuite
import java.sql._

class DBConnectionTest extends FunSuite {

  /**
  * Prueba de la conexión
  *
  */
  test("test open()"){
    val conn = DBConnection.open()
    try {
      assert(!conn.isClosed())
      assert(DBConnection.open() === conn)
      DBConnection.close()
      assert(conn.isClosed())
    } catch {
      case ex: SQLException => {
        fail()
      }
    }
  }

  /**
  * Prueba para cerrar la conexión
  */
  test("test close()") {
    val conn = DBConnection.open()
    try {
      assert(!conn.isClosed());
      assert(DBConnection.open() === conn);
      DBConnection.close();
      assert(conn.isClosed());
    } catch {
      case ex: SQLException => {
        fail()
      }
    }
  }

}
