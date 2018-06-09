package lol;

/**
* Plan o programa para el aprovechamiento de recursos forestales
*/
public class SchedulePlan {

  private ForestUnit[][] plan;
  private int[][] decisions;
  private int units;
  private int periods;

  private final String COLOR1 = "rgb(153, 102, 0)";
  private final String COLOR2 = "rgb(204, 153, 0)";
  private final String COLOR3 = "rgb(204, 204, 0)";
  private final String COLOR4 = "rgb(153, 204, 0)";
  private final String COLOR5 = "rgb(0, 153, 0)";
  private final String COLOR6 = "rgb(102, 153, 0)";
  private final String COLOR7 = "rgb(51, 102, 0)";
  private final String COLOR8 = "rgb(0, 51, 0)";

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

    boolean cond1 = (sum1 + sum2 + 4*decisions[period][unit]*sum2 + sum1) <= 4;
    boolean cond2 = plan[period][unit].getAge() >= 19;

    return cond1 && cond2;
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

  public int getUnitDecisionIndex(int unit) {
    for(int t = 0; t < periods; t++){
      if(decisions[t][unit] == 1) return t;
    }
    return -1;
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

    for(int i = 0; i < units; i++){
      int t = getUnitDecisionIndex(i);
      if(t == -1)
        continue;
      total += unitNetRevenue(i, t);
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

  public String toSVG(){

    int height = 50;
    int width = 50;

    String iPeriod =
      String.format("\n<text x='%d' y='%d' fill='%s'>Periodo inicial</text>\n",
      0, 15, "black");

    String iRects = "";

    int t = 0;

    for(int i = 0; i < units; i++){
      ForestUnit unit = plan[t][i];
      int age = getUnitCurrentAge(unit);

      String unitColor = "";
      if(age <= 4){
        unitColor = COLOR1;
      } else if(age <= 9){
        unitColor = COLOR2;
      } else if(age <= 14){
        unitColor = COLOR3;
      } else if(age <= 19){
        unitColor = COLOR4;
      } else if(age <= 24){
        unitColor = COLOR5;
      } else if(age <= 29){
        unitColor = COLOR6;
      } else if(age <= 34){
        unitColor = COLOR7;
      } else {
        unitColor = COLOR8;
      }

      int x = (i % (int)Math.sqrt(units)) * width;
      int y = 30 + ((i / (int)Math.sqrt(units)) * 50);

      iRects += String.format("<rect x='%d' y='%d' width='%d' height='%d' style=\"fill:%s; stroke-width:1; stroke:rgb(0,0,0)\" />\n",
                            x, y, width, height, unitColor);
    }

    int newSY = (50 * (int)Math.sqrt(units)) + 45;
    String fPeriod =
      String.format("\n<text x='%d' y='%d' fill='%s'>Periodo final</text>\n",
      0, newSY , "black");

    String fRects = "";

    t = 9;

    for(int i = 0; i < units; i++){
      ForestUnit unit = plan[t][i];
      int age = getUnitCurrentAge(unit);

      String unitColor = "";
      if(age <= 4){
        unitColor = COLOR1;
      } else if(age <= 9){
        unitColor = COLOR2;
      } else if(age <= 14){
        unitColor = COLOR3;
      } else if(age <= 19){
        unitColor = COLOR4;
      } else if(age <= 24){
        unitColor = COLOR5;
      } else if(age <= 29){
        unitColor = COLOR6;
      } else if(age <= 34){
        unitColor = COLOR7;
      } else {
        unitColor = COLOR8;
      }

      int x = (i % (int)Math.sqrt(units)) * width;
      int y = 30 + ((i / (int)Math.sqrt(units)) * 50) + newSY;

      fRects += String.format("<rect x='%d' y='%d' width='%d' height='%d' style=\"fill:%s; stroke-width:1; stroke:rgb(0,0,0)\" />\n",
                            x, y, width, height, unitColor);
    }

    int sX = 0;
    newSY += (50 * (int)Math.sqrt(units)) + 45 + 100;

    String classes = "";
    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR1,COLOR1);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>0-4 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR2, COLOR2);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>5-9 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR3, COLOR3);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>10-14 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR4, COLOR4);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>15-19 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR5, COLOR5);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>20-24 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR6, COLOR6);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>25-29 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR7, COLOR7);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>30-34 años</text>\n",sX+25,newSY+15);

    sX += 110;

    classes += String.format("\n<rect x='%d' y='%d' width='20' height='20' style=\"fill:%s;stroke-width:1;stroke:%s\"/>\n",sX,newSY,COLOR8, COLOR8);
    classes += String.format("\n<text x='%d' y='%d' fill='black'>35-40 años</text>\n",sX+25,newSY+15);


    String svg = "<!DOCTYPE html>\n<html>\n<body>\n<svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' width='"+(50 * (int)Math.sqrt(units))+
                 "' height='2710'>" +
  	             "\n<g>"+ iPeriod + iRects +"\n</g>\n" +
                 "\n<g>"+ fPeriod + fRects +"\n</g>\n" +
                 "\n<g>" + classes + "\n</g>" +
                 "\n</svg>\n</body>\n</html>";

    return svg;

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
