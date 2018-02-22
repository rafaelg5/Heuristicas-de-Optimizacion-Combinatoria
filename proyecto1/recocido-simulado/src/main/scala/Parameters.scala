object Parameters {

  private val src = scala.io.Source.fromFile("src/etc/init_config")
  private val lines = src.getLines.toArray
  src.close

  var initTemp = lines(0).substring(lines(0).indexOf('=') + 1).toDouble
  var batchSize = lines(1).substring(lines(1).indexOf('=') + 1).toInt
  var percentage = lines(2).substring(lines(2).indexOf('=') + 1).toDouble
  var seed = lines(3).substring(lines(3).indexOf('=') + 1).toInt
  var instance1 = lines(4).substring(lines(4).indexOf('=') + 1).split(',').map(_.toInt)
  var instance2 = lines(5).substring(lines(5).indexOf('=') + 1).split(',').map(_.toInt)

}
