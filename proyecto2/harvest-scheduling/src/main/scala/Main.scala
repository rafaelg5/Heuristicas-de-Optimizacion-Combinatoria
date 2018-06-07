import java.util.concurrent.{Executors, ExecutorService}

object Main {


  val rng = scala.util.Random

  // Matriz de las unidades del bosque
  val iS = Parameters.InitialSolution

  var initial = 196959867
  /*
  do {
    initial = rng.nextInt
  } while(initial + Parameters.SeedNum >= Integer.MAX_VALUE);*/

  // Grupo de hilos
  val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)
  for(i <- initial until initial + Parameters.SeedNum) {
    val hs = new HarvestScheduling(iS, i)
    pool.execute(hs)
  }
  pool.shutdown();
}
