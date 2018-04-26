import java.io._
import scala.io.Source

object Main extends App {

  val rng = scala.util.Random
  args.length match {
    case 0 => println("Se necesitan más argumentos! Leer modo de uso en el README.")

    case 1 => {
      val seed = {

        var r = rng.nextLong
        val coin = rng.nextFloat
        if(Long.MaxValue - 1000 < r) r = Long.MaxValue - 1000
        if(coin > 0.5) r else (-1) * r
      }
      run(args(0).toInt, Parameters.seeds, seed)
    }
    case 2 => {
      val seed = args(1).toLong
      run(args(0).toInt, 1, seed)
    }

    case what => printf(f"Demasiados argumentos ($what)! Leer modo de uso en el README.")
  }

  private def run(instance: Int, iterations: Int, seed: Long): Unit = {

    if(instance != 40 && instance != 150) {
      println("Tamaño de instancia incorrecto. Leer modo de uso en el README.")
      return
    }

    if(instance == 40) {

      val l1 = Parameters.instance1.length

      /********** 40 ciudades **********/
      for(i <- seed to seed + iterations - 1) {

        val ti = System.currentTimeMillis
        var writer = new PrintWriter(new File(f"src/etc/solutions/$l1-$i%d"))

        rng.setSeed(i)

        // Construir la solución inicial (aleatoria)
        val rngSol = rng.shuffle(Parameters.instance1.toSeq)

        val initSolution = new Solution(rngSol.toArray, rng)

        // Obtener la temperatura inicial
        val t = new Temperature(Parameters.initTemp, initSolution)
        var realInitTemp = t.initTemp(Parameters.percentage)

        // Recocido simulado. Aceptación por umbrales
        val sa = new SimulatedAnnealing(initSolution)
        //sa.acceptByThresholds(realInitTemp, true, f"$l1%d-$i%d")
        sa.acceptByThresholds(realInitTemp, false)

        val tf = System.currentTimeMillis
        val dur = tf - ti
        val minSolution = sa.minSolution
        val c = minSolution.cost
        val feasible = { if(minSolution.isFeasible) "YES" else "NO"}

        writer.write(f"Path:$minSolution%s\nEvaluation:$c%2.9f\nFeasible: $feasible%s\n$dur%f\nSeed: $seed%d")
        writer.flush
        writer.close
      }

    } else if(instance == 150) {

      val l2 = Parameters.instance2.length

      /********** 150 ciudades **********/
      for(i <- seed to seed + iterations - 1) {

        val ti = System.currentTimeMillis
        var writer = new PrintWriter(new File(f"src/etc/solutions/$l2%d-$i%d"))

        rng.setSeed(i)

        // Construir la solución inicial (aleatoria)
        val rngSol = rng.shuffle(Parameters.instance2.toSeq)

        val initSolution = new Solution(rngSol.toArray, rng)

        // Obtener la temperatura inicial
        val t = new Temperature(Parameters.initTemp, initSolution)
        var realInitTemp = t.initTemp(Parameters.percentage)

        // Recocido simulado. Aceptación por umbrales
        val sa = new SimulatedAnnealing(initSolution)
        //sa.acceptByThresholds(realInitTemp, true, f"$l2%d-$i%d")
        sa.acceptByThresholds(realInitTemp, false)

        val tf = System.currentTimeMillis
        val dur = tf - ti
        val minSolution = sa.minSolution

        val c = minSolution.cost
        val feasible = { if(minSolution.isFeasible) "YES" else "NO"}

        writer.write(f"Path:$minSolution%s\nEvaluation:$c%2.9f\nFeasible: $feasible%s\n$dur%f\nSeed: $seed%d")
        writer.flush
        writer.close
      }
    }
  }
}

