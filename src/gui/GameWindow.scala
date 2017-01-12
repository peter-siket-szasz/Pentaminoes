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
  
  var Grid = Game.grid
  
  val gridSize = Grid.size
  val blockSize = 50
  val smallBlockSize = 25
  val gridDimesnion = new Dimension(gridSize * blockSize, gridSize * blockSize)
  val nextGridSize = 5
  val nextGridDimension = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
  val windowSize = new Dimension(1000, 900)
  var mousePosx = 3
  var mousePosy = 3
  
  val verticalPic = new ImageIcon("Icons/flipVertical.png")
  val horizontalPic = new ImageIcon("Icons/flipHorizontal.png")
  val clockwisePic = new ImageIcon("Icons/rotateClockwise.png")
  val counterclockwisePic = new ImageIcon("Icons/rotateCounterclockwise.png")
  val backgroundPic = ImageIO.read(new File("Icons/background.png"))
  
  val defaultFont = new Font("Castellar", 0, 30)

  val grid = new Display(gridSize, gridSize, Grid.colors, Grid.edges, blockSize)
  
  val currentPentamino = new Display(nextGridSize, nextGridSize, Game.currentPentamino.toVector, 
      Game.currentPentamino.twoBooleanEdges, smallBlockSize)
  
  val nextPentamino = new Display(nextGridSize, nextGridSize, Game.nextPentamino.toVector, 
      Game.nextPentamino.twoBooleanEdges, smallBlockSize)
  
  def scoreText = "Score: " + Game.score
  def levelText = "Level: " + Game.level
  def rowsText = "Rows: " + Game.rowsToNextLevel
  def updateLabels() = {
    score.text = scoreText
    level.text = levelText
    rows.text = rowsText
  }
  def updateHighscores() = {
    val scores = Highscore.getHighscoreListAsString
    for (i <- 0 until highscores.size) {
      highscores(i).text = s"${i+1}: ${scores(i)}"
      highscores(i).foreground = Color.WHITE
    }
  }
  
  def updateGrids() = {
    showHypo()
    currentPentamino.colors = Game.currentPentamino.toVector
    currentPentamino.edges  = Game.currentPentamino.twoBooleanEdges
    nextPentamino.colors = Game.nextPentamino.toVector
    nextPentamino.edges  = Game.nextPentamino.twoBooleanEdges
  }
  
  def showHypo() = {
    def hypoGrid = Grid.hypotheticalAdd(Game.currentPentamino, mousePosx, mousePosy)
    grid.colors = hypoGrid.colors
    grid.edges = hypoGrid.edges
  }
  
  val score = new Label{text = scoreText; preferredSize = new Dimension(250,45); font = defaultFont}
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
  }
  
  val flipVertically = new Button {
    preferredSize = new Dimension(100,100)
    icon = verticalPic
  }
  
  val rotateClockwise = new Button {
    preferredSize = new Dimension(100,100)
    icon = clockwisePic
  }
  
  val rotateCounterclockwise = new Button {
    preferredSize = new Dimension(100,100)
    icon = counterclockwisePic
  }
  
  val playButton = new Button {
    preferredSize = new Dimension(250, 50)
    text = "Play"
    font = defaultFont
  }
  
  val scoreButton = new Button {
    preferredSize = new Dimension(250,50)
    text = "Hi-Scores"
    font = defaultFont
  }
  
  val menuButton = new Button {
    text = "Menu"
    font = defaultFont
  }
  
  val quitButton = new Button {
    preferredSize = new Dimension(250,50)
    text = "Quit"
    font = defaultFont
  }
  
  val gameScreen = new GridBagPanel {
    override def paintComponent(g: Graphics2D) = {
      g.drawImage(backgroundPic, 0, 0, null)
    }
    focusable = true
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
    val scoreInfo = new Label{text = "Name, Score, Level, Rows"; font = defaultFont; foreground = Color.WHITE}
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.ipady = 25
    layout(scoreInfo) = c
    c.gridy += 1
    for (score <- highscores) {
      layout(score) = c
      c.gridy += 1
    }
    layout(menuButton) = c
  }
  
  def newGame = {
    Game.newGame
    mousePosx = 3
    mousePosy = 3
    updateLabels()
    updateGrids()
    frame.contents = gameScreen
    frame.repaint() 
  }
  
  val startGame = new MenuItem(Action("New game")(newGame))
  
  val endGame = new MenuItem(Action("End game")(gameOver)){enabled = false}
  
  
  def gameOver: Unit = {
    if (Highscore.isScoreEnough(Game.score, Game.level, Game.rows)) {
      val popup = Dialog.showInput(gameScreen, "Your score is eligible for the Highscore list!", "Highscore!", Dialog.Message.Info, initial = "Insert name")
      val name = popup.getOrElse("Anonymous").replace(' ', '_')
      val newRank = Highscore.setNewScore(name, Game.score, Game.level, Game.rows)
      frame.contents = highscoreScreen
      updateHighscores()
      highscores(newRank).foreground = Color.RED
      Game.newGame
    } else {
        val popup = Dialog.showConfirmation(gameScreen, "Game over! Do you want to play again?", "Game over", Dialog.Options.YesNo)
        if (popup == Dialog.Result.No) frame.contents = menuScreen
        else newGame
    }
    frame.repaint()
  }
  
  val frame: MainFrame = new MainFrame {
    
    title = "Pentaminoes"
    resizable = false
    location = new Point(200, 50)
    preferredSize = windowSize

    menuBar = new MenuBar {
      contents += new Menu("Game") {
        contents += startGame
        contents += endGame
      }
    }
    contents = menuScreen
    menuScreen.requestFocus
    
    listenTo(grid.mouse.clicks, gameScreen.mouse.moves, grid.mouse.moves, grid.keys, gameScreen.keys)
    listenTo(flipHorizontally, flipVertically, rotateClockwise, rotateCounterclockwise)
    listenTo(playButton, scoreButton, menuButton, quitButton)
    reactions += {
      case MouseClicked(_, point, _, _, _)  => {
        Game.placePentamino(point.x / blockSize, point.y / blockSize)
        updateGrids()
        updateLabels()
        frame.repaint()
        if (!Game.gameOn)gameOver
      }
      case MouseMoved(component, point, _) => {
        if (component == grid) {
          mousePosx = point.x / blockSize
          mousePosy = point.y / blockSize
        }
        updateGrids()
        frame.repaint()
        gameScreen.requestFocus
      }
      case ButtonClicked(source) => {
        if (source == flipHorizontally) Game.currentPentamino.flipHorizontal()
        else if (source == flipVertically) Game.currentPentamino.flipVertical()
        else if (source == rotateClockwise) Game.currentPentamino.rotateClockwise()
        else if (source == rotateCounterclockwise) Game.currentPentamino.rotateCounterClockwise()
        else if (source == playButton)  newGame
        else if (source == scoreButton) {this.contents = highscoreScreen; updateHighscores()}
        else if (source == menuButton)  this.contents = menuScreen
        else if (source == quitButton)  dispose()
        endGame.enabled = this.contents(0) == gameScreen
        updateGrids()
        frame.repaint()
        gameScreen.requestFocus
      }
      case KeyPressed(_, key, _, _) => {
        if (key == Key.A)      Game.currentPentamino.rotateCounterClockwise()
        else if (key == Key.D) Game.currentPentamino.rotateClockwise()
        else if (key == Key.W) Game.currentPentamino.flipVertical()
        else if (key == Key.S) Game.currentPentamino.flipHorizontal()
        else if (key == Key.Up)    mousePosy = Math.max(mousePosy - 1, 0)
        else if (key == Key.Down)  mousePosy = Math.min(mousePosy + 1, gridSize - 1)
        else if (key == Key.Right) mousePosx = Math.min(mousePosx + 1, gridSize - 1)
        else if (key == Key.Left)  mousePosx = Math.max(mousePosx - 1, 0)
        else if (key == Key.Enter) {
          Game.placePentamino(mousePosx, mousePosy)
          mousePosx = 3
          mousePosy = 3
        }
        updateGrids()
        updateLabels()
        frame.repaint()
        if (!Game.gameOn) gameOver
      }
    }
  }
  
  def top: MainFrame = frame
}
  
  
  
  
  