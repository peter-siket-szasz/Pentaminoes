package gui

import pentaminoes._
import Game.grid
import scala.swing._
import scala.swing.event._
import java.awt.Color
import scala.swing.GridBagPanel._
import scala.util.Random
import javax.swing. { UIManager, ImageIcon }
import java.awt.image.BufferedImage                                           
import java.io.File                                                           
import javax.imageio.ImageIO
    

object GameWindow extends SimpleSwingApplication {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  
  private var Grid = Game.Grid
  
  val gridWidth = Game.gridColors(0).size
  val gridHeight = Game.gridColors.size
  val blockSize = 50
  val smallBlockSize = 25
  val gridDimesnion = new Dimension(gridWidth * blockSize, gridHeight * blockSize)
  val nextGridSize = 5
  val nextGridDimension = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
  val windowSize = new Dimension(1000, 900)
  
  val verticalPic = new ImageIcon("Icons/flipVertical.png")
  val horizontalPic = new ImageIcon("Icons/flipHorizontal.png")
  val clockwisePic = new ImageIcon("Icons/rotateClockwise.png")
  val counterclockwisePic = new ImageIcon("Icons/rotateCounterclockwise.png")
  val backgroundPic = ImageIO.read(new File("Icons/background.png"))
  
  val defaultFont = new Font("Castellar", 0, 30)

  val numbersToColors = Vector(Color.WHITE, Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.MAGENTA)
  
  def paintLinesAndSquares(g: Graphics2D, colors: grid, edges: Vector[Vector[Vector[Boolean]]], blockSize: Int) = {
    
    val sidex = colors(0).size
    val sidey = colors.size
    
    for (row <- 0 until sidey) {
      for (col <- 0 until sidex) {
        g.setColor(numbersToColors(colors(row)(col)))
        g.fillRect(col * blockSize, row * blockSize, blockSize, blockSize)
      }
    }
    g.setColor(Color.BLACK)
    for (x <- 1 until sidex) g.drawLine(x * blockSize, 0, x * blockSize, sidey * blockSize)
    for (y <- 1 until sidey) g.drawLine(0, y * blockSize, sidex * blockSize, y * blockSize)
    paintEdges(g, edges, blockSize)
    
    
  }
  
  def paintEdges(g: Graphics2D, edges: Vector[Vector[Vector[Boolean]]], blockSize: Int) = {
    
    val height = edges(0).size
    val width = edges.size
    g.setColor(Color.BLACK)
    
    for (row <- 0 until height) {
      for (col <- 0 until width) {
        if (edges(row)(col)(0) && row != 0) {
          for (i <- -1 to 1) {
            g.drawLine(col * blockSize, row * blockSize + i, (col + 1) * blockSize, row * blockSize + i)
          }
        }
        if (edges(row)(col)(1) && col !=0) {
          for (i <- -1 to 1) {
            g.drawLine(col * blockSize + i, row * blockSize, col * blockSize + i, (row + 1) * blockSize)
          }
        }
      }
    }
  }
  
  class Display(width: Int, height: Int, var colors: grid, var edges: Vector[Vector[Vector[Boolean]]], blockSize: Int) 
  extends GridPanel(width, height) {
    preferredSize = new Dimension(width * blockSize, width * blockSize)
    focusable = true
    override def paintComponent(g: Graphics2D) = 
      paintLinesAndSquares(g, colors, edges, blockSize)
  }
  
  
  
  /*var grid = new GridPanel(gridWidth, gridHeight) {     
      preferredSize = new Dimension(gridWidth * blockSize, gridHeight * blockSize)
      focusable = true
      override def paintComponent(g: Graphics2D) = 
        paintLinesAndSquares(g, Game.gridColors, Grid.edges, blockSize)
    }*/
  
  val grid = new Display(gridWidth, gridHeight, Game.gridColors, Grid.edges, blockSize)
  
