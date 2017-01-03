package pentaminoes

import scala.io.Source
import java.io.PrintWriter
import scala.collection.mutable.Buffer


object Highscore {
  
  private val fileName = "highscores.txt"
  private val listLenght = 10
  private var highscoreList = this.readListFromFile
  private var minScore = this.minimumScore(this.highscoreList)
  
  def initialize() = {
    this.writeListToFile(Vector())
  }
  
  def getHighscoreList = {
    this.highscoreList
  }
  
  def setNewScore(name: String, score: Int, level: Int, rows: Int): Int = {
    val oldList = this.getHighscoreList
    val newList = oldList.toBuffer
    newList += Some(Tuple4(name, score, level, rows))
    val newListVector = this.arrange(newList.toVector).take(this.listLenght)
    this.writeListToFile(newListVector)
    this.findPosition(name, score, level, rows)
  }
  
  def isScoreEnough(score: Int, level: Int, rows: Int): Boolean = {
    this.arrange(Vector(Option(this.minScore), Option(Tuple4("",score,level,rows))))(1) != Tuple4("",score,level,rows)
  }
  
  
  
  private def readListFromFile: Vector[Option[Tuple4[String,Int,Int,Int]]] = {
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
  
  private def writeListToFile(list: Vector[Option[Tuple4[String,Int,Int,Int]]]) = {
    val file = new PrintWriter(fileName)
    for (row <- list) {
      if (row.isDefined) file.println(row.get._1 + " " + row.get._2 + " " + row.get._3 + " " + row.get._4)
    }
    file.close()
    this.updateVariables()
  }
  
  private def findPosition(name: String, score: Int, level: Int, rows: Int): Int = {
    10 - this.highscoreList.reverse.indexOf(Some(Tuple4(name, score, level, rows)))
  } 
  
  private def updateVariables() = {
    highscoreList = this.readListFromFile
    minScore = this.minimumScore(this.highscoreList)
  }
  
  private def minimumScore(list: Vector[Option[Tuple4[String,Int,Int,Int]]]) = {
    list.last.getOrElse(Tuple4("",0,0,0))
  }
  
  private def arrange(list: Vector[Option[Tuple4[String,Int,Int,Int]]]): Vector[Option[Tuple4[String,Int,Int,Int]]] = {
    val arrangedList = list.map(_.getOrElse(Tuple4("",0,0,0))).sortWith(_._4 > _._4).sortWith(_._3 > _._3).sortWith(_._2 > _._2)
    arrangedList.map({tuple: Tuple4[String,Int,Int,Int] => if (tuple == ("",0,0,0)) None; else Some(tuple) })
  }
  
}