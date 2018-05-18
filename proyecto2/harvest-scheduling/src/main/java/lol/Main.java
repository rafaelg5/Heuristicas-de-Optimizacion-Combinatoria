package lol;
import java.util.Random;

public class Main {
  public static void main(String[] args) {

    Random rng = new Random();

    ForestUnit[][] iS = Parameters.INITIAL_SOLUTION;

    SchedulePlan schedule = new SchedulePlan(iS);

    System.out.println(schedule.objective());
    schedule.neighborhood();
    System.out.println(schedule.objective());
    //val bestSolution = TabuSearch.run(1, schedule)*/

  }
}
