import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

import java.util.*;

Web web;
boolean freeze;
boolean playing;
boolean instructions;
boolean prePressed;
PImage ins;
PImage titleScreen;
PImage titleScreen2;
int fade;
boolean freeOn;
int reds = 0;
AudioController audio;
boolean pause;
boolean pauseButtoned;
boolean restarting;

void setup()
{  
  size(1280, 720);
  restarting = false;
  pause = false;
  prePressed = false;
  freeOn = false;
  pauseButtoned = false;
  audio = new AudioController(this);
  instructions = false;
  ins = loadImage("ins.png");
  ins.resize(1280, 720);
  background(0, 0, 0); //orange  background(100); //grayscale  //single int = grayscale
  stroke(255, 255, 255); // RGB color of lines and such
  web = new Web(7, width/2, height/2);
  web.score = 0;
  fade=0;
  freeze = false;
  playing = false;
  titleScreen = loadImage("title.png");
  titleScreen.resize(1280, 720);
  titleScreen2 = loadImage("title2.png");
  titleScreen2.resize(1280, 720);
}

void restart()
{
  web = new Web(7, width/2, height/2);
  web.score = 0;
  fade=0;
  audio.restartBM();
  smooth();
  freeze = false;
  playing = false;
  pause = false;
}

void draw()
{ 
  if (restarting)
  {
    imageMode(CORNER);
    playing = false;
    instructions = false;
  }
  restarting = false;
  for (;freeze;)
  {
  }

  if (playing)
  {
    rectMode(CORNER);
    fill(0,0,0,150);
    rect(0,0, width, height);
      
    if (!pause)
    {
      if(!(web.lose.yes && !freeOn))
      {
        web.update();

        web.update();

        web.update();

        web.draw();
      }
      
      if (!keyPressed)
      {
        pauseButtoned = false;
      }
      if (keyPressed && key == 'p' && !pauseButtoned)
      { 
        pause = true;
        pauseButtoned = true;
      }
    }
    else
    {  
      rectMode(CORNER);
      fill(0,0,0,20);
      rect(0,0, width, height);
      textAlign(CENTER);
      textSize(32);
      fill(255, 255, 0, 255);
      text("Paused: Press 'p' to return or 'r' to restart", width/2, height/2);

      if (!keyPressed)
      {
        pauseButtoned = false;
      }
      if (keyPressed && key == 'p' && !pauseButtoned)
      { 
        pause = false;
        pauseButtoned = true;
      }
      if (keyPressed && key == 'r' && !pauseButtoned)
      {
        restarting = true;
        restart();
        pauseButtoned = true;
      }
    }

    fill(255);
    textAlign(RIGHT);
    textSize(32);
    text("Score: " + web.score, width, 32);
    if (!restarting)
    {
      int hp = web.health;
      int hp2 = web.player.health;

      fill(0, 0, 100, 150);
      rect(0, 0, width, 85);

      fill(4*(50-hp)+((hp/2)-25), 4*(hp)-hp, 0);
      textAlign(LEFT);
      textSize(32);
      text("Web HP: " + web.health, 36, 32);


      fill(2*(100-hp2)+((hp2/2)-50), 2*(hp2)+(hp2/2), 0);
      textAlign(LEFT);
      textSize(32);
      text("Spider HP: " + web.player.health, 36, 72);


      fill(50, 200, 0, 185);
      textAlign(CENTER);
      textSize(72);
      if (web.level<24)
        text("LEVEL: " + (web.level + 1), width/2, 72);
      else
        text("LEVEL: " + ("MAX!"), width/2, 72);

      fill(200, 200, 0, 185);
      textAlign(CENTER);
      textSize(36);
      int i = web.player.specTime;
      if (i < 0)
        i=0;
      text("Kill Mode: \n" + (i), 124, height/4 - 50);
    }
    if (web.lose.yes && !freeOn && !restarting)
    {
      
      textAlign(CENTER);
      textSize(200);
      stroke(255, 0, 0);
      if (fade<180)
        fade++;
      fill(255, 255, 0, fade);
      if (!pause)
      {
        background(0,2);
        
        text("YOU LOSE!", width/2, height/2);
        textAlign(CENTER);
        textSize(32);
        fill(0, 255, 0, fade+70);
        text("Press 'p' to pause or 'r' to restart", width/2, 60 + height/2);
      }

      if (!keyPressed)
        pauseButtoned = false;

      if (keyPressed && key == 'r' && !pauseButtoned)
      {
        restart();
        restarting = true;
        pauseButtoned = true;
      }
    }
  }
  else if (!instructions && !pause)
  {    
    if (!freeOn)
    {
      image(titleScreen, 0, 0);
    }
    else
    {
      image(titleScreen2, 0, 0);
    }

    if (mouseX < 273 && mouseX > 218 && mouseY < 135 && mouseY > 85) 
    {
      fill(255, 255, 0, 125);
      rect(218, 85, 55, 50);
    }
  }
  else if (!pause)
  {
    image(ins, 0, 0);
  }
}

void mousePressed()
{
  if (!playing && !prePressed)
  {
    if (mouseX < 273 && mouseX > 218 && mouseY < 135 && mouseY > 85) 
    {
      freeOn = !freeOn;
    }
    else
    {
      if (!instructions)
      {
        instructions = true;
      }
      else
        playing = true;
    }
  }
  prePressed=true;
  web.mousePressed();
}

void mouseReleased()
{
  prePressed = false;

  web.mouseReleased();
}

