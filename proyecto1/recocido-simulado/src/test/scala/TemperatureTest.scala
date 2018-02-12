import org.scalatest.FunSuite

/* La solución inicial y la temperatura aún no están definidas. Los valores de
para las funciones serán sustituidos cuando se definan los valores */

class TemperatureTest extends FunSuite {
  test("definitions_1.percentageOfAccepted"){

    val result = definitions_1.percentageOfAccepted(List(), 0.0)
    assert(result >= 0.0)
    assert(result <= 1.0)
  }
}
