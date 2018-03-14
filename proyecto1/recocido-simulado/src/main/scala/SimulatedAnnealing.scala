import util.control.Breaks._
import java.io._

class SimulatedAnnealing(initSolution: Solution) {

  private val epsilon = Parameters.epsilon
  private val coolingFactor = Parameters.coolingFactor
  private var _minSolution = initSolution
  private val batchSize = Parameters.batchSize
  private val exitBatch = 50 * batchSize
  private var X = 1

  def minSolution = _minSolution

  /**
  * Cálcula el promedio de soluciones aceptadas y la última solución dado un
  * número fijo de soluciones aceptadas llamado lote.
  * @param temp la temperatura inicial
  * @param s una solución
  * @return una tupla que contiene el promedio de soluciones y la última solución
  */
  private def computeBatch(temp: Double, s: Solution, writer: PrintWriter=null): (Double, Solution) = {

    var i = 0
    var r = 0.0
    var solution = s

    var exit = 0

    while (i < batchSize && exit < exitBatch){

      var newSolution = solution.neighbor
      if(newSolution.cost <= solution.cost + temp){
        i += 1
        val newSCost = newSolution.cost
        r += newSCost
        solution = newSolution
        if(writer != null) {
          writer.append(f"$X%d\t$newSCost%2.9f\n")
          writer.flush
          X += 1
        }
      }
      exit += 1
    }
    return (r / batchSize, solution)
  }

  /**
  * Heurística de aceptación por umbrales
  * @param temp la temperatura inicial
  * @param s una solución
  */
  def acceptByThresholds(_temp: Double, write: Boolean=false, graph: String=null): Unit = {

    if(write) {

      var avg = 0.0
      var temp = _temp
      var newSolution = _minSolution

      val name = if(graph != null) graph else "graph"

      var writer = new PrintWriter(new FileOutputStream(new File(f"src/etc/graphs/graph-$name"), true))
      val c = newSolution.cost
      writer.append(f"0\t$c%2.9f\n")

      while(temp > epsilon) {

        var auxAvg = Double.MaxValue
        breakable{
          while(avg <= auxAvg) {
            auxAvg = avg
            var result = computeBatch(temp, newSolution, writer)
            avg = result._1
            newSolution = result._2
            if(newSolution.cost < _minSolution.cost) _minSolution = newSolution
            //if(avg == 0) break
          }
        }
        temp *= coolingFactor
      }
      writer.close

    } else {

      var avg = 0.0
      var temp = _temp
      var newSolution = _minSolution

      while(temp > epsilon) {

        var auxAvg = Double.MaxValue
        breakable{
          while(avg <= auxAvg) {
            auxAvg = avg
            var result = computeBatch(temp, newSolution)
            avg = result._1
            newSolution = result._2
            if(newSolution.cost < _minSolution.cost) _minSolution = newSolution
            //if(avg == 0) break
          }
        }
        temp *= coolingFactor
      }
    }
  }
}
