package lol;

import java.util.LinkedList;

public class TabuSearch {

  /**
  *
  */
  public static SchedulePlan run(int iterations, SchedulePlan plan) {

    SchedulePlan sBest = plan;
    int maxTabuSize = 15;
    SchedulePlan bestCandidate = sBest;
    int i = 0;

    LinkedList<SchedulePlan> tabuList = new LinkedList<>();
    tabuList.add(sBest);

    while(i < iterations){

      LinkedList<int[][]> sNeighborhood = bestCandidate.neighborhood();
      bestCandidate = new SchedulePlan(plan.getPlan(), sNeighborhood.get(0));
      for(int[][] sCandidate : sNeighborhood) {
        SchedulePlan aux = new SchedulePlan(plan.getPlan(), sCandidate);
        if(aux.meetsAdjacency()){
          if(!tabuList.contains(aux) &&
            aux.objective() > bestCandidate.objective()){
              bestCandidate = aux;
          }
        }
      }
      if(bestCandidate.objective() > sBest.objective()) {
        sBest = bestCandidate;
      }
      tabuList.add(bestCandidate);
      if(tabuList.size() > maxTabuSize)
        tabuList.remove(0);
      i++;
    }
    return sBest;
  }
}
