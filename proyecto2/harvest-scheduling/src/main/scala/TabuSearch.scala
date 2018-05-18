object TabuSearch {

  /**
  *
  */
  def run(iterations: Int, plan: SchedulePlan): SchedulePlan = {

    var sBest = new SchedulePlan(plan.table)
    val maxTabuSize = 15
    var bestCandidate = new SchedulePlan(plan.table)
    var i = 0

    var tabuList = scala.collection.mutable.ArrayBuffer.empty[SchedulePlan]
    tabuList += sBest

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
      if(bestCandidate.objective > sBest.objective) {
        sBest = new SchedulePlan(bestCandidate.table)
      }
      tabuList += bestCandidate
      if(tabuList.size > maxTabuSize) tabuList.remove(0)
      i += 1
    }
    sBest
  }
}
