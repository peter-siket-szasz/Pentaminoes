package pentaminoes

import scala.util.Random

class Pentamino(private var array: Array[Array[Int]]) {
  
  override def toString: String = {
    var text = ""
    for (i <- Range(0,5)) {
      text += this.array(i).mkString(" ") + "\n"
    }
    text
  }
  
  def toVector: Vector[Vector[Int]] = this.array.map(_.toVector).toVector
  
  def apply(x: Int, y:Int):Int = this.toVector(x+2)(y+2) //relative, (2,2) -> (0,0)
  
  /* Rotate methods and flip methods and randomRotation all 
  both make changes to original Pentamino and return the modified version of it */
  
  def rotateClockwise(): Pentamino = {
    val newArray = Array.fill(5,5)(0)
    for (x <- Range(0,5); y <- Range(0,5)) newArray(x)(y) = this.array(4-y)(x)
    this.array = newArray
    this
  }
  
  def rotateCounterClockwise(): Pentamino = {
    val newArray = Array.fill(5,5)(0)
    for (x <- Range(0,5); y <- Range(0,5)) newArray(x)(y) = this.array(y)(4-x)
    this.array = newArray
    this
  }
  
  def flipVertical(): Pentamino = { // x-axis reflection
    val newArray = Array.fill(5,5)(0)
    for (x <- Range(0,5); y <- Range(0,5)) newArray(x)(y) = this.array(4-x)(y)
    this.array = newArray
    this
  }
  
  def flipHorizontal(): Pentamino = { // y-axis reflection
    val newArray = Array.fill(5,5)(0)
    for (x <- Range(0,5); y <- Range(0,5)) newArray(x)(y) = this.array(x)(4-y)
    this.array = newArray
    this
  }
  
  def randomRotation(): Pentamino = { // Randomly flip AND/OR rotate Pentamino
    Random.nextInt(4) match { // Random rotation
      case 0 => this
      case 1 => this.rotateClockwise()
      case 2 => {this.rotateClockwise(); this.rotateClockwise()}
      case 3 => this.rotateCounterClockwise()
    }
    
    Random.nextInt(4) match { // Random flip
      case 0 => this
      case 1 => this.flipHorizontal()
      case 2 => this.flipVertical()
      case 3 => {this.flipHorizontal(); this.flipVertical()}
    }

    this
  }
  
}

object Pentamino {
  
  def pentaminoes: Vector[Char] = Vector('p', 'x', 'f', 'v', 'w', 'y', 'i', 't', 'z', 'u', 'n', 'l')
  
  def apply(param: Array[Array[Int]]): Pentamino = new Pentamino(param)
  
  def apply(list: Array[Int]): Pentamino = {
    val shape = Array.ofDim[Int](5,5)
    for (i <- 0 until list.size) {
      shape(i / 5)(i % 5) = list(i)
    }
    Pentamino(shape)
  }
  
  def apply(char: Char, c1: Int, c2: Int, c3: Int, c4: Int, c5: Int): Pentamino =
    char match {
      case 'p' | 'P' => this.p(c1, c2, c3, c4, c5)
      case 'x' | 'X' => this.x(c1, c2, c3, c4, c5)
      case 'f' | 'F' => this.f(c1, c2, c3, c4, c5)
      case 'v' | 'V' => this.v(c1, c2, c3, c4, c5)
      case 'w' | 'W' => this.w(c1, c2, c3, c4, c5)
      case 'y' | 'Y' => this.y(c1, c2, c3, c4, c5)
      case 'i' | 'I' => this.i(c1, c2, c3, c4, c5)
      case 't' | 'T' => this.t(c1, c2, c3, c4, c5)
      case 'z' | 'Z' => this.z(c1, c2, c3, c4, c5)
      case 'u' | 'U' => this.u(c1, c2, c3, c4, c5)
      case 'n' | 'N' => this.n(c1, c2, c3, c4, c5)
      case 'l' | 'L' => this.l(c1, c2, c3, c4, c5)
      case  _  => this(Array.ofDim[Int](5,5)) // Default = empty "Pentamino"
    }
  
  // Random shaped and rotated Pentamino
  def random(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    val pentaminoChar = this.pentaminoes(Random.nextInt(pentaminoes.size))
    this(pentaminoChar, c1, c2, c3, c4, c5).randomRotation()
  }
  
  def p(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, 0, c1,c2,0,
                      0, 0, c3,c4,0,
                      0, 0, c5,0, 0,
                      0, 0, 0, 0, 0) )
  }
  
  def x(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, 0, c1,0, 0,
                      0, c2,c3,c4,0,
                      0, 0, c5,0, 0,
                      0, 0, 0, 0, 0) )
  }
  
  def f(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, c1,c2,0, 0,
                      0, 0 ,c3,c4,0,
                      0, 0, c5,0, 0,
                      0, 0, 0, 0, 0) )
  }
  
  def v(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, 0, 0 ,0, 0,
                      0, 0, c1,c2,c3,
                      0, 0, c4,0, 0,
                      0, 0, c5,0, 0) )
  }
  
  def w(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, c1,0 ,0, 0,
                      0, c2,c3,0, 0,
                      0, 0, c4,c5,0,
                      0, 0, 0, 0, 0) )
  }
  
  def y(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, 0, c1,0, 0,
                      0, 0, c2,c3,0,
                      0, 0, c4,0, 0,
                      0, 0, c5,0, 0) )
  }
  
  def i(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, c1,0, 0,
                      0, 0, c2,0, 0,
                      0, 0, c3,0, 0,
                      0, 0, c4,0, 0,
                      0, 0, c5,0, 0) )
  }
  
  def t(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                      0, c1,c2,c3,0,
                      0, 0, c4,0, 0,
                      0, 0, c5,0, 0,
                      0, 0, 0, 0, 0) )
  }
  
  def z(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                      0, c1,c2,0, 0,
                      0, 0, c3,0, 0,
                      0, 0, c4,c5,0,
                      0, 0, 0, 0, 0) )
  }
  
  def u(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                      0, c1,0, c2,0,
                      0, c3,c4,c5,0,
                      0, 0, 0 ,0, 0,
                      0, 0, 0, 0, 0) )
  }
  
  def n(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                      0, 0, 0, c1,0,
                      0, 0, c2,c3,0,
                      0, 0, c4,0, 0,
                      0, 0, c5,0, 0) )
  }
  
  def l(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int): Pentamino = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                      0, 0, c1,c2,0,
                      0, 0, c3,0, 0,
                      0, 0, c4,0, 0,
                      0, 0, c5,0, 0) )
  }
  
}

