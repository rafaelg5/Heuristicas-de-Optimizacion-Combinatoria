package lol;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) {

    Random rng = new Random();

    // Matriz de las unidades del bosque
    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;

    int seedNum = 0;

    if(args.length == 0)
      seedNum = Parameters.SEED_NUM;
    else if(args.length == 1 || args.length == 2)
      seedNum = Integer.parseInt(args[0]);

    int initial = 0;
    if(args.length == 2)
      initial = Integer.parseInt(args[1]);
    else {
      do {
        initial = rng.nextInt();
      } while(initial + seedNum >= Integer.MAX_VALUE);
    }

    // Grupo de hilos
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    for (int i = initial; i < initial + seedNum; i++) {
      Runnable hs = new HarvestScheduling(iS, i);
      executor.execute(hs);
    }
    executor.shutdown();

  }
}
