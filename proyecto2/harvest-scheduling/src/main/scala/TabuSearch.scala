import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.LinkedList
import scala.util.control.Breaks._

object TabuSearch {

  /**
  * Crea una solución inicial para utilizar en la Búsqueda Tabú utilizando el
  * algoritmo de programación entera de Monte Carlo con parámetros especiales.
  */
  private def monteCarlo(units: Int, periods: Int, s: SchedulePlan,
    seed: Int): SchedulePlan = {

    /* Los parámetros especiales son:
    * núm. iteraciones: 250
    * volumen de cada periodo >= VOLUME_GOAL * 1.1
    */

    val rng = new Random(seed)
    var bestSolution = s
    var count = 0

    var bestObjective = bestSolution.objective

    /* ArrayList con los índices de las unidades para utilizar en el random. */
    var uI = ArrayBuffer[Int]()
    for(i <- 0 until units)
      uI += i

    for(iter <- 0 until 250){

      // Solución inicial en 0s
      val decisions = Array.ofDim[Int](periods, units)

      /* Arreglo en el que cada índice representa una unidad y el valor es
      * el periodo en que es talado, si el valor es -1 esa unidad no se taló */
      val unitsPeriods = Array.fill[Int](units)(-1)

      var unitsIndexes = ArrayBuffer[Int]()
      unitsIndexes ++= uI

      for(t <- 0 until periods){
        var volume = 0
        breakable {
          while(count < 600) {

            // unidad aleatoria
            val rUnit = rng.nextInt(unitsIndexes.size)
            // Si la unidad a talar cumple la restricción de adyacencia, se agrega
            if(s.unitMeetsAdjacency(rUnit, t)){
              // quitar unidad para no repetir y cumplir la restricción de singularidad
              unitsIndexes.remove(rUnit)
              unitsPeriods(rUnit) = t
              count = 0
              volume += s.plan(t)(rUnit).timberVolume
              // Si el volumen excede en 10% la meta del volumen, termina con el periodo
              if(volume > Parameters.VolumeGoal * 1.1){
                break
              }
            } else {
              count += 1
            }
          }
        }
      }

      // Asigna los valores de unidad y periodo a la solución
      for(i <- 0 until units){
        if(unitsPeriods(i) != -1) decisions(unitsPeriods(i))(i) = 1
      }

      val newS = new SchedulePlan(s.plan, decisions)
      val newObjective = newS.objective

      /* Si el nuevo objetivo es mayor que el mejor, actualiza el mejor con
         el nuevo. */
      if(newObjective > bestObjective){
        bestSolution = newS
        bestObjective = newObjective
      }
    }

    return bestSolution
  }

  /**
  * Búsqueda Tabú para el problema de Harvest Scheduling (URM)
  * @param iterations el número de iteraciones del algoritmo
  * @param schedule la solución inicial
  * @return una solución factible
  */
  def run(iterations: Int, schedule: SchedulePlan, seed: Int): SchedulePlan = {

    /* Tamaño de la lista tabú */
    val maxTabuSize = 100
    val periods = schedule.plan.length
    val units = schedule.plan(0).length

    /* Solución inicial usando el algoritmo de Monte Carlo */
    var best = monteCarlo(units, periods, schedule, seed)

    var bestObjective = best.objective

    /* Inicializar el mejor candidato con la solución inicial */
    var bestCandidate = new SchedulePlan(best.plan, best.decisions)

    var bestCObjective = bestCandidate.objective

    var tabuList = ArrayBuffer[SchedulePlan]()

    for(it <- 0 until iterations){

      for(i <- 0 until units){

        for(t <- 0 until periods){

          val newCDec = bestCandidate.decisions

          val index = bestCandidate.getUnitDecisionIndex(i)
          if(t != index) {

            newCDec(t)(i) = 1

            if(index != -1){
              newCDec(index)(i) = 0
            }

            val newCandidate = new SchedulePlan(bestCandidate.plan, newCDec)

            if(newCandidate.unitMeetsAdjacency(i, t)){

              val obj = if(index != -1) bestCObjective - bestCandidate.unitNetRevenue(i, index) else bestCObjective
              val unitRev = newCandidate.unitNetRevenue(i, t)
              val newObjective = obj + unitRev

              if(newObjective > bestCObjective && !tabuList.contains(newCandidate)){
                bestCandidate = newCandidate
                bestCObjective = newObjective
              }
            }
          }
        }
      }

      if(bestCObjective > bestObjective){
        best = bestCandidate
        bestObjective = bestCObjective
      }

      tabuList += bestCandidate

      if(tabuList.size > maxTabuSize) tabuList.remove(0)
    }

    return best
  }
}
