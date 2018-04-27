object Parameters {

  val rng = scala.util.Random
  val timberVolume = Array(190, 215, 233, 261, 278, 311, 337, 363, 390, 417, 443,
    470, 499, 527, 554, 581, 607, 633, 658, 682, 706, 748, 771, 794, 818, 842)
  val units = 256
  val periods = 10

  val adjacencies = Array.ofDim[Int](units,units)
  val round:(Double => Long) = scala.math.round _
  for (i <- 0 until units; j <- 0 until units) {
    if(i != j) adjacencies(i)(j) = round(rng.nextDouble).toInt
  }

  val lowerBound = new Array[Double](periods)
  val upperBound = new Array[Double](periods)

  val shock = 6
}
