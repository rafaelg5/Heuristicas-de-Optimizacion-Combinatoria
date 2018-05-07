
class Cell(val revenue: Double, val loggingCost: Double,
  val volumePerHectare: Double, decision: Int){

  private var _x = decision
  def x = _x
  def x_= (newVal: Int) = _x = newVal
}
