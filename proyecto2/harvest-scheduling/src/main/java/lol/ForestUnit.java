package lol;

import java.math.*;
import java.util.Random;

public class ForestUnit{

  private int index;
  private int period;
  /* Variable de decisión 0, 1 donde la unidad con índice 'index' es talada
  * en el periodo 'period'
  */
  private int x;
  // Edad promedio inicial de la unidad
  private int initialAge;
  // Edad promedio de los árboles en la unidad forestal
  private int age;
  // Volumen (de madera) por hectárea de la unidad
  private int timberVolume;
  // Costo de tala por metro cúbico
  private double loggingCost;
  private final Random rng = new Random();
  // Superficie en hectáreas
  private final int SURFACE = 30;
  // Precio de madera por metro cúbico
  private final double LOG_PRICE = 100.0;
  // Ingreso por metro cúbico aplicando una tasa de interés del 8%
  private double revenue;

  public ForestUnit(int index, int period, int decision, int unitAge, double loggingC){
    initialize(index,period,decision);
    initialAge = unitAge;
    age = initialAge + period - 1;
    if(age < 15)
      timberVolume = 150;
    else
      timberVolume = Parameters.TIMBER_VOLUME[age - 15];
    loggingCost = loggingC;
  }

  public ForestUnit(int index, int period, int decision, int unitAge){
    initialize(index,period,decision);
    initialAge = unitAge;
    age = initialAge + period - 1;
    if(age < 15)
      timberVolume = 150;
    else
      timberVolume = Parameters.TIMBER_VOLUME[age - 15];
    loggingCost = calcLoggingCost();
  }

  public ForestUnit(int index, int period, int decision){
    initialize(index,period,decision);
    initialAge = calcInitialAge();
    age = initialAge + period - 1;
    if(age < 15)
      timberVolume = 150;
    else
      timberVolume = Parameters.TIMBER_VOLUME[age - 15];
    loggingCost = calcLoggingCost();
  }

  private void initialize(int index, int period, int decision){
    this.index = index;
    this.period = period;
    x = decision;
    revenue = discountedPresentValue(LOG_PRICE, 8, period);
  }

  private double calcLoggingCost(){
    double totalVal = (20 + rng.nextInt(51)) + rng.nextDouble();
    if(totalVal > 70)
      return 70.0;
    else {
      BigDecimal bd = new BigDecimal(totalVal);
      return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
  }

  private int calcInitialAge(){

    if(index % 25 <= 5)
      return 10 + rng.nextInt(6);

    if(index % 25 >= 6 && index % 25 <= 10)
      return 15 + rng.nextInt(6);

    if(index % 25 >= 11 && index % 25 <= 15)
      return 20 + rng.nextInt(6);

    return  25 + rng.nextInt(6);
  }

  public int getIndex(){
    return index;
  }

  public int getPeriod(){
    return period;
  }

  public int getX(){
    return this.x;
  }

  public void setX(int val){
    x = val;
  }

  public double getRevenue(){
    return revenue;
  }

  public double getLoggingCost(){
    return loggingCost;
  }

  public int getTimberVolume(){
    return timberVolume;
  }

  public int getAge(){
    return age;
  }

  /*
  * Crea una nueva unidad a partir de la actual, modificando el periodo y la
  * variable de decisión
  * @param p el nuevo periodo
  * @param decision el nuevo valor de la variable de decisión X
  * @return la nueva unidad forestal
  */
  public ForestUnit copyAndUpdate(int p){
    ForestUnit newFU = new ForestUnit(index, p, x, initialAge);
    return newFU;
  }

  public ForestUnit copy(){
    return new ForestUnit(index, period, x, initialAge, loggingCost);
  }

  // DPV = FV * (1 + R/100)^-t
  public static double discountedPresentValue(double initialAmount, int R, int p){
    return initialAmount * Math.pow(1+R/100.0,-1*p);
  }
}
