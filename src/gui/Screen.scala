package gui
import scala.swing.{ GridBagPanel, Graphics2D }
import javax.swing.ImageIcon


//A simple class with the paintComponent overridden with the background picture.
class Screen extends GridBagPanel {
  
  private val backgroundPic = new ImageIcon("Icons/background.png")
  
  val c = new Constraints
  
  override def paintComponent(g: Graphics2D) = {
    g.drawImage(backgroundPic.getImage, 0, 0, null)
  }
  
}