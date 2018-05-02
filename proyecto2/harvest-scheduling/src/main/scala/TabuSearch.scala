object TabuSearch {

  /**
  *
  */
  def run(iterations: Int, plan: SchedulePlan): SchedulePlan = {

    var sBest = plan
    val maxTabuSize = 15
    var bestCandidate = plan
    var i = 0

    var tabuList = scala.collection.mutable.ArrayBuffer.empty[SchedulePlan]
    tabuList += plan

    while(i < iterations){
      val sNeighborhood = bestCandidate.getNeighborPlans
      bestCandidate = sNeighborhood(0)
      for(sCandidate <- sNeighborhood) {
        if(tabuList.contains(sCandidate) && sCandidate.cost > bestCandidate.cost){
          bestCandidate = sCandidate
        }
      }
      if(bestCandidate.cost > sBest.cost) sBest = bestCandidate
      tabuList += bestCandidate
      if(tabuList.size > maxTabuSize) tabuList.remove(0)
      i += 1
    }
    sBest
  }
}
