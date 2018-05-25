package lol;
import java.util.Random;

public class Main {
  public static void main(String[] args) {

    // Matriz de las unidades del bosque
    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;

    int[][] iD = Parameters.DECISIONS;

    SchedulePlan schedule = new SchedulePlan(iS, iD);

    System.out.printf("Inicial: %.2f\n", schedule.objective());

    SchedulePlan bestSolution = TabuSearch.run(Parameters.ITERATIONS, schedule);

    System.out.printf("Mejor solución: %.2f\n", bestSolution.objective());

  }
}
