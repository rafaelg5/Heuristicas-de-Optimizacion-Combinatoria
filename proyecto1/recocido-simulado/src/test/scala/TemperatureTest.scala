import org.scalatest.FunSuite

/* La solución inicial y la temperatura aún no están definidas. Los valores de
para las funciones serán sustituidos cuando se definan los valores */

class TemperatureTest extends FunSuite {
  test("test percentageOfAccepted()"){
    val t = new Temperature(0.0, 0)

    val result = t.percentageOfAccepted(null)
    assert(result >= 0.0)
    assert(result <= 1.0)
  }
}
