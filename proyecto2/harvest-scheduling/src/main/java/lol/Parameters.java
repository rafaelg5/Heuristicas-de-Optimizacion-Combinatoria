package lol;
import java.util.Random;

public class Parameters {

  private static final Random RNG = new Random();
  public static final int[] TIMBER_VOLUME = {190, 215, 233, 261, 278, 311, 337,
    363, 390, 417, 443, 470, 499, 527, 554, 581, 607, 633, 658, 682, 706, 748,
    771, 794, 818, 842};
  // La raíz cuadrada de las unidades debe ser un entero. Ya que representa un
  // tablero de n x n
  public static final int UNITS = 625;
  public static final int PERIODS = 10;
  // Volumen deseado de madera (m^3 / ha) por periodo
  public static final int volumeGoal = 25000;
  public static final ForestUnit[][] INITIAL_SOLUTION = initSol();
  public static final int[][] DECISIONS = initDec();

  private static ForestUnit[][] initSol(){
    // Genera las unidades con sus valores iniciales del bosque del primer periodo
    ForestUnit[] forest = new ForestUnit[UNITS];
    for (int i = 0; i < UNITS; i++)
      forest[i] = new ForestUnit(i+1,1);

    // Solución inicial para el problema
    ForestUnit[][] solution = new ForestUnit[PERIODS][UNITS];
    solution[0] = forest;

    // Genera los valores para los periodos restantes
    for (int i = 0; i < PERIODS; i++) {
      for (int j = 0; j < UNITS; j++) {
        ForestUnit fu = solution[0][j].copyAndUpdate(i+1);
        solution[i][j] = fu;
      }
    }

    return solution;
  }

  private static int[][] initDec(){
    int[][] dec = new int[PERIODS][UNITS];

    // Asignar las variables de decisión para que cumplan la restricción de
    // singularidad
    for (int i = 0; i < UNITS; i++) {
      int pos = RNG.nextInt(PERIODS);
      dec[pos][i] = 1;
    }
    return dec;
  }

}
