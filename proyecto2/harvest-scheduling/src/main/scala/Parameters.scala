import scala.util.Random

object Parameters {

  val SeedNum = 1
  private val rng = new Random(5)

  val TimberVolume = Array(190, 215, 233, 261, 278, 311, 337, 363, 390, 417,
    443, 470, 499, 527, 554, 581, 607, 633, 658, 682, 706, 748, 771, 794, 818, 842)
  // La raíz cuadrada de las unidades debe ser un entero. Ya que representa un
  // tablero de n x n
  val Units = 625
  val Periods = 10
  // Volumen deseado de madera (m^3 / ha) por periodo
  val VolumeGoal = 20000
  val Iterations = 2730
  val InitialSolution = initSol

  private def initSol: Array[Array[ForestUnit]] = {
    // Genera las unidades con sus valores iniciales del bosque del primer periodo
    val forest = new Array[ForestUnit](Units)

    for (i <- 0 until Units) {
      // Costo de tala por metro cúbico entre $20.00 y $70.00
      var totalVal = 20 + rng.nextInt(51) + rng.nextDouble
      if(totalVal > 70) totalVal = 70.0
      else {
        val bd = BigDecimal(totalVal)
        totalVal = bd.setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
      }
      // Valor de la edad inicial de la unidad entre 15 y 30 años
      forest(i) = new ForestUnit(i+1,1, 15 + rng.nextInt(16), totalVal)
    }

    // Solución inicial para el problema
    val solution = Array.ofDim[ForestUnit](Periods, Units)
    solution(0) = forest

    // Genera los valores para los periodos restantes
    for (i <- 0 until Periods;
         j <- 0 until Units) {

        val fu = solution(0)(j).copyAndUpdate(i+1)
        solution(i)(j) = fu
    }


    return solution
  }

}
