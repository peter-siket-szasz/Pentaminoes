package gui

import pentaminoes._
import Game.grid
import scala.swing._
import scala.swing.event._
import java.awt.Color
import scala.swing.GridBagPanel._
import scala.util.Random
import javax.swing. { UIManager, ImageIcon }

object GameWindow extends SimpleSwingApplication {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  
  val gridWidth = Game.gridColors(0).size
  val gridHeight = Game.gridColors.size
  val blockSize = 50
  val smallBlockSize = 25
  val gridDimesnion = new Dimension(gridWidth * blockSize, gridHeight * blockSize)
  val nextGridSize = 5
  val nextGridDimension = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
  
  val verticalPic = new ImageIcon("Icons/flipVertical.png")
  val horizontalPic = new ImageIcon("Icons/flipHorizontal.png")
  val clockwisePic = new ImageIcon("Icons/rotateClockwise.png")
  val counterclockwisePic = new ImageIcon("Icons/rotateCounterclockwise.png")
  

  val numbersToColors = Vector(Color.WHITE, Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.MAGENTA)
  
  def paintLinesAndSquares(g: Graphics2D, colors: grid, blockSize: Int) = {
    
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
  }
  
  val grid = new GridPanel(gridWidth, gridHeight) {     
      preferredSize = new Dimension(gridWidth * blockSize, gridHeight * blockSize)
      focusable = true
      override def paintComponent(g: Graphics2D) =  paintLinesAndSquares(g, Game.gridColors, blockSize)
    }
  
  val currentPentamino = new GridPanel(nextGridSize, nextGridSize) {
    preferredSize = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
    focusable = false
    override def paintComponent(g: Graphics2D) = paintLinesAndSquares(g, Game.currentPentamino.toVector, smallBlockSize)
  }
  
  val nextPentamino = new GridPanel(nextGridSize, nextGridSize) {
    preferredSize = new Dimension(nextGridSize * smallBlockSize, nextGridSize * smallBlockSize)
    focusable = false
    override def paintComponent(g: Graphics2D) = paintLinesAndSquares(g, Game.nextPentamino.toVector, smallBlockSize)
  }
  
  private def scoreText = "Score: " + Game.score
  private def levelText = "Level: " + Game.level
  private def rowsText = "Rows: " + Game.rowsToNextLevel
  private def updateLabels = {
    score.text = scoreText
    level.text = levelText
    rows.text = rowsText
  }
  
  val score = new Label{text = scoreText; preferredSize = new Dimension(100,45)}
  val level = new Label{text = levelText; preferredSize = new Dimension(100,45)}
  val rows = new Label{text = rowsText; preferredSize = new Dimension(100,45)}
  
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
    
  val screen = new GridBagPanel {
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.gridwidth = 6
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
  
  val newGame = Action("New game") { Game.newGame; updateLabels; screen.repaint }
    
  def top: MainFrame = new MainFrame {
    
    title = "Pentaminoes"
    resizable = false
    location = new Point(200, 50)
    preferredSize = new Dimension(1000, 900)

    menuBar = new MenuBar {
      contents += new Menu("Game") {
        contents += new MenuItem(newGame)
      }
    }
    contents = screen
    
    listenTo(grid.mouse.clicks, grid.keys)
    listenTo(flipHorizontally, flipVertically, rotateClockwise, rotateCounterclockwise)
    reactions += {
      case MouseClicked(grid, point, _, _, _)  => {
        Game.placePentamino(point.x / blockSize, point.y / blockSize)
        updateLabels
        screen.repaint()
      }
      //case KeyPressed(grid, _, _, _) => {
      //  Game.gridColors(Random.nextInt(gridWidth))(Random.nextInt(gridHeight)) = Random.nextInt(4)
      //  grid.repaint()
      //}
      case ButtonClicked(source) => {
        if (source == flipHorizontally) Game.currentPentamino.flipHorizontal()
        else if (source == flipVertically) Game.currentPentamino.flipVertical()
        else if (source == rotateClockwise) Game.currentPentamino.rotateClockwise()
        else if (source == rotateCounterclockwise) Game.currentPentamino.rotateCounterClockwise()
        screen.repaint
      }
    }
  }
}
  
  
  
  
  