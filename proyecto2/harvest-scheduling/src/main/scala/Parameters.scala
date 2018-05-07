object Parameters {

  val rng = scala.util.Random
  val timberVolume = Array(190, 215, 233, 261, 278, 311, 337, 363, 390, 417, 443,
    470, 499, 527, 554, 581, 607, 633, 658, 682, 706, 748, 771, 794, 818, 842)
  // La raíz cuadrada de las unidades debe ser un entero. Ya que representa un
  // tablero de n x n
  private val units = 625
  private val periods = 10

  val initial_solution = Array.ofDim[Cell](periods,units)

  for (i <- 0 until periods; j <- 0 until units) {
    initial_solution(i)(j) = new Cell(2,2,2,2)
  }

  val shock = 6
}
