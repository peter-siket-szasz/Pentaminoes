package pentaminoes
import Game.grid
import scala.collection.mutable.Buffer

class Grid {

  val size = 7
  
  private var _colors = Array.ofDim[Int](size, size)
  private var _pentaminoes =  Array.ofDim[Option[Pentamino]](size,size)
  private var _edges = Array.ofDim[Boolean](size, size, 2)
  
  private val minRowLength = 4
  private var pentaminoCounter = 0
  
  override def toString = {
    var text = ""
    for (i <- Range(0,7)) {
      text += this.colors(i).mkString(" ") + "\n"
    }
    text
  }
  
  def colors: Vector[Vector[Int]] = this._colors.map(_.toVector).toVector 
  def pentaminoes: Vector[Vector[Option[Pentamino]]] = this._pentaminoes.map(_.toVector).toVector
  def edges: Vector[Vector[Vector[Boolean]]] = this._edges.map(_.map(_.toVector).toVector).toVector

  def colorAt(x: Int, y: Int) = this._colors(y)(x)
  def pentaminoAt(x: Int, y: Int) = this._pentaminoes(y)(x)

  //Removes everything from Grids memory
  def initialize(): Unit = { 
    this._colors = Array.ofDim[Int](size, size)
    this._pentaminoes =  Array.ofDim[Option[Pentamino]](size,size)
    this._edges = Array.ofDim[Boolean](size, size, 2)
  } 
  
  //Test if given coordinates is empty (its color is 0)
  def canBePlaced(x: Int, y: Int): Boolean = colorAt(x, y) == 0

  //Tests if given coordinate is outside of the grid.
  def isOutOfBounds(x: Int, y: Int): Boolean = x < 0 || y < 0 || x >= size || y >= size
  
  //Test if pentamino can be placed to given coordinates
  def pentaminoCanBePlaced(x: Int, y: Int, pentamino: Pentamino): Boolean = {
    for (x1 <- -2 to 2; y1 <- -2 to 2) {
      if ((pentamino(y1,x1) != 0) && (isOutOfBounds(x + x1, y + y1) || !canBePlaced(x + x1, y + y1))) {
        return false
      }
    }
    true
  }
  
  /*Pentamino is placed to given coordinates if its possible.
   * Returns true or false depending of if placement was succesful or not.
   */

  def add(pent: Pentamino, x: Int, y: Int): Boolean = {

    var pentaminoCanBePlace = this.pentaminoCanBePlaced(x, y, pent)

    if (pentaminoCanBePlace) {
      for (x1 <- -2 to 2; y1 <- -2 to 2) {
        if (pent.edgesCentered(y1,x1) != 0 && !isOutOfBounds(x + x1, y + y1)) {
          pent.edgesCentered(y1, x1).foreach {
            direction => 
              if (direction == 1 && x + x1 + 1 < this.size) this._edges(y + y1)(x + x1 + 1)(1) = true
              if (direction == 2) this._edges(y + y1)(x + x1)(0) = true
              if (direction == 3) this._edges(y + y1)(x + x1)(1) = true
              if (direction == 4 && y + y1 + 1 < this.size) this._edges(y + y1 + 1)(x + x1)(0) = true
            }
        }
        if (pent(y1,x1) != 0 && !isOutOfBounds(x + x1, y + y1)) {
          _colors(y + y1)(x + x1) = pent(y1, x1)
          _pentaminoes(y + y1)(x + x1) = Some(pent)
        }
      }
      true
    } else {
      false
    }
  }

  //Removes the pentamino in given coordinates
  def remove(x: Int, y: Int) {
    val removedPentamino = pentaminoAt(x, y) 

    if (removedPentamino != None) {
      for (x1 <- 0 until this.size; y1 <- 0 until this.size) {
        if (pentaminoAt(x1, y1) == removedPentamino) {
          if ( !this.isOutOfBounds(x1+1, y1) && colorAt(x1 + 1, y1)==0 )
            this._edges(y1)(x1 + 1)(1) = false
          if ( this.isOutOfBounds(x1, y1-1) || colorAt(x1, y1-1)==0 )
            this._edges(y1)(x1)(0) = false
          if ( this.isOutOfBounds(x1-1, y1) || colorAt(x1 - 1, y1)==0 )
            this._edges(y1)(x1)(1) = false
          if ( !this.isOutOfBounds(x1, y1+1) && colorAt(x1, y1+1 )==0 )
            this._edges(y1 + 1)(x1)(0) = false
            
          _pentaminoes(y1)(x1) = None
          _colors(y1)(x1) = 0
        }
      }
    }
  }
  
