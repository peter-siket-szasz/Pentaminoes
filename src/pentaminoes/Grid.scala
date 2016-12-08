package pentaminoes
import Game.grid

object Grid {

  private val size = 7
  private var _pentaminoes: Array[Array[Option[Pentamino]]] = Array.ofDim[Option[Pentamino]](size,size)
  private var _colors = Array.ofDim[Int](size, size)
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


  def colorAt(x: Int, y: Int) = this._colors(y)(x)
  def pentaminoAt(x: Int, y: Int) = this._pentaminoes(y)(x)

  def initialize(): Unit = { 
    this._colors = Array.ofDim[Int](size, size)
    this._pentaminoes =  Array.ofDim[Option[Pentamino]](size,size)
  } 
  
  //jos annetussa kohdassa ei ole jotain väriarvoa (nollaa), palauttaa true, muuten false
  def canBePlaced(x: Int, y: Int): Boolean = colorAt(x, y) == 0

  //Tests if given coordinate is outside of the grid.
  def isOutOfBounds(x: Int, y: Int): Boolean = x < 0 || y < 0 || x >= size || y >= size
  
  //lisää pentaminon ja sen värit arrayhyn jos kaikilla kohdilla on tyhjää ja väriä ei yritetä lisätä sallitun alueen ulkopuolelle 
  def add(pent: Pentamino, x: Int, y: Int): Boolean = { // kohta (0,0) on sallitun alueen ulkopuolella

    var foundError = false

    for (x1 <- -2 to 2; y1 <- -2 to 2) {
      if ((pent(y1,x1) != 0) && (isOutOfBounds(x + x1, y + y1) || !canBePlaced(x + x1, y + y1))) {
        foundError = true
        return false
      }
    }

    if (!foundError) {
      for (x1 <- -2 to 2; y1 <- -2 to 2) {
        if (pent(y1,x1) != 0 && !isOutOfBounds(x + x1, y + y1)) {
          _colors(y + y1)(x + x1) = pent(y1, x1)
          _pentaminoes(y + y1)(x + x1) = Some(pent)
        }
      }
    }
    true
  }

  //poistaa annetussa kohdassa olevan pentaminon ja kaikki sen osat molemmista taulukoista
  def remove(x: Int, y: Int) { //kohta (0,0) on sallitun alueen ulkopuolella
    val poistettava = pentaminoAt(x, y)

    if (poistettava != None) {
      for (x1 <- 0 until size; y1 <- 0 until size) {
        if (pentaminoAt(x1, y1) == poistettava) {
          _pentaminoes(y1)(x1) = None
          _colors(y1)(x1) = 0
        }
      }
    }

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