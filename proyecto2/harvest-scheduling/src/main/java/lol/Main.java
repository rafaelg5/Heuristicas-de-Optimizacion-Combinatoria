package lol;
import java.util.Random;

public class Main {
  public static void main(String[] args) {

    Random rng = new Random();

    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;
    int[][] iD = Parameters.DECISIONS;

    SchedulePlan schedule = new SchedulePlan(iS, iD);

    System.out.println(schedule.objective());
    schedule.neighborhood();
    SchedulePlan bestSolution = TabuSearch.run(10, schedule);
    System.out.println(bestSolution.objective());
  }
}
