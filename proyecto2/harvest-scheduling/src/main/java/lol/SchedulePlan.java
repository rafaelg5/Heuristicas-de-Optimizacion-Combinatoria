package lol;

/**
* Plan o programa para el aprovechamiento de recursos forestales
*/
public class SchedulePlan {

  private ForestUnit[][] plan;
  private int[][] decisions;
  private int units;
  private int periods;

  /**
  * Construye un nuevo plan para agendar con una matriz de unidades forestales
  * y una matriz de variables de decisión
  * @param plan la matriz de unidades forestales
  * @param decisions la matriz de variables de decisión
  */
  public SchedulePlan(ForestUnit[][] plan, int[][] decisions){
    this.plan = plan;
    units = plan[0].length;
    periods = plan.length;
    this.decisions = decisions;
  }

  /**
  * Devuelve la matriz de unidades forestales
  * @return la matriz de unidades forestales
  */
  public ForestUnit[][] getPlan(){
    return this.plan;
  }

  /**
  * Devuelve la matriz de variables de decisión
  * @return la matriz de variables de decisión
  */
  public int[][] getDecisions(){
    return copyDecisions();
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
  * 1. Restricción de singularidad
  * 2. Restricciones de adyacencia
  */

  /**
  * Determina si una unidad en un periodo cumple la restricción de adyacencia
  * @param unit la unidad
  * @param period el periodo
  * @return true si cumple la restricción, false e.o.c.
  */
  public boolean unitMeetsAdjacency(int unit, int period){

    int initialPeriod = period - 2 < 0 ? 0 : period - 2;
    int finalPeriod = period + 2 > periods - 1 ? periods - 1 : period + 2;

    int sum1 = 0;
    if(unit - 1 >= 0)
      for(int t = initialPeriod; t <= finalPeriod; t++){
        sum1 += decisions[t][unit - 1];
      }

    int sum2 = 0;
    if(unit - 25 >= 0)
      for(int t = initialPeriod; t <= finalPeriod; t++){
        sum2 += decisions[t][unit - 25];
      }

    return (sum1 + sum2 + 4*decisions[period][unit]*sum2 + sum1) <= 4;
  }

  /*
  * Copia la matriz de decisiones
  */
  private int[][] copyDecisions(){

    int[][] newArray = new int[periods][units];

    for(int t = 0; t < periods; t++)
      for(int i = 0; i < units; i++)
        newArray[t][i] = decisions[t][i];

    return newArray;
  }

  /**
  * Calcula la función objetivo que queremos maximizar. Las variables a tomar en
  * cuenta son: ingreso (por unidad y periodo), costo de tala (por unidad y
  * periodo), volumen de madera (por unidad y periodo) y
  * la variable de decisión (por unidad y periodo). Las unidades del valor son:
  * $ / ha
  * @return el valor de la evaluación
  */
  public double objective() {

    double total = 0.0;

    for(int t = 0; t < periods; t++) {

      double sum = 0.0;

      for(int i = 0; i < units; i++) {

        /* *** variables del plan *** */
        ForestUnit fUnit = plan[t][i];
        double rev = fUnit.getRevenue();
        double lc = fUnit.getLoggingCost();
        int v = fUnit.getTimberVolume();
        int x = decisions[t][i];
        /* ************************* */

        if((rev - lc) > 0 && x == 1)
          sum += (rev - lc) * v;
      }

      total += sum;
    }
    return total;
  }

  /**
  * Calcula el ingreso neto de una unidad en un periodo determinado
  * @param unit la unidad
  * @param period el periodo
  * @return el ingreso neto
  */
  public double unitNetRevenue(int unit, int period) {

      /* *** variables del plan *** */
      ForestUnit fUnit = plan[period][unit];
      double rev = fUnit.getRevenue();
      double lc = fUnit.getLoggingCost();
      int v = fUnit.getTimberVolume();
      /* ************************* */

    return (rev - lc) * v;
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

  /*
  * Obtiene la edad de la unidad en el periodo de tiempo que se encuentra
  */
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
