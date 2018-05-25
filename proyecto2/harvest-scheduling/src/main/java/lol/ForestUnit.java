package lol;

public class ForestUnit{

  private int index;
  private int period;
  // Edad promedio inicial de la unidad
  private int initialAge;
  // Edad promedio de los árboles en la unidad forestal
  private int age;
  // Volumen (de madera) por hectárea de la unidad
  private int timberVolume;
  // Costo de tala por metro cúbico
  private double loggingCost;
  // Superficie en hectáreas
  private final int SURFACE = 30;
  // Precio de madera por metro cúbico
  private final double LOG_PRICE = 100.0;
  // Ingreso por metro cúbico aplicando una tasa de interés del 8%
  private double revenue;

  public ForestUnit(int index, int period, int unitAge, double loggingC){
    initialize(index,period);
    initialAge = unitAge;
    age = initialAge + period - 1;
    if(age < 15)
      timberVolume = 150;
    else
      timberVolume = Parameters.TIMBER_VOLUME[age - 15];
    loggingCost = loggingC;
  }

  private void initialize(int index, int period){
    this.index = index;
    this.period = period;
    revenue = futureValue(LOG_PRICE, 8, period);
  }

  /**
  * Obtiene el índice de esta unidad
  * @return el índice
  */
  public int getIndex(){
    return index;
  }

  /**
  * Obtiene el periodo de esta unidad
  * @return el periodo
  */
  public int getPeriod(){
    return period;
  }

  /**
  * Obtiene el ingreso ($) de esta unidad
  * @return el ingreso
  */
  public double getRevenue(){
    return revenue;
  }

  /**
  * Obtiene el costo de tala ($) de esta unidad
  * @return el costo de tala
  */
  public double getLoggingCost(){
    return loggingCost;
  }

  /**
  * Obtiene el volumen de madera (m^3 / ha) de esta unidad
  * @return el volumen de madera
  */
  public int getTimberVolume(){
    return timberVolume;
  }

  /**
  * Obtiene la edad de esta unidad
  * @return la edad
  */
  public int getAge(){
    return age;
  }

  /**
  * Crea una nueva unidad a partir de la actual, modificando el periodo
  * @param p el nuevo periodo
  * @return la nueva unidad forestal
  */
  public ForestUnit copyAndUpdate(int p){
    ForestUnit newFU = new ForestUnit(index, p, initialAge, loggingCost);
    return newFU;
  }

  /**
  * Crea un nuevo objeto con los mismos valores de este
  * @return una copia de este objeto
  */
  public ForestUnit copy(){
    return new ForestUnit(index, period, initialAge, loggingCost);
  }

  /**
  * Calcula el valor en el futuro de cierta cantidad con una tasa de descuento
  * FV = PV * (1 + r)^n
  * @param initialAmount el valor presente
  * @param discountRate la tasa de descuento
  * @param period el periodo o número de años
  * @return el valor en el futuro de esta cantidad
  */
  public static double futureValue(double initialAmount, int discountRate, int period){
    return initialAmount * Math.pow(1.0 + (discountRate / 100.0), period);
  }
}
