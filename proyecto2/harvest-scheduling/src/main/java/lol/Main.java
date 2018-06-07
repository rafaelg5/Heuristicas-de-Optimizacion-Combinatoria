package lol;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) {

    Random rng = new Random();

    // Matriz de las unidades del bosque
    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;

    int initial = 196959867;
    /*
    do {
      initial = rng.nextInt();
    } while(initial + Parameters.SEED_NUM >= Integer.MAX_VALUE);*/

    // Grupo de hilos
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    for (int i = initial; i < initial + Parameters.SEED_NUM; i++) {
      Runnable hs = new HarvestScheduling(iS, i);
      executor.execute(hs);
    }
    executor.shutdown();

  }
}
