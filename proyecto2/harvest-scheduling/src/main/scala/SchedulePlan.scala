
/**
* @constructor crea un nuevo plan o programa para el aprovechamiento de recursos
* forestales
*/
class SchedulePlan(solution: Array[Array[ForestUnit]], dec: Array[Array[Int]]) {

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
  *
  */

  /* Restricciones:
  * 1. Restricción de singularidad
  * 2. Restricciones de adyacencia
  */

  private var _decisions = dec
  val plan = solution

  def decisions = copyDecisions

  val units = solution(0).length
  val periods = solution.length

  /**
  * Determina si una unidad en un periodo cumple la restricción de adyacencia
  * @param unit la unidad
  * @param period el periodo
  * @return true si cumple la restricción, false e.o.c.
  */
  def unitMeetsAdjacency(unit: Int, period: Int): Boolean = {

    val initialPeriod = if(period - 2 < 0) 0 else period - 2

    val finalPeriod = if(period + 2 > periods - 1) periods - 1 else period + 2

    var sum1 = 0
    if(unit - 1 >= 0)
      for(t <- initialPeriod to finalPeriod){
        sum1 += decisions(t)(unit - 1)
      }

    var sum2 = 0
    if(unit - 25 >= 0)
      for(t <- initialPeriod to finalPeriod){
        sum2 += decisions(t)(unit - 25)
      }

    val cond1 = (sum1 + sum2 + 4*decisions(period)(unit)*sum2 + sum1) <= 4
    val cond2 = plan(period)(unit).age >= 19

    return cond1 && cond2
  }

  /*
  * Copia la matriz de decisiones
  */
  private def copyDecisions: Array[Array[Int]] = {

    val newArray = Array.ofDim[Int](periods, units)

    for(t <- 0 until periods;
        i <- 0 until units) {
        newArray(t)(i) = _decisions(t)(i)
    }

    return newArray
  }

  /**
  * Devuelve el periodo en que fue talada la unidad
  * @param unit la unidad
  * @return el índice del periodo que fue talada la unidad o -1 si nunca fue talada
  */
  def getUnitDecisionIndex(unit: Int): Int = {
    for(t <- 0 until periods) { if(decisions(t)(unit) == 1) return t }
    return -1
  }

  /**
  * Calcula la función objetivo que queremos maximizar. Las variables a tomar en
  * cuenta son: ingreso (por unidad y periodo), costo de tala (por unidad y
  * periodo), volumen de madera (por unidad y periodo) y
  * la variable de decisión (por unidad y periodo). Las unidades del valor son:
  * precio ($) / ha
  * @return el valor de la evaluación
  */
  def objective: Double = {

    var total = 0.0

    for(i <- 0 until units){
      val t = getUnitDecisionIndex(i)
      if(t != -1) total += unitNetRevenue(i, t)
    }

    return total
  }

  /**
  * Calcula el ingreso neto de una unidad en un periodo determinado
  * @param unit la unidad
  * @param period el periodo
  * @return el ingreso neto
  */
  def unitNetRevenue(unit: Int, period: Int): Double = {

      /* *** variables del plan *** */
      val fUnit = plan(period)(unit)
      val rev = fUnit.revenue
      val lc = fUnit.loggingCost
      val v = fUnit.timberVolume
      /* ************************* */

    return (rev - lc) * v;
  }


  override def toString = {
    var s = ""
    for(t <- 0 until periods){

      val rPeriod = t + 1
      s += f"\nPeriodo $rPeriod%d\n"

      for(c <- 0 until math.sqrt(units).toInt)
        s += "+--"

      s += "+\n"
      for(i <- 0 until units){
        val unit = plan(t)(i)
        var age = getUnitCurrentAge(unit) + ""

        if(age.length == 1)
          age = "0" + age

        s += f"|$age%s"
        if(unit.index % math.sqrt(units).toInt == 0 && unit.index != 0) {
          s += "|\n"
          for(c <- 0 until math.sqrt(units).toInt)
            s += "+--"
          s+= "+\n"
        }
      }
    }
    s
  }

  /*
  * Obtiene la edad de la unidad en el periodo de tiempo que se encuentra
  */
  private def getUnitCurrentAge(u: ForestUnit): Int = {

    var i = -1
    for(t <- 0 until periods){
      if(decisions(t)(u.index -1) == 1)
        i = t
    }

    if(i == -1)
      return u.age
    if(i > u.period - 1)
      return u.age
    if(i == u.period - 1)
      return 0
    return u.period -1 - i
  }

}
