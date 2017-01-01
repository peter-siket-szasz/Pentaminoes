package pentaminoes

import scala.io.Source
import java.io.PrintWriter
import scala.collection.mutable.Buffer


object Highscore {
  
  val fileName = "highscores.txt"
  val listLenght = 10
  
  def initialize() = {
    this.writeList(Vector())
  }
  
  def getList: Vector[Option[Tuple4[String,Int,Int,Int]]] = {
    val file = Source.fromFile(fileName)
    val highscores = Buffer[Option[Tuple4[String,Int,Int,Int]]]()
    
    try {
      for (line <- file.getLines().take(listLenght)) {
        val data = line.split(" ")
        try { highscores += Some(Tuple4(data(0), data(1).toInt, data(2).toInt, data(3).toInt)) }
        catch { case ex: Exception => highscores += None; println(ex) }
      }
    } finally {
      for (i <- highscores.length until this.listLenght) highscores += None
      file.close()
    }
    highscores.toVector
  }

  def newScore(name: String, score: Int, level: Int, rows: Int): Int = {
    val oldList = this.getList
    val newList = oldList.toBuffer
    newList += Some(Tuple4(name, score, level, rows))
    val newListVector = this.arrange(newList.toVector).take(this.listLenght)
    this.writeList(newListVector)
    this.findPosition(name, score, level, rows)
  }
  
  def writeList(list: Vector[Option[Tuple4[String,Int,Int,Int]]]) = {
    val file = new PrintWriter(fileName)
    for (row <- list) {
      if (row.isDefined) file.println(row.get._1 + " " + row.get._2 + " " + row.get._3 + " " + row.get._4)
    }
    file.close()
  }
  
  def findPosition(name: String, score: Int, level: Int, rows: Int): Int = {
    val list = this.getList
    var position = this.listLenght
    while (position > 0 && list(position-1) != Some(Tuple4(name, score, level, rows))) {
      position -= 1
    }
    position
  }
  
  def arrange(list: Vector[Option[Tuple4[String,Int,Int,Int]]]): Vector[Option[Tuple4[String,Int,Int,Int]]] = {
    val arrangedList = list.map(_.getOrElse(Tuple4("",0,0,0))).sortWith(_._4 > _._4).sortWith(_._3 > _._3).sortWith(_._2 > _._2)
    arrangedList.map({tuple: Tuple4[String,Int,Int,Int] => if (tuple == ("",0,0,0)) None; else Some(tuple) })
  }
  
  /* For testing
  var list = this.getList
  list.foreach(println(_))
  println()
  this.arrange(list).foreach(println(_))
  println("___________________________________________________")
  this.initialize()
  list = this.getList
  list.foreach(println(_))
  println("___________________________________________________")
  println(this.newScore("miro",87,51,3))
  list = this.getList
  list.foreach(println(_))
  */
  
}