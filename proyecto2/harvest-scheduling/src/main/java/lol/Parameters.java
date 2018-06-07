package lol;

import java.util.Random;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Parameters {

  public static final int SEED_NUM = 1;
  private static final Random RNG0 = new Random(5);
  //public static final int SEED = 196959867;
  public static final int[] TIMBER_VOLUME = {190, 215, 233, 261, 278, 311, 337,
    363, 390, 417, 443, 470, 499, 527, 554, 581, 607, 633, 658, 682, 706, 748,
    771, 794, 818, 842};
  // La raíz cuadrada de las unidades debe ser un entero. Ya que representa un
  // tablero de n x n
  public static final int UNITS = 625;
  public static final int PERIODS = 10;
  // Volumen deseado de madera (m^3 / ha) por periodo
  public static final int VOLUME_GOAL = 20000;
  public static final int ITERATIONS = 2730;
  public static final ForestUnit[][] INITIAL_SOLUTION = initSol();

  private static ForestUnit[][] initSol(){
    // Genera las unidades con sus valores iniciales del bosque del primer periodo
    ForestUnit[] forest = new ForestUnit[UNITS];
    for (int i = 0; i < UNITS; i++){

      // Costo de tala por metro cúbico entre $20.00 y $70.00
      double totalVal = (20 + RNG0.nextInt(51)) + RNG0.nextDouble();
      if(totalVal > 70)
        totalVal = 70.0;
      else {
        BigDecimal bd = new BigDecimal(totalVal);
        totalVal = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      // Valor de la edad inicial de la unidad entre 15 y 30 años
      forest[i] = new ForestUnit(i+1,1, 15 + RNG0.nextInt(16), totalVal);
    }

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

}
