package pentaminoes
import Game.grid

object Grid {

  private val size = 7 + 4
  private var _pentaminoes: Array[Array[Int]] = Array.ofDim[Int](size,size)
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
  def pentaminoes: Vector[Vector[Int]] = this._pentaminoes.map(_.toVector).toVector 


  def colorAt(x: Int, y: Int) = this._colors(x)(y)
  def pentaminoAt(x: Int, y: Int) = this._pentaminoes(x)(y)

  def initialize(): Unit = { 
    this._colors = Array.ofDim[Int](size, size)
    this._pentaminoes =  Array.ofDim[Int](size,size)
  } 
  
  //jos annetussa kohdassa ei ole jotain väriarvoa (nollaa), palauttaa true, muuten false
  def canBePlaced(x: Int, y: Int): Boolean = colorAt(x, y) == 0

  //jos kohta on jossain reunoilla, palauttaa true, muuten false
  def isOutOfBounds(x: Int, y: Int): Boolean = x < 2 || y < 2 || x >= size - 2 || y >= size - 2

  
  
  //lisää pentaminon ja sen värit arrayhyn jos kaikilla kohdilla on tyhjää ja väriä ei yritetä lisätä sallitun alueen ulkopuolelle 
  def add(pent: Pentamino, x: Int, y: Int) = { // kohta (0,0) on sallitun alueen ulkopuolella

    var foundError = false

    for (x1 <- 0 to 4; y1 <- 0 to 4) {
      if ((pent.toVector(x1)(y1) != 0) && (isOutOfBounds(x + x1, y + y1) || !canBePlaced(x + x1, y + y1))) {
        foundError = true
      }
    }

    if (!foundError) pentaminoCounter += 1

    for (x1 <- 0 to 4; y1 <- 0 to 4) {
      if (!foundError) {
        _colors(x + x1)(y + y1) += pent.toVector(x1)(y1)
        if (_colors(x + x1)(y + y1) != 0) _pentaminoes(x + x1)(y + y1) += pentaminoCounter
      }
    }
  }

  //poistaa annetussa kohdassa olevan pentaminon ja kaikki sen osat molemmista taulukoista
  def remove(x: Int, y: Int) { //kohta (0,0) on sallitun alueen ulkopuolella
    val poistettava = pentaminoAt(x, y)

    if (poistettava != 0) {
      for (x1 <- 0 until size; y1 <- 0 until size) {
        if (pentaminoAt(x1, y1) == poistettava) {
          _pentaminoes(x1)(y1) = 0
          _colors(x1)(y1) = 0
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