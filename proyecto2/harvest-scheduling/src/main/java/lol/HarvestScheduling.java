package lol;

import java.util.Random;

public class HarvestScheduling implements Runnable {

  private ForestUnit[][] plan;
  private int seed;
  private Random rng;

  public HarvestScheduling(ForestUnit[][] initialSolution, int seed){
    plan = initialSolution;
    this.seed = seed;
    rng = new Random(seed);
  }

  public void run(){
    long startTime = System.nanoTime();
    int[][] dec = new int[Parameters.PERIODS][Parameters.UNITS];

    /* Asignar las variables de decisión para que cumplan la restricción de
       singularidad */
    for (int i = 0; i < Parameters.UNITS; i++) {
      int pos = rng.nextInt(Parameters.PERIODS);
      float prob = rng.nextFloat();
      if(prob < 0.5)
        dec[pos][i] = 1;
    }

    SchedulePlan schedule = new SchedulePlan(plan, dec);
    System.out.printf("Inicial (%d): %.2f\n", seed, schedule.objective());

    SchedulePlan bestSolution = TabuSearch.run(Parameters.ITERATIONS, schedule, seed);

    System.out.printf("Mejor solución (%d): %.2f\n", seed, bestSolution.objective());
    long endTime = System.nanoTime();
    long totalTime = endTime - startTime;
    System.out.printf("Tiempo total (%d): %.2f\n", seed, totalTime / 1000000000.0);
  }

}