/*
Path: 47,857,180,986,830,747,679,829,935,1073,272,492,178,137,921,505,893,853,2,345,216,839,498,572,493,717,299,786,1032,113,627,200,109,607,117,642,74,521,710,774
Evaluation: 2.826428006495813
Feasible:   NO

Path: 830,117,178,498,857,200,747,679,921,109,1073,607,180,786,627,74,299,1032,774,137,853,642,113,572,710,492,272,521,935,493,893,216,22,717,345,447,839,505,986,829
Evaluation: 0.616235864927380
Feasible:   YES

Path: 830,117,178,498,857,200,747,679,921,109,1073,607,180,786,627,74,299,1032,774,137,853,642,113,572,710,492,272,521,216,893,935,493,505,986,829,839,447,22,717,345
Evaluation: 0.628228629806404
Feasible:   YES

Path: 22,74,109,113,117,137,178,180,200,216,272,299,345,447,492,493,498,505,521,572,607,627,642,679,710,717,747,774,786,829,830,839,853,857,893,921,935,986,1032,1073
Evaluation: 3.365439774982549
Feasible:   NO

Path: 830,117,178,498,857,200,747,679,921,109,1073,607,180,786,627,74,299,1032,774,137,853,642,113,572,710,492,272,521,935,493,893,216,22,717,345,447,839,505,986,829
Evaluation: 0.616235864927380
Feasible:   YES

Path: 829,986,505,839,447,345,717,22,216,893,493,935,521,272,492,710,572,113,642,853,137,774,1032,299,74,627,786,180,607,1073,109,921,679,747,200,857,498,178,117,830
Evaluation: 0.616235864927380
Feasible:   YES

Path: 1007,675,186,190,933,571,795,748,69,470,67,40,735,96,896,492,614,1016,347,203,489,877,393,563,885,159,254,981,919,271,319,256,991,591,413,957,308,213,717,872,587,332,206,219,654,576,447,477,98,355,688,1069,278,611,95,639,233,505,281,986,111,79,486,358,528,893,766,935,257,837,284,705,323,796,741,890,871,414,1034,1080,853,670,697,876,757,297,438,359,12,107,151,44,299,115,74,408,302,385,86,293,627,637,93,10,854,395,607,785,1002,189,690,156,557,341,1037,398,119,94,547,833,234,582,679,535,13,478,243,1057,583,515,410,498,497,310,669,39,178,857,712,517,1072,955,552,454,700,313,865,337,117,830
Evaluation: 0.266416993285296
Feasible:   YES

Path: 10,12,13,39,40,44,67,69,74,79,86,93,94,95,96,98,107,111,115,117,119,151,156,159,178,186,189,190,203,206,213,219,233,234,243,254,256,257,271,278,281,284,293,297,299,302,308,310,313,319,323,332,337,341,347,355,358,359,385,393,395,398,408,410,413,414,438,447,454,470,477,478,486,489,492,497,498,505,515,517,528,535,547,552,557,563,571,576,582,583,587,591,607,611,614,627,637,639,654,669,670,675,679,688,690,697,700,705,712,717,735,741,748,757,766,785,795,796,830,833,837,853,854,857,865,871,872,876,877,885,890,893,896,919,933,935,955,957,981,986,991,1002,1007,1016,1034,1037,1057,1069,1072,1080
Evaluation: 3.104873366109664
Feasible:   NO

Path: 795,748,571,933,190,186,675,12,107,44,151,359,1007,67,69,470,735,40,96,896,492,614,1016,347,203,489,877,393,563,885,159,254,981,919,271,319,256,991,591,413,957,308,213,717,872,587,332,206,219,654,576,447,477,278,233,639,95,611,986,111,79,281,505,486,1069,98,355,688,358,528,893,766,935,257,837,284,705,323,796,741,890,871,1034,1080,853,414,670,697,876,757,297,438,408,74,115,299,293,86,385,302,627,637,93,10,854,395,607,785,1002,189,690,156,557,341,1037,398,119,94,547,833,234,582,679,535,1057,13,478,243,583,515,410,498,669,310,39,178,497,857,712,1072,955,552,454,700,313,517,865,337,117,830
Evaluation: 0.259860115657675
Feasible:   YES

Path: 795,748,571,933,190,44,74,115,299,151,107,12,359,675,186,1007,67,470,69,735,40,96,1016,492,896,203,489,614,347,766,893,563,885,877,393,159,254,981,919,271,319,256,991,591,957,413,308,213,717,872,587,332,206,219,654,576,447,98,688,355,281,486,505,111,79,611,95,639,278,233,986,477,1069,358,528,935,257,284,837,705,323,741,796,890,871,414,853,1080,1034,670,697,876,757,297,438,408,86,385,302,293,627,637,93,10,854,395,785,607,1002,189,690,156,557,341,1037,398,119,94,547,833,234,582,679,1057,535,478,13,243,515,583,410,498,857,712,1072,700,454,313,552,955,517,865,337,117,497,669,310,39,178,830
Evaluation: 0.293078116642905
Feasible:   YES
*/
