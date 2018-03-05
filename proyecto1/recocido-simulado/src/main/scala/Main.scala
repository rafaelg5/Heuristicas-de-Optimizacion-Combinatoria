import java.io._
import scala.io.Source

object Main extends App {

  val rng = scala.util.Random
  var bestSolution: Solution = _
  val writer = new PrintWriter(new File("src/etc/solutions"))

  /* Instancia de 40 ciudades */
  for(i <- 1 to 4) {

    val thread = new Thread {

      rng.setSeed(i)

      // Construir la solución inicial (aleatoria)
      val rngSol = rng.shuffle(Parameters.instance1.toSeq)
      val initSolution = new Solution(rngSol.toArray, rng)

      // Obtener la temperatura inicial
      val t = new Temperature(Parameters.instance1.length)
      var realInitTemp = t.initTemp(initSolution, Parameters.initTemp, Parameters.percentage)

      // Recocido simulado. Aceptación por umbrales
      val sa = new SimulatedAnnealing(initSolution)
      sa.acceptByThresholds(realInitTemp)
      val lastSolution = sa.minSolution

      // Escribir en un archivo la solución obtenida si es factible
      if(i == 1) { bestSolution = lastSolution }
      if(lastSolution.isFeasible) {
        bestSolution = lastSolution
        writer.write(bestSolution.toString)
      }
    }
    thread.start
  }

    /*
    // Instancia de 150 ciudades
    for(i <- 1 to 4) {

      val thread = new Thread {

        rng.setSeed(i)

        //val initSolution = rng.shuffle(Parameters.instance2.toSeq)
        //var solution = new Solution(initSolution.toArray, rng)
        var initSolution = new Solution(Parameters.instance2, rng)

        val t = new Temperature(Parameters.instance1.length)
        var realInitTemp = t.initTemp(initSolution, Parameters.initTemp, Parameters.percentage)

        val sa = new SimulatedAnnealing(initSolution)
        sa.acceptByThresholds(realInitTemp)
        val lastSolution = sa.minSolution

        if(i == 1) { bestSolution = lastSolution }
        if(lastSolution.isFeasible) {
          bestSolution = lastSolution
          writer.write(bestSolution.toString)
        }
      }
      thread.start
    }*/

  writer.close
}
