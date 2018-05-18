package lol;
import java.util.Random;

public class Main {
  public static void main(String[] args) {

    Random rng = new Random();

    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;
    int[][] iD = Parameters.DECISIONS;

    SchedulePlan schedule = new SchedulePlan(iS, iD);

    System.out.println(schedule.objective());
    SchedulePlan bestSolution = TabuSearch.run(Parameters.ITERATIONS, schedule);
    System.out.println(bestSolution.objective());
    System.out.println(bestSolution);
  }
}
