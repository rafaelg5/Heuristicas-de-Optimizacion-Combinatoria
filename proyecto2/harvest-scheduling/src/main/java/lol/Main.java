package lol;
import java.util.Random;

public class Main {
  public static void main(String[] args) {

    // Matriz de las unidades del bosque
    long startTime = System.nanoTime();
    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;

    int[][] iD = Parameters.DECISIONS;

    SchedulePlan schedule = new SchedulePlan(iS, iD);
    System.out.println("Semilla: " + Parameters.SEED);
    //System.out.println();
    //System.out.println(schedule);
    System.out.printf("Inicial: %.2f\n", schedule.objective());

    SchedulePlan bestSolution = TabuSearch.run(Parameters.ITERATIONS, schedule);

    //System.out.println(bestSolution);
    System.out.printf("Mejor solución: %.2f\n", bestSolution.objective());
    long endTime = System.nanoTime();
    long totalTime = endTime - startTime;
    System.out.printf("Tiempo total: %.2f\n", totalTime / 1000000000.0);
  }
}
