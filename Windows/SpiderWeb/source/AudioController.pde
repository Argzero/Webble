public class AudioController
{
  Minim minim;
  AudioPlayer bM;
  AudioInput input;
  AudioSample kill;
  AudioSample gameOver;
  AudioSample powerUp;
  AudioSample Ow;
  AudioSample levelup;
  
  public AudioController(SpiderWeb thing)
  {
    minim = new Minim(thing);
    bM = minim.loadFile("leaves.wav");
    bM.setGain(35);   
    
    levelup = minim.loadSample("level.wav");
    levelup.setGain(-7);
    
    Ow = minim.loadSample("ow.wav");
    Ow.setGain(6);
    
    kill = minim.loadSample("sfx.wav");
    kill.setGain(-14);    
    
    powerUp = minim.loadSample("powerup.wav");
    powerUp.setGain(-6);
    
    gameOver = minim.loadSample("gameover.wav");
    gameOver.setGain(-2);
    
    input = minim.getLineIn();
    bM.loop();
  }
  
  public void KillSound()
  {
    kill.trigger();
  } 
  
  public void LevelUp()
  {
    levelup.trigger();
  }
  
  public void PowerUp()
  {
    powerUp.trigger();
  }
  
  public void GameOver()
  {
    bM.pause();
    gameOver.trigger();
  }
  
  public void restartBM()
  {
    bM.pause();
    bM = minim.loadFile("leaves.wav");
    bM.loop();
  }
  
  public void OW()
  {
    Ow.trigger();
  }
}
