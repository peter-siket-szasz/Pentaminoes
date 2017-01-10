package pentaminoes
import Game.grid
import scala.collection.mutable.Buffer

class Grid(private var _pentaminoes: Array[Array[Option[Pentamino]]],
           private var _colors: Array[Array[Int]],
           private var _edges: Array[Array[Array[Boolean]]]) {

  val size = Grid.size  
  private val minRowLength = 4
  private var pentaminoCounter = 0
 // private var lastPentamino: Option[Pentamino] = None
  
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

  def initialize(): Unit = { 
    this._colors = Array.ofDim[Int](size, size)
    this._pentaminoes =  Array.ofDim[Option[Pentamino]](size,size)
    this._edges = Array.ofDim[Boolean](size, size, 2)
  } 
  
  //jos annetussa kohdassa ei ole jotain väriarvoa (nollaa), palauttaa true, muuten false
  def canBePlaced(x: Int, y: Int): Boolean = colorAt(x, y) == 0

  //Tests if given coordinate is outside of the grid.
  def isOutOfBounds(x: Int, y: Int): Boolean = x < 0 || y < 0 || x >= size || y >= size
  
  def pentaminoCanBePlaced(x: Int, y: Int, pentamino: Pentamino): Boolean = {
    for (x1 <- -2 to 2; y1 <- -2 to 2) {
      if ((pentamino(y1,x1) != 0) && (isOutOfBounds(x + x1, y + y1) || !canBePlaced(x + x1, y + y1))) {
        return false
      }
    }
    true
  }
  
  //lisää pentaminon ja sen värit arrayhyn jos kaikilla kohdilla on tyhjää ja väriä ei yritetä lisätä sallitun alueen ulkopuolelle 
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

  //poistaa annetussa kohdassa olevan pentaminon ja kaikki sen osat molemmista taulukoista
  def remove(x: Int, y: Int) { //kohta (0,0) on sallitun alueen ulkopuolella
    val poistettava = pentaminoAt(x, y) 

    if (poistettava != None) {
      for (x1 <- 0 until this.size; y1 <- 0 until this.size) {
        if (pentaminoAt(x1, y1) == poistettava) {
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
  
  def hypotheticalAdd(pent: Pentamino, x: Int, y: Int): Grid = {
    val hypotheticalGrid = new Grid(Array.tabulate(size, size)(this.pentaminoes(_)(_)),
                                    Array.tabulate(size, size)(this.colors(_)(_)),
                                    Array.tabulate(size,size,2)(this.edges(_)(_)(_)))
    hypotheticalGrid.add(pent, x, y)
    hypotheticalGrid
  }
  
  //Returns points gained and number of rows made. Removes Pentaminoes which are part of rows.
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
    (points.round.toInt, rows.toInt)
  }
  
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
  
  def copy: Grid = {
    val anotherGrid = Grid.empty
    anotherGrid._colors = this._colors.map(identity)
    anotherGrid._pentaminoes = this._pentaminoes.map(identity)
    anotherGrid._edges = this._edges.map(identity)
    //println(anotherGrid.pentaminoes)
    //println(this.pentaminoes)
    anotherGrid
  }

  
  
  /*
  //oma apumetodi gridien tarkasteluun kehitysvaiheessa
  def tulosta = { 
    for (x <- 0 until size) {
      print(s"\n")
      for (y <- 0 until size) {
        print(this._colors(x)(y))
      }
    }
  }

  //oma apumetodi gridien tarkasteluun kehitysvaiheessa
  def tulosta2 = {
    for (x <- 0 until size) {
      print(s"\n")
      for (y <- 0 until size) {
        print(this._pentaminoes(x)(y) + " ")
      }
    }
  }
  // */
}

object Grid  {
  
  val size = 7
  
  def empty = new Grid(Array.ofDim[Option[Pentamino]](size,size),
                       Array.ofDim[Int](size, size),
                       Array.ofDim[Boolean](size, size, 2))
  
}

