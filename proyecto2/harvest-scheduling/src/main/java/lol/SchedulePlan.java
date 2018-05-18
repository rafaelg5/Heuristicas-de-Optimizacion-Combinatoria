package lol;

import java.util.Random;
import java.util.LinkedList;

/**
* Plan o programa para el aprovechamiento de recursos forestales
*/
public class SchedulePlan {

  private ForestUnit[][] plan;
  private int[][] decisions;
  private int units;
  private int periods;

  public SchedulePlan(ForestUnit[][] plan, int[][] decisions){
    this.plan = plan;
    units = plan[0].length;
    periods = plan.length;
    this.decisions = decisions;
  }

  public ForestUnit[][] getPlan(){
    return this.plan;
  }

  public int[][] getDecisions(){
    return this.decisions;
  }
  /*
  * Función objetivo:
  * Max ΣΣ (Rev_it - Lc_it) * X_it * V_it
  * donde
  * i = unidad
  * t = periodo
  * Rev_it = ingreso por metro cúbico para la unidad i talada en el periodo t
  * Lc_it = costo de tala por metro cúbico para la unidad i talada en el periodo t
  * V_it = volumen por hectarea para la unidad i talada en el periodo t
  * X_it = variable de decisión (0,1) donde la unidad i es talada en el periodo t
  * Vp_t = penalización de volumen por metro cúbico en el periodo t
  * d_t = desviación del volumen deseado en el periodo t en metros cúbicos
  *
  */

  /* Restricciones:
  * 1. Restricción de volumen
  * 2. Restricción de singularidad
  * 3. Restricciones de adyacencia
  */

  public boolean meetsAdjacency(){
    return true;
  }

  /**
  * Revisa que las unidades cumplan la restricción de singularidad.
  * @return true si cumple la restricción, false e.o.c
  */
  public boolean meetsSingularity() {

    for (int i = 0; i < units; i++) {
      int total = 0;
      for(int j = 0; j < periods; j++)
        total += decisions[j][i];

      if(total > 1)
        return false;
    }
    return true;
  }

  /* Obtiene el índice del periodo en el cual cierta unidad es talada.
  * Si nunca se taló, devuelve -1
  */
  private int getDecisionIndex(int unit){

      for(int t = 0; t < periods; t++){
        if(decisions[t][unit] == 1)
          return t;
      }

    return -1;
  }


  private int[][] copyDecisions(int[][] arr){

    int[][] newArray = new int[arr.length][arr[0].length];

    for(int i=0; i<arr.length; i++)
      for(int j=0; j<arr[i].length; j++)
        newArray[i][j]= arr[i][j];

    return newArray;
  }

  public void setDecision(int unit, int period, int value) {
    decisions[period][unit] = value;
  }

  /**
  * Devuelve la vecindad (todas las posibles soluciones vecinas) del plan
  * @return las soluciones vecinas
  */
  public LinkedList<int[][]> neighborhood(){

    LinkedList<int[][]> neighbors = new LinkedList<>();

    for(int i = 0; i < units; i++){

      int period = getDecisionIndex(i);
      if(period != - 1) {
        int[][] newDec = copyDecisions(decisions);
        newDec[period][i] = 0;
        neighbors.add(newDec);
      }

      for(int t = 0; t < periods; t++){

        if(t != period) {
          int[][] newDec = copyDecisions(decisions);
          newDec[t][i] = 1;
          newDec[period][i] = 0;
          neighbors.add(newDec);
        }
      }
    }
    return neighbors;
  }

  /**
  * Calcula la desviación estandar del volumen en un periodo de tiempo
  * @param period el periodo
  * @return la desviación del volumen deseado
  */
  public double deviation(int period){

    double mean = 0.0;

    for(int i = 0; i < units; i++){
      ForestUnit fUnit = plan[period][i];
      int dec = decisions[period][i];
      mean += fUnit.getTimberVolume() * dec;
    }
    mean /= units;

    double variance = 0.0;

    for(int i = 0; i < units; i++){
      ForestUnit fUnit = plan[period][i];
      int dec = decisions[period][i];
      variance += Math.pow((fUnit.getTimberVolume() - mean)*dec, 2);
    }
    return Math.sqrt(variance / units);
  }

  /**
  * Calcula la función objetivo que queremos maximizar. Las variables a tomar en
  * cuenta son: ingreso (por unidad y periodo), costo de tala (por unidad y
  * periodo), volumen de madera (por unidad y periodo), variable de decisión
  * (por unidad y periodo), penalización del volumen (por periodo), desviación
  * del volumen deseado (por periodo).
  * @return el valor de la evaluación
  */
  public double objective() {

    double sum = 0.0;

    for(int t = 0; t < periods; t++) {

      double dev = deviation(t);
      double penalty = ForestUnit.discountedPresentValue(100, 9, t+1);

      for(int i = 0; i < units; i++) {

        /* *** variables del plan *** */
        ForestUnit fUnit = plan[t][i];
        double rev = fUnit.getRevenue();
        double lc = fUnit.getLoggingCost();
        int v = fUnit.getTimberVolume();
        int x = decisions[t][i];
        /* ***************** */

        if((rev - lc) > 0 && x == 1)
          sum += (rev - lc) * v;
      }

      sum -= penalty * dev;
    }
    return sum;
  }

  @Override
  public String toString(){
    String s = "";
    for(int t = 0; t < periods; t++){
      s += String.format("\nPeriodo %d\n", t + 1);

      for(int c = 0; c < (int)Math.sqrt(units); c++)
        s += "+--";

      s+= "+\n";
      for(int i = 0; i< units; i++){
        ForestUnit unit = plan[t][i];
        String age = getUnitCurrentAge(unit) + "";

        if(age.length() == 1)
          age = "0" + age;

        s += String.format("|%s", age);
        if(unit.getIndex() % (int)Math.sqrt(units) == 0 && unit.getIndex() != 0) {
          s += "|\n";
          for(int c = 0; c < (int)Math.sqrt(units); c++)
            s += "+--";
          s+= "+\n";
        }
      }
    }
    return s;
  }

  private int getUnitCurrentAge(ForestUnit u) {

    int i = -1;
    for(int t = 0; t < periods; t++){
      if(decisions[t][u.getIndex()-1] == 1)
        i = t;
    }
    if(i == -1)
      return u.getAge();
    if(i > u.getPeriod() - 1)
      return u.getAge();
    if(i == u.getPeriod() - 1)
      return 0;
    return u.getPeriod() -1 - i;
  }

}
