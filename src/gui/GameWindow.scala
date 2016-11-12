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
  

  val numbersToColors = Vector(Color.WHITE, Color.GREEN, Color.BLUE, Color.RED)
  
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
    for (x <- 1 until sidex; y <- 1 until sidey) {
      g.drawLine(x * blockSize, 0, x * blockSize, sidey * blockSize)
      g.drawLine(0, y * blockSize, sidex * blockSize, y * blockSize)
    }
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
    
    preferredSize = new Dimension(nextGridSize * blockSize, nextGridSize * blockSize)
    focusable = false
    
    override def paintComponent(g: Graphics2D) = paintLinesAndSquares(g, Game.nextPentamino.toVector, smallBlockSize)
  }
  
  val flipHorizontally = new Button {
    preferredSize = new Dimension(100,100)
    icon = horizontalPic
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
  
  
  val nextPentaminoes = new GridBagPanel {
    val c = new Constraints
    c.gridx = 1
    c.gridy = 0
    c.insets = new Insets(0,-120,0,0)
    layout(flipHorizontally) = c
    c.gridx = 0
    c.gridy = 1
    c.insets = new Insets(0,25,0,25)
    layout(rotateCounterclockwise) = c
    c.gridx = 2
    c.gridy = 1
    c.insets = new Insets(0,-100,0,0)
    layout(rotateClockwise) = c
    c.gridx = 1
    c.gridy = 1
    c.insets = new Insets(25,-125,0,0)
    layout(currentPentamino) = c
    c.gridx = 1
    c.gridy = 2
    c.insets = new Insets(25,0,0,0)
    layout(nextPentamino) = c
  }
    
  val screen = new GridBagPanel {
    val c = new Constraints
    c.gridx = 0
    c.gridy = 0
    c.insets = new Insets(0,0,0,0)
    layout(grid) = c
    c.gridx = 1
    c.gridy = 0
    c.insets = new Insets(0,0,0,0)
    layout(nextPentaminoes) = c
  }
  
  val newGame = Action("New game") { Game.newGame; screen.repaint }
    
  def top: MainFrame = new MainFrame {
    
    title = "Pentaminoes"
    resizable = false
    location = new Point(200, 100)
    preferredSize = new Dimension(1000, 900)

    menuBar = new MenuBar {
      contents += new Menu("Game") {
        contents += new MenuItem(newGame)
      }
    }
    contents = screen
    
    listenTo(grid.mouse.clicks, grid.keys)
    listenTo(flipHorizontally, rotateClockwise, rotateCounterclockwise)
    reactions += {
      /*case MouseClicked(grid, point, _, _, _)  => {
        Game.gridColors (point.x / blockSize)(point.y / blockSize) = 3
        grid.repaint()
      }
      case KeyPressed(grid, _, _, _) => {
        Game.gridColors(Random.nextInt(gridWidth))(Random.nextInt(gridHeight)) = Random.nextInt(4)
        grid.repaint()
      }*/
      case ButtonClicked(source) => {
        if (source == flipHorizontally) Game.currentPentamino.flipHorizontal()
        else if (source == rotateClockwise) Game.currentPentamino.rotateClockwise()
        else if (source == rotateCounterclockwise) Game.currentPentamino.rotateCounterClockwise
        screen.repaint
      }
    }
  }
}
  
  
  
  
  