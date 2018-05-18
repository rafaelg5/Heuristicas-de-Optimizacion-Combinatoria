object Parameters {

  val rng = scala.util.Random
  val timberVolume = Array(190, 215, 233, 261, 278, 311, 337, 363, 390, 417, 443,
    470, 499, 527, 554, 581, 607, 633, 658, 682, 706, 748, 771, 794, 818, 842)
  // La raíz cuadrada de las unidades debe ser un entero. Ya que representa un
  // tablero de n x n
  val units = 625
  val periods = 10

  /** TO DO:
  * Generar unidades tal que cumplan la restricción de singularidad
  * Decidir si poner edad 0 si X_ij = 0
  */

  // Genera las unidades con sus valores iniciales del bosque del primer periodo
  val forest = new Array[ForestUnit](units)
  for (i <- 0 until units) {
    forest(i) = new ForestUnit(i+1,1,0)
  }

  // Solución inicial para el problema
  val initialSolution = Array.ofDim[ForestUnit](periods,units)
  initialSolution(0) = forest

  // Genera los valores para los periodos restantes
  for (i <- 1 until periods; j <- 0 until units) {
    val fu = initialSolution(0)(j).copyAndUpdate(i+1)
    initialSolution(i)(j) = fu
  }

  // Asignar las variables de decisión para que cumplan la restricción de
  // singularidad
  for (i <- 0 until units) {
    val pos = rng.nextInt(periods)
    initialSolution(pos)(i).x_(1)
  }

  // Volumen deseado de madera (m^3 / ha) por periodo
  val volumeGoal = 25000

  val shock = 6
}
