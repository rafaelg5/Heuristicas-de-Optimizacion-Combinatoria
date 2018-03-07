import java.io._
import scala.io.Source
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object Main extends App {

  val rng = scala.util.Random
  var bestSolution: Solution = _
  val writer = new PrintWriter(new File("src/etc/solutions"))

  val seed = rng.nextLong()

  val startTime = System.nanoTime
  for(i <- seed to seed + 200) {
    rng.setSeed(i)

    // Construir la solución inicial (aleatoria)
    val rngSol = rng.shuffle(Parameters.instance1.toSeq)
    val initSolution = new Solution(rngSol.toArray, rng)


    // Obtener la temperatura inicial
    val t = new Temperature(Parameters.initTemp, initSolution)
    var realInitTemp = t.initTemp(Parameters.percentage)

    // Recocido simulado. Aceptación por umbrales
    val sa = new SimulatedAnnealing(initSolution)
    sa.acceptByThresholds(realInitTemp)
    val lastSolution = sa.minSolution

    if(lastSolution.isFeasible) { writer.write(f"$i%d: $lastSolution%s\n") }
  }
  val endTime = System.nanoTime
  val duration = (endTime - startTime) / 1000000000.0
  writer.write(f"Total time:$duration%f\n")

  /* Instancia de 40 ciudades
  for(i <- seed to seed + 100) {

    val task: Future[Solution] = Future {

      rng.setSeed(i)

      // Construir la solución inicial (aleatoria)
      val rngSol = rng.shuffle(Parameters.instance1.toSeq)
      val initSolution = new Solution(rngSol.toArray, rng)


      // Obtener la temperatura inicial
      val t = new Temperature(Parameters.instance1.length, Parameters.initTemp, initSolution)
      var realInitTemp = t.initTemp(Parameters.percentage)

      // Recocido simulado. Aceptación por umbrales
      val sa = new SimulatedAnnealing(initSolution)
      sa.acceptByThresholds(realInitTemp)
      sa.minSolution
    }

    task onComplete {
      case Success(value) => writer.write(f"$i%d: $value%s\n")
      case Failure(e) => println("Ha ocurrido un error: " + e.getMessage)
      // Escribir en un archivo la solución obtenida
      //writer.write(lastSolution.toString)
      /if(i == 1) { bestSolution = lastSolution }
      if(lastSolution.isFeasible) {
        bestSolution = lastSolution
        //writer.write(bestSolution.toString)
      }
    }

  }

    /
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
