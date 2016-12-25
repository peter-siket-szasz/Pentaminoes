package pentaminoes

import scala.util.Random

object Game {
  
  type grid = Vector[Vector[Int]]
  
  private var numberOfColors = 2
  private var currentLevel = 1
  private var currentScore = 0
  private var rows = 0 //Counts the number of moves which have scored points
  private var nextLevelLimit = 5
  
  private var firstPentamino = this.randomPentamino
  private var secondPentamino = this.randomPentamino
  
  def newGame() = {
    this.numberOfColors = 2
    this.currentLevel = 1
    this.currentScore = 0
    this.rows = 0
    this.nextLevelLimit = 5
    
    this.firstPentamino = this.randomPentamino
    this.secondPentamino = this.randomPentamino
    
    Grid.initialize()
  }
  
  def randomPentamino = {
    var randomInts = Array.fill(5)(0)
    while ( (randomInts.map(color => randomInts.count(_ == color))).reduceLeft(_ max _) >= 4 ) {
      randomInts = Array.fill(5)(Random.nextInt(this.numberOfColors)+1) // Random int 1 - numberOfColors
    }
    Pentamino.random(randomInts(0), randomInts(1), randomInts(2), randomInts(3), randomInts(4))
  }
  
  def placePentamino(x: Int, y: Int) = {
    val isPlaced = Grid.add(this.currentPentamino, x, y)

    if (isPlaced) {
      val pointsAndRows = Grid.checkRows()
      val points = pointsAndRows._1
      val rows = pointsAndRows._2
      
      this.addScore(points)
      
      if (points > 0) {
        this.rows += rows
        while (this.isLevelUp) this.nextLevel() //Level up from level 1 to 3 is possible in theory
      }
      
      this.firstPentamino = this.secondPentamino
      this.secondPentamino = this.randomPentamino
    }
    
    if ( ! this.possibleMovesLeft) this.newGame()
  }
  
  def possibleMovesLeft = Grid.checkIfMovesPossible(this.currentPentamino)
  
  // Returns true if current Pentamino can be placed to Grid's coordinates (x,y)
  def canPlacePentamino(x: Int, y:Int): Boolean = {
    Grid.canBePlaced(x, y)
  }
  
  // Returns a Vector of coordinates in which the current Pentamino (if played in coordinates (x,y)) overlaps another Pentamino
  def overlaps(x: Int, y: Int): Array[Tuple2[Int,Int]] = {
    ??? // TODO calls Grid's methods
  }
  
  def currentPentamino = this.firstPentamino
  
  def nextPentamino = this.secondPentamino
  
  // Returns 2-dimensional Vector of Ints(/colors) in Grid
  def gridColors: grid = Grid.colors
  
  def level = this.currentLevel
  
  def nextLevel() = {
    this.currentLevel += 1
    this.numberOfColors += 1
    this.nextLevelLimit *= 2
  }
  
  def isLevelUp = this.rows >= this.nextLevelLimit
  
  def score = this.currentScore
  
  def addScore(score: Int) = {
    this.currentScore += score
  }
  
  def rowsToNextLevel = this.rows + " / " + this.nextLevelLimit
  
}