  /*Returns an instance of Grid in which Pentamino was placed to given coordinates if possible.
   *Method is used to show the player where pentamino would be placed if player clicked now.
   */
  def hypotheticalAdd(pent: Pentamino, x: Int, y: Int): Grid = {
    val hypotheticalGrid = new Grid()
    hypotheticalGrid._colors = Array.tabulate(size, size)(this.colors(_)(_))
    hypotheticalGrid._edges = Array.tabulate(size,size,2)(this.edges(_)(_)(_))
    hypotheticalGrid.add(pent, x, y)
    hypotheticalGrid
  }
  
  //Returns points gained and number of rows made. Removes all pentaminoes which are part of rows.
  def checkRows(): (Int, Int) = {
    
    var points = 0.0
    var rows = 0.0
    val removeList = Buffer[(Int,Int)]()
    
    for (x0 <- 0 until size; y0 <- 0 until size) {
      val rowsColor = colorAt(x0, y0)
   
      var horizontal1 = Buffer[(Int,Int)]()
      var horizontal2 = Buffer[(Int,Int)]()
      var vertical1 = Buffer[(Int,Int)]()
      var vertical2 = Buffer[(Int,Int)]()
      
      if (rowsColor != 0) {
        var x = 0
        var y = 0
        while ( !isOutOfBounds(x0 + x, y0) && colorAt(x0 + x, y0) == rowsColor ) {
          horizontal1 += Tuple2(x0 + x, y0)
          x += 1
        }
        
        x = 0
        y = 0
        while ( !isOutOfBounds(x0 - x, y0) && colorAt(x0 - x, y0) == rowsColor ) {
          horizontal2 += Tuple2(x0 - x, y0)
          x += 1
        }
      
        x = 0
        y = 0
        while ( !isOutOfBounds(x0, y0 + y) && colorAt(x0, y0 + y) == rowsColor ) {
          vertical1 += Tuple2(x0, y0 + y)
          y += 1
        }
        
        x = 0
        y = 0
        while ( !isOutOfBounds(x0, y0 - y) && colorAt(x0, y0 - y) == rowsColor ) {
          vertical2 += Tuple2(x0, y0 - y)
          y += 1
        }
        
        val horizontal = Math.max(0, horizontal1.size + horizontal2.size - 1) //x=0, y=0 is counted twice
        val vertical = Math.max(0, vertical1.size + vertical2.size - 1)

        if (horizontal >= minRowLength){  //Points are calculated for every square so row gets points for
          points += 10 * Math.pow(2, horizontal - minRowLength) / horizontal //each of the squares it contains.
          rows += 1.0 / horizontal
          horizontal1.foreach{coordinates => removeList.append((coordinates._1, coordinates._2))}
          horizontal2.foreach{coordinates => removeList.append((coordinates._1, coordinates._2))}
        }
        if (vertical >= minRowLength) {
          points += 10 * Math.pow(2, vertical - minRowLength) / vertical
          rows += 1.0 / vertical
          vertical1.foreach{coordinates => removeList.append((coordinates._1, coordinates._2))}
          vertical2.foreach{coordinates => removeList.append((coordinates._1, coordinates._2))}
        }
      }
    }
    points *= rows //multiple rows earns more points
    removeList.foreach{coordinate => remove(coordinate._1, coordinate._2)}
    (points.round.toInt, rows.round.toInt)
  }
  
  /*Tests if it's possible to place current pentamino somewhere on the grid.
   *Output is used to determine if player has lost.
   */
  def checkIfMovesPossible(pentamino: Pentamino): Boolean = {
    
    var posibleMoveFound = false
    
    for (x <- 0 to this.size; y <- 0 to this.size) {
      for (rotation <- 1 to 4) {
        for (flip <- 1 to 2) {
          if (this.pentaminoCanBePlaced(x, y, pentamino)) posibleMoveFound = true
          pentamino.flipHorizontal()
        }
        pentamino.rotateClockwise()
      }
    }
    posibleMoveFound
  }
  
}


