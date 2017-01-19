package gui
import pentaminoes._
import Game.grid
import scala.swing.{ GridPanel, Graphics2D, Dimension }
import java.awt.Color

class Display(width: Int, height: Int, var colors: grid, var edges: Vector[Vector[Vector[Boolean]]], blockSize: Int) 
  extends GridPanel(width, height) {
    preferredSize = new Dimension(width * blockSize, height * blockSize)

    //Override paintComponent with the paint method defined in the companion object
    override def paintComponent(g: Graphics2D) = 
      Display.paintLinesAndSquares(g, colors, edges, blockSize)
  }


object Display {
  
  //This Vector is used to convert int values to colors for drawing
  val numbersToColors = Vector(Color.WHITE, Color.YELLOW, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.BLUE)
  
  //Paints the edges of pentaminoes in grids. Each square has a (Boolean, Boolean) value. The first value determines if
  //the square has the top edge, the second one the left edge.
  def paintEdges(g: Graphics2D, edges: Vector[Vector[Vector[Boolean]]], blockSize: Int) = {
    val height = edges(0).size
    val width = edges.size
    g.setColor(Color.BLACK)
    for (row <- 0 until height) {
      for (col <- 0 until width) {
        //No edges are drawn at the sides of grids. Horizontal edges here, 3 pixels wide.
        if (edges(row)(col)(0) && row != 0) {
          for (i <- -1 to 1) {
            g.drawLine(col * blockSize, row * blockSize + i, (col + 1) * blockSize, row * blockSize + i)
          }
        }
        //Vertical edges here.
        if (edges(row)(col)(1) && col !=0) {
          for (i <- -1 to 1) {
            g.drawLine(col * blockSize + i, row * blockSize, col * blockSize + i, (row + 1) * blockSize)
          }
        }
      }
    }
  }
  
  //Paints the whole grid. First the colored squares, then the grid, and finally the edges for pentaminoes.
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