package lol;

import java.util.LinkedList;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class TabuSearch {

  /**
  * Crea una solución inicial para utilizar en la Búsqueda Tabú utilizando el
  * algoritmo de programación entera de Monte Carlo con parámetros especiales.
  */
  private static SchedulePlan monteCarlo(int units, int periods, SchedulePlan s){

    /* Los parámetros especiales son:
    * núm. iteraciones: 250
    * volumen de cada periodo >= VOLUME_GOAL * 1.1
    */
    Random rng = Parameters.RNG;
    SchedulePlan bestSolution = s;
    int count = 0;

    double bestObjective = bestSolution.objective();

    /* ArrayList con los índices de las unidades para utilizar en el random. */
    ArrayList<Integer> uI = new ArrayList<>();
    for(int i = 0; i < units; i++)
      uI.add(i);

    for(int iter = 0; iter < 250; iter ++){

      // Solución inicial en 0s
      int[][] decisions = new int[periods][units];

      /* Arreglo en el que cada índice representa una unidad y el valor es
      * el periodo en que es talado, si el valor es -1 esa unidad no se taló */
      int[] unitsPeriods = new int[units];
      Arrays.fill(unitsPeriods, -1);

      ArrayList<Integer> unitsIndexes = new ArrayList<>(uI);

      for(int t = 0; t < periods; t++){
        int volume = 0;
        while(count < 600) {

          // unidad aleatoria
          int rUnit = rng.nextInt(unitsIndexes.size());
          // Si la unidad a talar cumple la restricción de adyacencia, se agrega
          if(s.unitMeetsAdjacency(rUnit, t)){
            // quitar unidad para no repetir y cumplir la restricción de singularidad
            unitsIndexes.remove(rUnit);
            unitsPeriods[rUnit] = t;
            count = 0;
            volume += s.getPlan()[t][rUnit].getTimberVolume();
            // Si el volumen excede en 10% la meta del volumen, termina con el periodo
            if(volume > Parameters.VOLUME_GOAL * 1.1){
              break;
            }
          } else {
            count++;
          }
        }
      }

      // Asigna los valores de unidad y periodo a la solución
      for(int i = 0; i < units; i++){
        if(unitsPeriods[i] == -1){
          continue;
        }
        decisions[unitsPeriods[i]][i] = 1;
      }

      SchedulePlan newS = new SchedulePlan(s.getPlan(), decisions);
      double newObjective = newS.objective();

      /* Si el nuevo objetivo es mayor que el mejor, actualiza el mejor con
         el nuevo. */
      if(newObjective > bestObjective){
        bestSolution = newS;
        bestObjective = newObjective;
      }
    }

    return bestSolution;
  }

  /**
  * Búsqueda Tabú para el problema de Harvest Scheduling (URM)
  * @param iterations el número de iteraciones del algoritmo
  * @param schedule la solución inicial
  * @return una solución factible
  */
  public static SchedulePlan run(int iterations, SchedulePlan schedule) {

    /* Tamaño de la lista tabú */
    int maxTabuSize = 100;
    int periods = schedule.getPlan().length;
    int units = schedule.getPlan()[0].length;

    /* Solución inicial usando el algoritmo de Monte Carlo */
    SchedulePlan best = monteCarlo(units, periods, schedule);

    double bestObjective = best.objective();

    /* Inicializar el mejor candidato con la solución inicial */
    SchedulePlan bestCandidate =
      new SchedulePlan(best.getPlan(), best.getDecisions());

    double bestCObjective = bestCandidate.objective();

    LinkedList<SchedulePlan> tabuList = new LinkedList<>();

    for(int it = 0; it < iterations; it++){

      for(int i = 0; i < units; i++){

        for(int t = 0; t < periods; t++){

          int[][] newCDec = bestCandidate.getDecisions();

          int index = bestCandidate.getUnitDecisionIndex(i);
          if(t == index) continue;

          newCDec[t][i] = 1;

          if(index != -1){
            newCDec[index][i] = 0;
          }

          SchedulePlan newCandidate = new SchedulePlan(bestCandidate.getPlan(),
            newCDec);

          if(newCandidate.unitMeetsAdjacency(i, t)){

            double obj = index != -1 ?
              bestCObjective - bestCandidate.unitNetRevenue(i, index) : bestCObjective;

            double unitRev = newCandidate.unitNetRevenue(i, t);

            double newObjective = obj + unitRev;

            if(newObjective > bestCObjective && !tabuList.contains(newCandidate)){
              bestCandidate = newCandidate;
              bestCObjective = newObjective;
            }
          }
        }
      }

      if(bestCObjective > bestObjective){
        best = bestCandidate;
        bestObjective = bestCObjective;
      }
      tabuList.add(bestCandidate);

      if(tabuList.size() > maxTabuSize){
        tabuList.remove(0);
      }
    }

    return best;
  }
}
