import scala.util.Random

/**
* @constructor crea un nuevo plan o programa para el aprovechamiento de recursos
* forestales
*/
class SchedulePlan(solution: Array[Array[ForestUnit]]) {

  /*
  * Función objetivo:
  * Max ΣΣ (Rev_it - Lc_it) * X_it * V_it
  * donde
  * i = unidad
  * t = periodo
  * Rev_it = ingreso por metro cúbico para la unidad i talada en el periodo t
  * Lc_it = costo de tala por metro cúbico para la unidad i talada en el periodo t
  * V_it = volumen por hectarea para la unidad i talada en el periodo t
  * X_it = variable de decisión (0,1) donde la unidad i es talada en el periodo t
  * Vp_t = penalización de volumen por metro cúbico en el periodo t
  * d_t = desviación del volumen deseado en el periodo t en metros cúbicos
  *
  */

  /* Restricciones:
  * 1. Restricción de volumen
  * 2. Restricción de singularidad
  * 3. Restricciones de adyacencia
  */

  private var _table = solution

  def table = _table

  private val _units = solution(0).length
  private val _periods = solution.length

  def units = _units
  def periods = _periods

  def meetsAdjacency:Boolean = {

    true
  }

  /**
  * Revisa que las unidades cumplan la restricción de singularidad.
  * @return true si cumple la restricción, false e.o.c
  */
  def meetsSingularity: Boolean = {
    for(i <- 0 until _units) {
      var total = 0
      for(j <- 0 until _periods) {
        total += table(j)(i).x
      }
      if(total > 1) return false
    }
    return true
  }

  /* Obtiene el índice del periodo en el cual cierta unidad es talada.
  * Si nunca se taló, devuelve -1
  */
  private def getDecisionIndex(unit: Int): Int = {

      for(t <- 0 until _periods){
        if(_table(t)(unit).x == 1) return t
      }

    return -1
  }

  private def copyTable: Array[Array[ForestUnit]] = {

    val arr = Array.ofDim[ForestUnit](periods,units)

    for(period <- 0 until _periods) {
      for(unit <- 0 until _units){
        arr(period)(unit) = _table(period)(unit).copy
      }
    }
    return arr
  }

  def setDecision(unit: Int, period: Int, value: Int) = {
    _table(period)(unit).x_(value)
  }

  /**
  * Devuelve la vecindad (todas las posibles soluciones vecinas) del plan
  * @return las soluciones vecinas
  */
  def neighborhood:Array[SchedulePlan] = {

    val neighbors = scala.collection.mutable.ArrayBuffer.empty[SchedulePlan]
    var c = 0
    for(i <- 0 until _units){

      val period = getDecisionIndex(i)
      if(period != - 1) {
        var newPlan = new SchedulePlan(copyTable)
        newPlan.setDecision(i,period,0)
        neighbors += newPlan
      }

      for(t <- 0 until _periods){

        if(t != period) {
          var newPlan = new SchedulePlan(copyTable)
          newPlan.setDecision(i,t,1)
          newPlan.setDecision(i,period,0)
          //var newPlan = copyTable
          //newPlan(t)(i).x_(1)
          //newPlan(period)(i).x_(0)
          neighbors += newPlan
        }
      }
      c += 1
      println(c)
    }
    return neighbors.toArray
  }

  /**
  * Calcula la desviación estandar del volumen en un periodo de tiempo
  * @param period el periodo
  * @return la desviación del volumen deseado
  */
  def deviation(period: Int): Double = {

    var mean = 0.0

    for(i <- 0 until _units){
      val fUnit = _table(period)(i)
      mean += fUnit.timberVolume * fUnit.x
    }
    mean /= units

    var variance = 0.0

    for(i <- 0 until _units){
      val fUnit = _table(period)(i)
      variance += math.pow((fUnit.timberVolume - mean)*fUnit.x, 2)
    }
    return math.sqrt(variance / units)
  }

  /**
  * Calcula la función objetivo que queremos maximizar. Las variables a tomar en
  * cuenta son: ingreso (por unidad y periodo), costo de tala (por unidad y
  * periodo), volumen de madera (por unidad y periodo), variable de decisión
  * (por unidad y periodo), penalización del volumen (por periodo), desviación
  * del volumen deseado (por periodo).
  * @return el valor de la evaluación
  */
  def objective: Double = {

    var sum = 0.0

    for(t <- 0 until _periods) {

      val dev = deviation(t)
      val penalty = ForestUnit.discountedPresentValue(100, 9, t+1)

      for(i <- 0 until _units) {

        /* *** variables del plan *** */
        val fUnit = _table(t)(i)
        val rev = fUnit.revenue
        val lc = fUnit.loggingCost
        val v = fUnit.timberVolume
        val x = fUnit.x
        /* ***************** */

        if((rev - lc) > 0 && x == 1){

          sum += (rev - lc) * v
        }
      }

      sum -= penalty * deviation(t)
    }
    return sum
  }

  override def toString = {
    var s = ""
    for(t <- 0 until periods){
      val p = t +1
      s += f"\nPeriodo $p\n"
      for(c <- 0 until math.sqrt(units).toInt) { s += "+--" }
      s+= "+\n"
      for(i <- 0 until units){
        val unit = _table(t)(i)
        var age = getUnitCurrentAge(unit).toString
        if(age.length == 1) age = "0" + age
        s += f"|$age"
        if(unit.index % math.sqrt(units).toInt == 0 && unit.index != 0) {
          s += "|\n"
          for(c <- 0 until math.sqrt(units).toInt) { s += "+--" }
          s+= "+\n"
        }
      }
    }
    s
  }

  private def getUnitCurrentAge(u: ForestUnit):Int = {

    var i = -1
    for(t <- 0 until periods){
      if(_table(t)(u.index-1).x == 1) i = t
    }
    if(i == -1) return u.age
    if(i > u.period - 1) return u.age
    if(i == u.period - 1) return 0
    return u.period -1 - i
  }

}
