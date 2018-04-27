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
      val sNeighborhood = bestCandidate.getNeighbors
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
  /*var bestSolution = plan
  var currentSolution = plan

  def run(){
  for(shock <- 1 to Parameters.shock){
  for(i <- 1 to iterations){
  for(unit <- 0 until units){
  for(period <- 0 until periods){
  if(plan.unitMeetsAdjacency(unit)) {
  if(plan.cost > bestSolution.cost)

}
}
}
}
}
}*/
}