  val currentPentamino = new GridPanel(nextGridSize, nextGridSize) {
    preferredSize = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
    focusable = false
    override def paintComponent(g: Graphics2D) = 
      paintLinesAndSquares(g, Game.currentPentamino.toVector, Game.currentPentamino.twoBooleanEdges, smallBlockSize)
  }
  
  val nextPentamino = new GridPanel(nextGridSize, nextGridSize) {
    preferredSize = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
    focusable = false
    override def paintComponent(g: Graphics2D) = 
      paintLinesAndSquares(g, Game.nextPentamino.toVector, Game.nextPentamino.twoBooleanEdges, smallBlockSize)
  }
  
  private def scoreText = "Score: " + Game.score
  private def levelText = "Level: " + Game.level
  private def rowsText = "Rows: " + Game.rowsToNextLevel
  private def updateLabels = {
    score.text = scoreText
    level.text = levelText
    rows.text = rowsText
  }
  private def updateHighscores = {
    val scores = Highscore.getHighscoreListAsString
    for (i <- 0 until highscores.size) {
      highscores(i).text = s"${i+1}: ${scores(i)}"
    }
  }
  
  private def updateGrid = {
    grid.colors = Game.gridColors
    grid.edges = Grid.edges
  }
  
  val score = new Label{text = scoreText; preferredSize = new Dimension(200,45); font = defaultFont}
  val level = new Label{text = levelText; preferredSize = new Dimension(200,45); font = defaultFont}
  val rows  = new Label{text = rowsText;  preferredSize = new Dimension(250,45); font = defaultFont}
  
  val highscores = 
    Array.fill[Label](Highscore.getHighscoreList.size)(new Label{font = defaultFont; foreground = Color.WHITE})
  
  val scoreBoard = new FlowPanel {
    contents += score
    contents += level
    contents += rows
  }
  
  val flipHorizontally = new Button {
    preferredSize = new Dimension(100,100)
    icon = horizontalPic
    focusable = true
  }
  
  val flipVertically = new Button {
    preferredSize = new Dimension(100,100)
    icon = verticalPic
    focusable = true
  }
  
  val rotateClockwise = new Button {
    preferredSize = new Dimension(100,100)
    icon = clockwisePic
    focusable = true
  }
  
  val rotateCounterclockwise = new Button {
    preferredSize = new Dimension(100,100)
    icon = counterclockwisePic
    focusable = true
  }
  
  val playButton = new Button {
    preferredSize = new Dimension(250, 50)
    text = "Play"
    font = defaultFont
    focusable = true
  }
  
  val scoreButton = new Button {
    preferredSize = new Dimension(250,50)
    text = "Hi-Scores"
    font = defaultFont
    focusable = true
  }
  
  val menuButton = new Button {
    text = "Menu"
    font = defaultFont
    focusable = true
  }
  
  val quitButton = new Button {
    preferredSize = new Dimension(250,50)
    text = "Quit"
    font = defaultFont
    focusable = true
  }
  
  val gameScreen = new GridBagPanel {
    override def paintComponent(g: Graphics2D) = {
      g.drawImage(backgroundPic, 0, 0, null)
    }
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.gridwidth = 6
    c.insets = new Insets(0,0,25,0)
    layout(scoreBoard) = c
    c.gridx = 0
    c.gridy = 1
    c.gridwidth = 3
    c.gridheight = 3
    c.insets = new Insets(0,0,0,25)
    layout(grid) = c
    c.gridwidth = 1
    c.gridheight = 1
    c.gridx = 3
    c.gridy = 1
    c.insets = new Insets(0,100,0,0)
    layout(flipHorizontally) = c
    c.gridx = 5
    c.gridy = 1
    c.insets = new Insets(0,-100,0,0)
    layout(flipVertically) = c
    c.gridx = 3
    c.gridy = 2
    c.insets = new Insets(0,0,0,0)
    layout(rotateCounterclockwise) = c
    c.gridx = 5
    c.gridy = 2
    c.insets = new Insets(0,0,0,0)
    layout(rotateClockwise) = c
    c.gridx = 4
    c.gridy = 2
    c.insets = new Insets(25,-25,0,25)
    layout(currentPentamino) = c
    c.gridx = 4
    c.gridy = 3
    c.insets = new Insets(25,-50,0,0)
    layout(nextPentamino) = c
  }

