object Main extends App {

  val src = scala.io.Source.fromFile("src/etc/init_config")
  val lines = src.getLines.toArray
  src.close

  val initTemp = lines(0).substring(lines(0).indexOf('=') + 1).toDouble
  val batchSize = lines(1).substring(lines(1).indexOf('=') + 1).toInt
  val percentage = lines(2).substring(lines(2).indexOf('=') + 1).toDouble
  val seed = lines(3).substring(lines(3).indexOf('=') + 1).toInt
  val instance1 = lines(4).substring(lines(4).indexOf('=') + 1).split(',').map(_.toInt)
  val instance2 = lines(5).substring(lines(5).indexOf('=') + 1).split(',').map(_.toInt)

  val rng = scala.util.Random
  rng.setSeed(seed)

  val initSolution = rng.shuffle(instance1.toSeq)
  var solution = new Solution(initSolution.toArray, rng)

  val t = new Temperature(instance1.length)
  //var realInitTemp = t.initTemp(solution, initTemp, percentage)

  //new Threshold(batchSize, solution).acceptByThresholds(realInitTemp, solution)


}
