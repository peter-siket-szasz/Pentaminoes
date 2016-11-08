package pentaminoes

class Pentamino(var array: Array[Array[Int]]) {
  
  override def toString = {
    var text = ""
    for (i <- Range(0,5)) {
      text += this.array(i).mkString(" ") + "\n"
    }
    text
  }
  
  def spinClockwise() = {
    ???
  }
  
  def spinCounterClocwise() = {
    ???
  }
  
  def flip() = { // y-axis reflection
    ???
  }
    
  
}

object Pentamino {
  
  def pentaminoes = Array("P", "X", "F", "V", "W", "Y", "I", "T", "Z", "U", "N", "L")
  
  def apply(param: Array[Array[Int]]) = new Pentamino(param)
  
  def apply(list: Array[Int]) = { // cleaner solution?
    new Pentamino(Array(Array(list(0), list(1), list(2), list(3), list(4)),
                        Array(list(5), list(6), list(7), list(8), list(9)),
                        Array(list(10), list(11), list(12), list(13), list(14)),
                        Array(list(15), list(16), list(17), list(18), list(19)),
                        Array(list(20), list(21), list(22), list(23), list(24))))
  }
  
  def apply(list: Array[Array[Int]], char: Char) = {
    ???
  }
  
  def p(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                      0, 0, c1,c2,0,
                      0, 0, c3,c4,0,
                      0, 0, c5,0, 0,
                      0, 0, 0, 0, 0) )
  }
  
  def x(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                          0, 0, c1,0, 0,
                          0, c2,c3,c4,0,
                          0, 0, c5,0, 0,
                          0, 0, 0, 0, 0) )
  }
  
  def f(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                          0, c1,c2,0, 0,
                          0, 0 ,c3,c4,0,
                          0, 0, c5,0, 0,
                          0, 0, 0, 0, 0) )
  }
  
  def v(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                          0, 0, 0 ,0, 0,
                          0, 0, c1,c2,c3,
                          0, 0, c4,0, 0,
                          0, 0, c5,0, 0) )
  }
  
  def w(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                          0, c1,0 ,0, 0,
                          0, c2,c3,0, 0,
                          0, 0, c4,c5,0,
                          0, 0, 0, 0, 0) )
  }
  
  def y(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0, 0, 0,
                          0, 0, c1,0, 0,
                          0, 0, c2,c3,0,
                          0, 0, c4,0, 0,
                          0, 0, c5,0, 0) )
  }
  
  def i(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, c1,0, 0,
                          0, 0, c2,0, 0,
                          0, 0, c3,0, 0,
                          0, 0, c4,0, 0,
                          0, 0, c5,0, 0) )
  }
  
  def t(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                          0, c1,c2,c3,0,
                          0, 0, c4,0, 0,
                          0, 0, c5,0, 0,
                          0, 0, 0, 0, 0) )
  }
  
  def z(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                          0, c1,c2,0, 0,
                          0, 0, c3,0, 0,
                          0, 0, c4,c5,0,
                          0, 0, 0, 0, 0) )
  }
  
  def u(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                          0, c1,0, c2,0,
                          0, c3,c4,c5,0,
                          0, 0, 0 ,0, 0,
                          0, 0, 0, 0, 0) )
  }
  
  def n(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                          0, 0, 0, c1,0,
                          0, 0, c2,c3,0,
                          0, 0, c4,0, 0,
                          0, 0, c5,0, 0) )
  }
  
  def l(c1: Int, c2: Int, c3:Int, c4:Int, c5:Int) = {
    Pentamino( Array( 0, 0, 0 ,0, 0,
                          0, 0, c1,c2,0,
                          0, 0, c3,0, 0,
                          0, 0, c4,0, 0,
                          0, 0, c5,0, 0) )
  }
  
}

