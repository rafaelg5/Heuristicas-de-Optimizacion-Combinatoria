import org.scalatest.FunSuite

class ThresholdTest extends FunSuite {
  test("test computeBatch()"){

    val th = new Threshold()

    val result = th.computeBatch()
    assert(result._1 > 0)
    assert(result._1 < 1)
  }
}
