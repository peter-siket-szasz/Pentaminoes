package gui
import pentaminoes._
import Game.grid
import scala.swing.{ GridPanel, Graphics2D, Dimension }
import java.awt.Color

class Display(width: Int, height: Int, var colors: grid, var edges: Vector[Vector[Vector[Boolean]]], blockSize: Int) 
  extends GridPanel(width, height) {
    preferredSize = new Dimension(width * blockSize, width * blockSize)

    override def paintComponent(g: Graphics2D) = 
      Display.paintLinesAndSquares(g, colors, edges, blockSize)
  }



object Display {
  
  val numbersToColors = Vector(Color.WHITE, Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.MAGENTA)
  
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
    
  
}