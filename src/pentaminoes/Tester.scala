package pentaminoes

object Tester extends App{
  
  /*
  println(Pentamino.p(1,1,1,1,1).toString)
  println(Pentamino.x(1,1,1,1,1).toString)
  println(Pentamino.f(1,1,1,1,1).toString)
  println(Pentamino.v(1,1,1,1,1).toString)
  println(Pentamino.w(1,1,1,1,1).toString)
  println(Pentamino.y(1,1,1,1,1).toString)
  println(Pentamino.i(1,1,1,1,1).toString)
  println(Pentamino.t(1,1,1,1,1).toString)
  println(Pentamino.z(1,1,1,1,1).toString)
  println(Pentamino.u(1,1,1,1,1).toString)
  println(Pentamino.n(1,1,1,1,1).toString)
  println(Pentamino.l(1,1,1,1,1).toString)
  // */
  
  /*
  val pentaminoF = Pentamino.f(1,2,3,4,5)
  println(pentaminoF)
  pentaminoF.flipHorizontal()
  println(pentaminoF)
  pentaminoF.flipVertical()
  println(pentaminoF)
  pentaminoF.spinCounterClockwise()
  println(pentaminoF)
  pentaminoF.spinClockwise()
  println(pentaminoF)
  // */
  
  /*
  println(Pentamino.i(1,2,3,4,5)(0,0))
  println(Pentamino.i(1,2,3,4,5)(-1,2))
  */

  /*
	println(Pentamino('i',1,2,3,4,5))
	println(Pentamino('I',1,2,3,4,5))
	println(Pentamino('p',1,2,3,4,5))
	println(Pentamino('ä',1,2,3,4,5))
	// */

	val list = Array.fill(24)(Pentamino.random(1, 2, 3, 4, 5))
	println(list.mkString("\n"))
	
	
	
	
	
	
	
	


}