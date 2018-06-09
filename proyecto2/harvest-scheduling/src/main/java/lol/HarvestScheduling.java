package lol;

import java.util.Random;
import java.io.*;
import scala.io.Source;

public class HarvestScheduling implements Runnable {

  private ForestUnit[][] plan;
  private int seed;
  private Random rng;

  public HarvestScheduling(ForestUnit[][] initialSolution, int seed){
    plan = initialSolution;
    this.seed = seed;
    rng = new Random(seed);
  }

  public void run() {

    PrintWriter writer = null, writer2 = null;
    try {
      writer =
        new PrintWriter(new File(String.format("src/etc/solutions/%d", seed)));
      writer2 =
        new PrintWriter(new File(String.format("src/etc/solutions/%d.svg", seed)));
    } catch(FileNotFoundException e){}

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

    SchedulePlan bestSolution = TabuSearch.run(Parameters.ITERATIONS, schedule, seed);

    long endTime = System.nanoTime();
    double totalTime = (endTime - startTime) / 1000000000.0;

    String l1 = String.format("Solución inicial: %.2f", schedule.objective());
    String l2 = String.format("Mejor solución: %.2f", bestSolution.objective());
    String l3 = String.format("Semilla: %d", seed);
    String l4 = String.format("Tiempo ejecución: %.2f", totalTime);

    writer.println(l1);
    writer.println(l2);
    writer.println(l3);
    writer.println(l4);

    writer2.println(bestSolution.toSVG());

    writer.flush();
    writer.close();
    writer2.flush();
    writer2.close();
  }

}