  val menuScreen = new GridBagPanel {
    override def paintComponent(g: Graphics2D) = {
      g.drawImage(backgroundPic, 0, 0, null)
    }
    val c = new Constraints
    c.insets = new Insets(13,0,13,0)
    layout(playButton) = c
    c.gridy = 2
    layout(scoreButton) = c
    c.gridy = 4
    layout(quitButton) = c
  }
  
  val highscoreScreen = new GridBagPanel {
    override def paintComponent(g: Graphics2D) = {
      g.drawImage(backgroundPic, 0, 0, null)
    }
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.ipady = 25
    val scoreInfo = new Label{text = "Name, Score, Level, Rows"; font = defaultFont; foreground = Color.WHITE}
    layout(scoreInfo) = c
    c.gridy += 1
    for (score <- highscores) {
      layout(score) = c
      c.gridy += 1
    }
    layout(menuButton) = c
  }
  
  val newGame = Action("New game") { Game.newGame; updateLabels; updateGrid; frame.repaint }
  
  def gameOver: Unit = {
    if (Highscore.isScoreEnough(Game.score, Game.level, Game.rows)) {
      val popup = Dialog.showInput(gameScreen, "Your score is eligible for the Highscore list!", "Highscore!", Dialog.Message.Info, initial = "Insert name")
      val name = popup.getOrElse("Anonymous").replace(' ', '_')
      Highscore.setNewScore(name, Game.score, Game.level, Game.rows)
      frame.contents = highscoreScreen
      updateHighscores
    }
    else {
      val popup = Dialog.showConfirmation(gameScreen, "Game over! Do you want to play again?", "Game over", Dialog.Options.YesNo)
      if (popup == Dialog.Result.No) frame.contents = menuScreen
    }
    Game.newGame
    frame.repaint
  }
  
  val frame: MainFrame = new MainFrame {
    
    title = "Pentaminoes"
    resizable = false
    location = new Point(200, 50)
    preferredSize = windowSize

    menuBar = new MenuBar {
      contents += new Menu("Game") {
        contents += new MenuItem(newGame)
      }
    }
    contents = menuScreen
    
    listenTo(grid.mouse.clicks, grid.mouse.moves, grid.keys)
    listenTo(flipHorizontally, flipVertically, rotateClockwise, rotateCounterclockwise)
    listenTo(playButton, scoreButton, menuButton, quitButton)
    reactions += {
      case MouseClicked(gameScreen, point, _, _, _)  => {
        Game.placePentamino(point.x / blockSize, point.y / blockSize)
        updateGrid
        updateLabels
        frame.repaint()
        if (!Game.gameOn) {
          gameOver
        }
      }
      case MouseMoved(gameScreen, point, _) => {
        val hypoGrid = Grid.hypotheticalAdd(Game.currentPentamino, point.x / blockSize, point.y / blockSize)
        grid.colors = hypoGrid.colors
        grid.edges = hypoGrid.edges
        frame.repaint()
        updateGrid
      }
      case ButtonClicked(source) => {
        if (source == flipHorizontally) Game.currentPentamino.flipHorizontal()
        else if (source == flipVertically) Game.currentPentamino.flipVertical()
        else if (source == rotateClockwise) Game.currentPentamino.rotateClockwise()
        else if (source == rotateCounterclockwise) Game.currentPentamino.rotateCounterClockwise()
        else if (source == playButton)  {this.contents = gameScreen; Game.newGame}
        else if (source == scoreButton) {this.contents = highscoreScreen; updateHighscores}
        else if (source == menuButton)  this.contents = menuScreen
        else if (source == quitButton)  dispose()
        frame.repaint
      }
    }
  }
  
  def top: MainFrame = frame
  
}
  
  
  
  
  