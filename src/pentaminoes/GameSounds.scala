package pentaminoes

import java.io.File
import javax.sound.sampled.AudioSystem

object GameSounds {
  
  private val BGMFile = AudioSystem.getAudioInputStream(new File("sounds/music.wav"))
  private val PSFile = AudioSystem.getAudioInputStream(new File("sounds/placementSound.wav"))
  private val LineFile = AudioSystem.getAudioInputStream(new File("sounds/lineSound.wav"))
 
  private val BGMClip = AudioSystem.getClip()
  private val PSClip = AudioSystem.getClip()
  private val LineClip = AudioSystem.getClip()
  
  BGMClip.open(BGMFile)
  PSClip.open(PSFile)
  LineClip.open(LineFile)

  private var musicMuted = false
  private var effectsMuted = false

  //plays the background music if the game isn't muted
  def playMusic(): Unit = {
    if (!musicMuted) {
      BGMClip.setMicrosecondPosition(0)
      BGMClip.loop(20)
    }
  }

  //used for playing a sound when a pentamino is placed, if sound effects aren't muted
  def playPlacementSound() = {
    if (!effectsMuted) {
      PSClip.setMicrosecondPosition(0)
      PSClip.start()
    }
  }

  //used for playing a sound when a line is cleared, if sound effects aren't muted
  def playLineSound() = {
    if (!effectsMuted) {
      LineClip.setMicrosecondPosition(0)
      LineClip.start()
    }
  }
  
  //changes the state of musicMuted and stops/starts the background music accordingly.
  def muteMusic() = {
    musicMuted = !musicMuted
    if (musicMuted) {
      BGMClip.stop()
    } else {
      playMusic()
    }
  }

  //stops the background music from playing. Used when quitting game and entering menu. 
  def stopMusic() = {
    BGMClip.stop()
  }
  
  //changes the state of effectsMuted, for silencing sound effects
  def muteEffects() = effectsMuted = !effectsMuted
}