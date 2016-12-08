package pentaminoes

import scala.util.Random

object Game {
  
  type grid = Vector[Vector[Int]]
  
  private var numberOfColors = 2
  private var currentLevel = 1
  private var currentScore = 0
  
  private var firstPentamino = this.randomPentamino
  private var secondPentamino = this.randomPentamino
  
  def newGame() = {
    this.numberOfColors = 2
    this.currentLevel = 1
    this.currentScore = 0
    
    this.firstPentamino = this.randomPentamino
    this.secondPentamino = this.randomPentamino
    
    Grid.initialize()
  }
  
  def randomPentamino = {
    val randomInts = Array.fill(5)(Random.nextInt(this.numberOfColors)+1) // Random int 1 - numberOfColors
    Pentamino.random(randomInts(0), randomInts(1), randomInts(2), randomInts(3), randomInts(4))
  }
  
  def placePentamino(x: Int, y: Int): Boolean = {
    val isPlaced = Grid.add(this.currentPentamino, x, y)
    // TODO (after Grid object is ready) check consequenses
    if (isPlaced) {
      this.firstPentamino = this.secondPentamino
      this.secondPentamino = this.randomPentamino
      true
    } else {
      false
    }
    
  }
  
  // Returns true if current Pentamino can be placed to Grid's coordinates (x,y)
  def canPlacePentamino(x: Int, y:Int): Boolean = {
    ??? // TODO calls Grid's methods
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
    // TODO add other changes
  }
  
  def score = this.currentScore
  
  def addScore(score: Int) = {
    this.currentScore += score
  }
  
  
  
}