package pentaminoes

import java.io.File
import javax.sound.sampled.AudioSystem

object GameSounds {
  
  private val BGMFile = AudioSystem.getAudioInputStream(new File("sounds/music.wav"))
  private val PSFile = AudioSystem.getAudioInputStream(new File("sounds/placementSound.wav"))
  
  private val BGMClip = AudioSystem.getClip()
  private val PSClip = AudioSystem.getClip()
  
  BGMClip.open(BGMFile)
  PSClip.open(PSFile)

  private var musicMuted = false
  private var effectsMuted = false

  def playMusic(): Unit = {
    if (!musicMuted) {
      BGMClip.setMicrosecondPosition(0)
      BGMClip.loop(20)
    }
  }

  def playPlacementSound() = {
    if (!effectsMuted) {
      PSClip.setMicrosecondPosition(0)
      PSClip.start()
    }
  }

  def muteMusic() = {
    musicMuted = !musicMuted
    if (musicMuted) {
      BGMClip.stop()
    } else {
      playMusic()
    }
  }

  def stopMusic() = {
    BGMClip.stop()
  }
  
  def muteEffects() = effectsMuted = !effectsMuted
}