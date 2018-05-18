package lol;

import java.util.LinkedList;

public class TabuSearch {/*

  private static ForestUnit[][] copyPlan(ForestUnit[][] arr){

    ForestUnit[][] newArray = new ForestUnit[arr.length][arr[0].length];

    for(int i=0; i<arr.length; i++)
      for(int j=0; j<arr[i].length; j++)
        newArray[i][j]= arr[i][j].copy();

    return newArray;
  }

  /**
  *
  *
  public static SchedulePlan run(int iterations, SchedulePlan plan) {

    SchedulePlan sBest = plan;
    int maxTabuSize = 15;
    SchedulePlan bestCandidate = plan;
    int i = 0;

    LinkedList<SchedulePlan> tabuList = new LinkedList<>();
    tabuList.add(sBest);

    while(i < iterations){

      val sNeighborhood = bestCandidate.neighborhood
      bestCandidate = sNeighborhood(0)
      for(sCandidate <- sNeighborhood) {

        if(sCandidate.meetsSingularity && sCandidate.meetsAdjacency){
          if(!tabuList.contains(sCandidate) &&
            sCandidate.objective > bestCandidate.objective){
              bestCandidate = new SchedulePlan(sCandidate.table)
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
  }*/
}
