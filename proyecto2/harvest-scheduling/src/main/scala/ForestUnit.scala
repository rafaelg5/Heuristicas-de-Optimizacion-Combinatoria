
class ForestUnit(i: Int, p: Int, unitAge: Int, loggingC: Double){

  // Superficie en hectáreas
  val surface = 30
  // Precio de madera por metro cúbico
  val logPrice = 100.0

  val index = i
  val period = p

  // Ingreso por metro cúbico aplicando una tasa de interés del 8%
  val revenue = ForestUnit.futureValue(logPrice, 8, period)
  // Edad promedio inicial de la unidad
  private val initialAge = unitAge
  // Edad promedio de los árboles en la unidad forestal
  val age = initialAge + period -1

  // Volumen (de madera) por hectárea de la unidad
  val timberVolume:Int = {
    if(age < 15) 150
    Parameters.TimberVolume(age - 15)
  }

  // Costo de tala por metro cúbico
  val loggingCost = loggingC

  /**
  * Crea una nueva unidad a partir de la actual, modificando el periodo
  * @param p el nuevo periodo
  * @return la nueva unidad forestal
  */
  def copyAndUpdate(p: Int): ForestUnit = {
    new ForestUnit(index, p, initialAge, loggingCost)
  }

  /**
  * Crea un nuevo objeto con los mismos valores de este
  * @return una copia de este objeto
  */
  def copy: ForestUnit = {
    new ForestUnit(index, period, initialAge, loggingCost)
  }

}

object ForestUnit {

  /**
  * Calcula el valor en el futuro de cierta cantidad con una tasa de descuento
  * FV = PV * (1 + r)^n
  * @param initialAmount el valor presente
  * @param discountRate la tasa de descuento
  * @param period el periodo o número de años
  * @return el valor en el futuro de esta cantidad
  */
  def futureValue(initialAmount: Double, discountRate: Int, period: Int): Double = {
    initialAmount * Math.pow(1.0 + (discountRate / 100.0), period)
  }
}
