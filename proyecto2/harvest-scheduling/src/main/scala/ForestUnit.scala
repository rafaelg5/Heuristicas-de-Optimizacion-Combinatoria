
class ForestUnit(val index: Int, val period: Int, decision: Int, unitAge: Int,
                loggingC: Double){

  def this(index: Int, period: Int, decision: Int, unitAge: Int) = this(index, period, decision, unitAge, -1.0)

  def this(index: Int, period: Int, decision: Int) = this(index, period, decision, -1, -1.0)

  val rng = scala.util.Random

  // Superficie en hectáreas
  val surface = 30
  // Precio de madera por metro cúbico
  val logPrice = 100.0

  // Ingreso por metro cúbico aplicando una tasa de interés del 8%
  private var _revenue = ForestUnit.discountedPresentValue(logPrice, 8, period)
  // Costo de tala por metro cúbico
  private val _loggingCost = {
    if(loggingC != -1.0) loggingC
    else {
      val totalVal = (20 + rng.nextInt(51)) + rng.nextDouble
      if(totalVal > 70) 70.0
      else BigDecimal(totalVal).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
  }
  /* Variable de decisión 0, 1 donde la unidad con índice 'index' es talada
  * en el periodo 'period'
  */
  private var _x = decision

  // Edad promedio inicial de la unidad
  private var _initialAge: Int = {
    if(unitAge == -1) {
      if(index % 25 <= 5) 10 + rng.nextInt(6)
      else if(index % 25 >= 6 && index % 25 <= 10) 15 + rng.nextInt(6)
      else if(index % 25 >= 11 && index % 25 <= 15) 20 + rng.nextInt(6)
      else { 25 + rng.nextInt(6) }
    } else { unitAge }
  }

  // Edad promedio de los árboles en la unidad forestal
  private var _age: Int = { _initialAge + period - 1}

  // Volumen (de madera) por hectárea de la unidad
  private var _timberVolume:Int = {
    if(_age < 15) 120
    else { Parameters.timberVolume(_age - 15) }
  }

  def x = _x
  def x_(newVal: Int): Unit = {_x = newVal}

  def revenue = _revenue
  def loggingCost = _loggingCost
  def timberVolume = _timberVolume
  def age = _age

  /**
  * Crea una nueva unidad a partir de la actual, modificando el periodo y la
  * variable de decisión
  * @param p el nuevo periodo
  * @param decision el nuevo valor de la variable de decisión X
  * @return la nueva unidad forestal
  */
  def copyAndUpdate(p: Int): ForestUnit = {

    val newFU = new ForestUnit(index, p, _x, _initialAge)
    return newFU
  }

  def copy: ForestUnit = {
    new ForestUnit(index, period, _x, _initialAge, _loggingCost)
  }

  override def toString = {
    var harvested = "No"
    if(_x == 1) harvested = "Sí"
    f"unidad = $index\n" +
    f"periodo = $period\n" +
    f"talado = $harvested\n"
  }
}

object ForestUnit {
  // DPV = FV * (1 + R/100)^-t
  def discountedPresentValue(initialAmount: Double, R: Int, p: Int) = {
    initialAmount * math.pow(1 + R / 100.0,-1 * p)
  }
}
