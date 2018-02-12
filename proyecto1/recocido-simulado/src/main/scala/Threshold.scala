
object Graph {

  type Vertex = Int
  type Vertices = List[Vertex]
  type Edge = (Int, Int)
  type Edges = List[Edge]
  type Solution = List[Vertex]

  // función de peso (no implementada)
  var w = (e:Edge) => 0

  // lista de aristas realmente conectadas en la gráfica
  def connectedEdges(s:Solution, e:Edges): Edges = s match {
    case Nil => List()
    //case x:Int :: y:Int :: xs:List[Int] if !e.contains((x, y)) =>

  }
}

package object definitions_2 {

  import Graph.Solution


  var A = (s:Solution) =>
  var f = (s:Solution) =>

  def computeBatch(temp:Double, s:Graph.Solution): (Double, Solution) = {
    (0.0, List())
  }

  def acceptByThresholds(temp:Double, s:Graph.Solution): Unit = {

  }

}
