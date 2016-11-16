package pentaminoes

object Grid {
  
  private var _colors: Array[Array[Int]] = Array.fill[Int](7,7)(0)
  private var _pentaminoes: Array[Array[Option[Pentamino]]] = Array.fill[Option[Pentamino]](7,7)(None)
  
  override def toString = {
    var text = ""
    for (i <- Range(0,7)) {
      text += this.colors(i).mkString(" ") + "\n"
    }
    text
  }
  
  def colors: Vector[Vector[Int]] = this._colors.map(_.toVector).toVector
  
  def pentaminoes: Vector[Vector[Option[Pentamino]]] = this._pentaminoes.map(_.toVector).toVector
  
  def initialize(): Unit = {
    this._colors = Array.fill[Int](7,7)(0)
    this._pentaminoes = Array.fill[Option[Pentamino]](7,7)(None)
  }
  
  def placePentamino(x: Int, y: Int, pentamino: Pentamino): Unit = {
    val offset = pentamino.toVector.size/2
    for (xOffset <- -offset until offset; yOffset <- -offset until offset) {
      this._colors(x+xOffset)(y+yOffset) = pentamino(xOffset,yOffset)
    }
  }
  
  
}