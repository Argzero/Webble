import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.spi.*; 
import ddf.minim.signals.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.ugens.*; 
import ddf.minim.effects.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SpiderWeb extends PApplet {










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

public void setup()
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

public void restart()
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

public void draw()
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

public void mousePressed()
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

public void mouseReleased()
{
  prePressed = false;

  web.mouseReleased();
}

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
class Enemy
{
  PImage img;
  PImage altImg;
  Node curr;
  Node target;
  Node cSpot;
  PVector pos;
  float mass = 12;
  boolean moving;
  boolean player;
  float iteration;
  float direction;
  int type;
  Web eWeb;
  int SPEED = 200;
  int time;
  boolean Type = false;
  int deathIterator;
    
  FloatingValue[] pointsPlus = new FloatingValue[16];
  int plusNum;
  
  public Enemy(Web web, int _type, Node loc)
  {
    plusNum = 0;
    time = 0;
    eWeb = web;
    type = _type;
    curr = loc;
    pos = curr.p.get();
    deathIterator =0;
    img = loadImage("enemy.png");
    altImg = loadImage("enemy1.png");
    
    if(random(10)>5 && web.level>0 && reds < 10 *log(50*eWeb.level))
    {
      Type = true;
    } 
    
    if(Type)
    {
      img = loadImage("enemytype2.png");
      altImg = img;
      reds++;
    }
    
    moving = false;
    iteration = 0;
    player = false;
    direction = 0;
    setTarget();
  }
  
  public boolean collision(Spider s)
  {
    if(PVector.dist(pos,s.pos)<s.img.width)
    {
      reds--;
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public void setTarget()
  {
    moving = true;
    if(type == 0)
    {
      if(curr.index >7)
      {
        type = PApplet.parseInt(random(2));
        target = eWeb.nodes.get(curr.index-7);
      }
    }
    else if (curr.index>0)
    {
      type = PApplet.parseInt(random(2));
      target = eWeb.nodes.get(curr.index-1);
    }
  }
  
  public void update()
  { 
    time++;
    if(time>=2)
      time = 0;
    
    for(int i =0; i < 7; i++)
    {
      if(pointsPlus[i] !=null)
      {
        pointsPlus[i].update();
        pointsPlus[i].display();
      }
    }
      
    if(Type)
    {
      enemy2Update();
    
      if(abs(PVector.dist(pos,eWeb.player.pos))<eWeb.player.img.width)
      {
        deathIterator++;
        if(deathIterator == 25)
        {
          deathIterator = 0;
          pointsPlus[plusNum] = new FloatingValue(pos,-1,eWeb.player);
          eWeb.hurt = true;
          plusNum++;
          if(plusNum > pointsPlus.length-1)
            plusNum = 0;
        }  
        
        if(eWeb.player.health <= 0)
        {
          eWeb.player.health = 0;
          if(eWeb.lose.yes == false)
            audio.GameOver();
          eWeb.lose.yes = true;
        }
      }
      else
      {
        deathIterator = 24;
      }
      
      if(deathIterator == 25)
      {
        deathIterator = 0;
        pointsPlus[plusNum] = new FloatingValue(pos,-1,eWeb.player);
        plusNum++;
        if(plusNum > 7)
          plusNum = 0;
      }  
    }
    if(!Type)
    {
      if(moving&&curr.index>7)
      {
        float newV = SPEED-12*(8-curr.row) - 2*web.level;
          if(newV <30)
          {
            newV = 25;  
          }
          float newX = map(iteration, 0, newV, curr.p.x, target.p.x);
          float newY = map(iteration, 0, newV, curr.p.y, target.p.y); 
          iteration+=1;
          pos = new PVector(newX,newY);
          
          if(pos.x == target.p.x || iteration > newV)
        {
          pos = target.p.get();
          moving = false;
          curr = target;
          target = null;
          iteration = 0;
          if(curr.index>=7)
            setTarget();
          else
          {
            moving = true;
            
            if (curr.index>0)
            {
              type = PApplet.parseInt(random(2));
              target = eWeb.nodes.get(curr.index-1);
            }
          }
        }
      }
      else if(moving && !Type)
      {
        if(curr.index == 0)
        {
          deathIterator++;
          if(deathIterator == 20)
          {
            eWeb.health--;
            deathIterator = 0;
            pointsPlus[plusNum] = new FloatingValue(pos,-1, null);
            plusNum++;
            eWeb.hurt = true;
            if(plusNum > 7)
              plusNum = 0;
          }  
          
          if(eWeb.health <= 0)
          {
            eWeb.health = 0;
            if(eWeb.lose.yes == false)
              audio.GameOver();
            eWeb.lose.yes = true;
          }
        }
        else if(pos != null && target != null && curr != null)
        {
          float newV = SPEED-12*(8-curr.row) - 2*web.level;
          if(newV <30)
          {
            newV = 25;  
          }
          float newX = map(iteration, 0, newV, curr.p.x, target.p.x);
          float newY = map(iteration, 0, newV, curr.p.y, target.p.y); 
          iteration+=1;
          pos = new PVector(newX,newY);
          
          if(pos.x == target.p.x || iteration > newV)
          {
            pos = target.p.get();
            moving = false;
            curr = target;
            target = null;
            iteration = 0;
            
            moving = true;
            
            if (curr.index>0)
            {
              type = PApplet.parseInt(random(2));
              target = eWeb.nodes.get(0);
            }
          }
        }
      }
    }
  }
  
  public void setTarget(Node node)
  {
    if(!moving)
    {
      moving = true;
      target = node;
      direction = PVector.sub(curr.p,node.p).heading();
    }
  }
  
  public void enemy2Update()
  {
    if(web.spiderNode != null)    
      cSpot = web.spiderNode;
    else if(web.player.curr != null)    
      cSpot = web.player.curr;
    
    if(!moving)
    {
      pos = curr.p.get();
    }
    
    if(moving)
    {
      float newV = SPEED-15*web.level;
      if(newV <30)
      {
        newV = 25;  
      }
      
      float newX = map(iteration, 0, newV, curr.p.x, target.p.x);
      float newY = map(iteration, 0, newV, curr.p.y, target.p.y); 
      iteration++;
      pos = new PVector(newX,newY);
      
      if(pos.x == target.p.x || iteration>=newV)
      {
        pos = target.p.get();
        moving = false;
        curr = target;
        target = null;
        iteration = 0;
        
        print(curr.index);
      }
    }
    else if(cSpot != null)
    {
      if(target != cSpot)
      {
        int currLoc = curr.index;
        int targetLoc = cSpot.index;
        int currPoint = curr.point;
        int targetPoint = cSpot.point;
        
        if(abs(currPoint - targetPoint)<4 && currPoint!=targetPoint)
        {
          if(currPoint<targetPoint)
          {
            if(currLoc+1<web.nodes.size() && web.nodes.get(currLoc+1)!= null)
              setTarget(web.nodes.get(currLoc+1)); 
          }
          else if(targetPoint<currPoint)
          {
            setTarget(web.nodes.get(currLoc-1));
          }
        }
        else if(currLoc + 7 <= targetLoc)
        {
          setTarget(web.nodes.get(currLoc+7));
        }
        else if(currLoc - 7 >= targetLoc)
        {
          setTarget(web.nodes.get(currLoc-7));
        }
        else
        {
          if(abs(currLoc - targetLoc)<4 && currLoc!=targetLoc)
          {
            if(currLoc<targetLoc)
            {
              setTarget(web.nodes.get(currLoc+1)); 
            }
            else if(targetLoc<currLoc)
            {
              setTarget(web.nodes.get(currLoc-1));
            }
          }
          else
          {
            if(currLoc<targetLoc && currLoc+7<web.nodes.size())
            {
              setTarget(web.nodes.get(currLoc+7)); 
            }
            else if(targetLoc<currLoc && currLoc-7>0)
            {
              setTarget(web.nodes.get(currLoc-7));
            }
            else if(currLoc<targetLoc)
            {
              setTarget(web.nodes.get(currLoc+1)); 
            }
            else if(targetLoc<currLoc)
            {
              setTarget(web.nodes.get(currLoc-1));
            }
          }
        }
      }
      else
      {
        cSpot = null;
      }
    }
    else
    {
      iteration = 0;
      moving = false;
      if(target!=null)
      {
        curr = target;
      }
      target = null;
      pos = curr.p.get();
    }
  }
}
public class FloatingValue
{
  int timer;
  int number;
  PVector loc;
  int fade;
  public FloatingValue(PVector _p, int _n, Spider s)
  {
    if(s!=null)
      s.health--;
    timer = 62;
    number = _n;
    loc = _p;
  }
  
  public void update()
  {
    timer--;
    fade=timer;
  }
  
  public void display()
  {
    fill(0,0,255,2*timer+105);
    textSize(50-2*map(timer,0,125,4,0));
    text(((number>0)? "+ ":"- ") + abs(number), loc.x, loc.y-(125f-2*timer));
  }
}
public class Item
{
  Spider spidey;
  int point;
  Node node;
  
  PImage img;
  ArrayList<Node> nodes;
  PVector p, pP;
  
  public PVector Point()
  {
    //println("x: " + p.x + ", y: " + p.y);
    return p;
  }
 
  public void draw()
  {    
    spidey = web.player;
  }
  
  public Item(Node _n)
  {
    node = _n;
    p = _n.Point();
    img = loadImage("Item.png");
  }
}
class LoseChecker
{
  boolean yes = false;
}
class Node
{
  int index;
  Spider spidey;
  boolean selected;
  boolean near; 
  boolean last, start;
  boolean pressed;
  int point;
  int row;
  
  ArrayList<Node> nodes;
  boolean edge, center, moving;
  float mass =20;
  int pIndex; // determines orientation from the center for spider movement
  float damping= 0.85f;
  
  float maxV;
  
  PVector p, pP;
  PVector v;  
  PVector a;
  
  public void applyForce(PVector force)
  {
    PVector accel = PVector.div(force,mass);
    a.add(accel);
  }
  
  public void setIndex(int val)
  {
    index = val;
    if(index > 36)
    {
      edge = true;
    }
    
    point = index%7;
    row = 0;
    int copyIndex = index;
    while (copyIndex>7)
    {
      copyIndex-=7;
      row++;
    }
  }
    
  public void mousePressed()
  {
    pressed = true;
    if(index<3 && index!=0)
      pressed = false;
    if(!near)
    {
      pressed = false;
    }
  }
  
  public void mouseReleased()
  {
    pressed = false;
  }
    
  public PVector Point()
  {
    //println("x: " + p.x + ", y: " + p.y);
    return p;
  }
 
  public void draw()
  {    
    spidey = web.player;
    
    if(near&&pressed)
    {
      //p = new PVector(mouseX,mouseY);
      if(index != web.player.curr.index-1 && index != web.player.curr.index+1 && index != web.player.curr.index-7 && index != web.player.curr.index+7)
      {  web.current = this; }
      spidey = web.player;
      if(web.player!=null)
      {   web.player.clickSpot = web.nodes.get(index);   }
    }
    else if(pressed)
    {
      selected = false;
    }
    
    if(!edge)
    {
      v.add(a);
      v.limit(maxV);
      p.add(v);      
      v.mult(damping);
      a.mult(0);
    }
        
    PVector fMouse = new PVector(p.x-mouseX,p.y-mouseY);
    
    if(fMouse.mag()<30&&(abs(mouseX-400)>30||abs(mouseY-400)>30))
    {
      near = true;
    }
    else
    {
      near = false;
    }
  
    noStroke();
    
    fill(0,0,0,100);
    if(near)
    {
      fill(0,255,0,185);
    }
    
    if(index>2 || index == 0)
      ellipse(p.x, p.y, 30,30);
      
    strokeWeight(3);
    
    int plusSize = 0;
    
    fill(255,255,0);
    if(near)
    {
      fill(0,255,0);
      plusSize = 5;
    }
    
    stroke(255,255);
    
    fill(255);
    
    if(index>2 || index == 0)
      ellipse(p.x, p.y, 12, 12);
      
    noStroke();
    
    fill(0,0,100,100);
    if(near)
    {
      fill(0,255,0,100);
    }
    
    if(index>2 || index == 0)
      ellipse(p.x, p.y, 30 + plusSize, 30 + plusSize);
    
    strokeWeight(3); 
    stroke(255,255,0,255);
    
    fill(255);
    
  }
  
  public Node(float _x, float _y)
  {
    p = new PVector(_x,_y);
    v = new PVector(0,0);
    a = new PVector(0,0);
    pressed = false;
    maxV = 40;
    pP = p.get();
  }
}
class Spider
{
  PImage img;
  PImage sImg;
  Node curr;
  Node target;
  PVector pos;
  float mass = 6f;
  boolean moving;
  boolean player;
  int iteration;
  float direction;
  Node clickSpot;
  int speed;
  Web sWeb;
  int health;
  boolean special;
  int specTime;
  int specTimeTime;
  
  public Spider(Web web)
  {
    curr = web.Center();
    pos = new PVector(0,0);
    img = loadImage("spider.png");
    sImg = img;
    moving = false;
    iteration = 0;
    player = false;
    direction = 0;
    sWeb = web;
    speed = 55;
    health = 50;
    specTime = 0;
    specTimeTime = 0;
  }
  
  public void move(Node node)
  {
    if(curr != node && node.index>=8)
    {
      PVector shift = PVector.sub(node.p, curr.p);
      shift.mult(mass);
      node.applyForce(shift);
      curr = node;
    }
  }
  
  public void boostHealth(int value)
  {
    health+=value;
    if(health >= (web.level*10) + 50)
      health = (web.level*10) + 50;
  }
  
  public void setTarget(Node node)
  {
    if(!moving)
    {
      moving = true;
      target = node;
      direction = PVector.sub(curr.p,node.p).heading();
    }
  }
  
  public void update()
  {
    if(specTime > 0)
      special = true;
    else
      special = false;
    
    if(specTime>0 && specTimeTime > 0)
      specTimeTime--;
    
    if(specTime>0 && specTimeTime == 0)
    {
      specTime--;
      specTimeTime = 10;
    }
    
    if(PVector.dist(pos,curr.p)>3*width/4)
    {
      curr = web.Center();
      moving = false;
      clickSpot = null;
    } 
    
    if(!moving && !player)
    {
      pos = curr.p.get();
    }  
    
    if(player)
    {
      if(moving)
      {
        float val = 75-5*web.level;
        val-=42-target.index;
        if(val<20)
          val = 20;
          
        if(curr.index+7 == target.index || curr.index-7 == target.index)
          val = 20;
        
        float newX = map(iteration, 0, val, curr.p.x, target.p.x);
        float newY = map(iteration, 0, val, curr.p.y, target.p.y); 
        iteration++;
        pos = new PVector(newX,newY);
        
        if(pos.x == target.p.x || iteration >= val)
        {
          pos = target.p.get();
          moving = false;
          curr = target;
          target = null;
          iteration = 0;
          
          print(curr.index);
        }
      }
      else if(clickSpot != null)
      {
        if(target != clickSpot)
        {
          int currLoc = curr.index;
          int targetLoc = clickSpot.index;
          int currPoint = curr.point;
          int targetPoint = clickSpot.point;
          
          if(currLoc == web.nodes.size()-1 && targetLoc <web.nodes.size()-6)
          {
            setTarget(web.nodes.get(currLoc-7));
          }
          
          else if(currLoc == 0 && targetLoc <8)
          {
            setTarget(web.nodes.get(targetLoc));
          }
          else if(currLoc < 8 && targetLoc == 0)
          {
            setTarget(web.nodes.get(targetLoc));
          }
          else if(abs(currPoint - targetPoint)<4 && currPoint!=targetPoint)
          {
            if(currPoint<targetPoint && currLoc < 43)
            {
              setTarget(web.nodes.get(currLoc+1)); 
            }
            else if(targetPoint<currPoint)
            {
              setTarget(web.nodes.get(currLoc-1));
            }
          }
          else if(currLoc + 7 <= targetLoc)
          {
            setTarget(web.nodes.get(currLoc+7));
          }
          else if(currLoc - 7 >= targetLoc)
          {
            setTarget(web.nodes.get(currLoc-7));
          }
          else
          {
            if(abs(currLoc - targetLoc)<4 && currLoc!=targetLoc)
            {
              if(currLoc<targetLoc)
              {
                setTarget(web.nodes.get(currLoc+1)); 
              }
              else if(targetLoc<currLoc)
              {
                setTarget(web.nodes.get(currLoc-1));
              }
            }
            else
            {
              if(currLoc<targetLoc && currLoc+7<web.nodes.size())
              {
                setTarget(web.nodes.get(currLoc+7)); 
              }
              else if(targetLoc<currLoc && currLoc-7>0)
              {
                setTarget(web.nodes.get(currLoc-7));
              }
              else if(currLoc<targetLoc)
              {
                setTarget(web.nodes.get(currLoc+1)); 
              }
              else if(targetLoc<currLoc)
              {
                setTarget(web.nodes.get(currLoc-1));
              }
            }
          }
          pos = curr.p.get();
        }
        else
        {
          clickSpot = null;
          pos = curr.p.get();
        }
      }
      else
      {
        iteration = 0;
        moving = false;
        if(target!=null)
        {
          curr = target;
        }
        target = null;
        pos = curr.p.get();
      }
    }
  }
}
class Spring
{
  float k, L;
  Node a,b;
  PVector dist;
  float extension; // extension
  float rest; // rest length
  
  public Spring(Node x, Node y)
  {
    a = x;
    b = y;
    extension = 0;
    k = 0.99f;
    PVector pA = a.Point();
    PVector pB = b.Point();
       
    dist = PVector.sub(pA,pB);
    rest = abs(dist.mag());
    
    if(a.index < 8)
    {
      k = 0;
    }
  }
  
  public void draw()
  {
    PVector pA = a.Point();
    PVector pB = b.Point();
       
    PVector force = PVector.sub(a.Point(),b.Point());
    
    L = force.mag()/0.5f;
    extension = L-rest;
    
    extension = force.mag() - rest;
    
    extension = constrain(extension, -15f, 15f);
    
    force.normalize();
    if(k!=0)
      force.mult(k*extension);
    else
      force.mult(extension);
      
    force.mult(10);
    b.applyForce(force.get());
    force.mult(-1);
    a.applyForce(force.get());   
    stroke(245,245,255,75);
    line(pA.x, pA.y, pB.x, pB.y);
  }
}


class Web
{
  int score;  
  int pts;
  int level;
  Node recent;
  Node current;
  Node center;
  int stockPile;
  ArrayList<Spring> springs;
  ArrayList<Node> nodes;
  float r, theta, rInc, nNodes;
  int sides;
  boolean start;
  float tempX, tempY;
  PVector loc;
  boolean hurt = false;
  Spider spidey;
  Spider player;
  ArrayList<Integer> remove;
  ArrayList<Enemy> enemies;
  int timer;
  LoseChecker lose;
  Node spiderNode;
  int health;
  ArrayList<FloatingValue> floatNums = new ArrayList<FloatingValue>();
  Node lastCen;
  ArrayList<Item> items;

  public Web(/*float a is 0,*/ /*float b=0,*/ int c, /*float d =0.8,*/ /*float e=NUM,*/ float centerPointX, float centerPointY)
  {
    health = 100;
    stockPile = 4;
    lose = new LoseChecker();
    start = true;
    level = 0;
    timer = 0;
    remove = new ArrayList<Integer>();
    enemies = new ArrayList<Enemy>();
    r = 0/*float a*/;
    theta = 0;
    sides = c;
    rInc = 9;
    nNodes = 43;
    loc = new PVector(centerPointX, centerPointY);
    pts=0;
    nodes = new ArrayList<Node>();
    springs = new ArrayList<Spring>();
    nodes.add(new Node(centerPointX, centerPointY + 55));
    center = nodes.get(0);
    center.setIndex(0);
  }

  public void mousePressed()
  {
    for (Node node: nodes)
    {
      node.mousePressed();
    }
  }

  public void mouseReleased()
  {
    PVector mouse = new PVector(mouseX, mouseY);

    for (Node node : nodes)  
    {
      PVector mNode = PVector.sub(node.p, mouse);

      if (mNode.mag() < rInc*3) 
      { 
        current = node;
      }
    }

    if (current != null && current!= spidey.curr)
    {
      if (recent!=null)
      {
        if (recent != current)
        {
          recent = current;
          spidey.move(current);
        }
      }
      else
      {
        recent = current;
        spidey.move(current);
      }
    }
    /*for(Node node: nodes)
     {
     node.mouseReleased();
     }*/
  }

  public Node Center()
  {
    return center;
  }

  public void makeWeb()
  {
    Node orig = nodes.get(0);
    nodes = new ArrayList<Node>();
    nodes.add(orig);
    int index = 0;
    r=0;
    while ( (r<width||r<height)&&index<nNodes)
    {
      index++;
      r+=1.2f*rInc;
      theta += TWO_PI/sides;
      tempX = (1.2f * r * cos(theta)) + loc.x;
      tempY = (0.73f * r * sin(theta)) + loc.y + 55;
      Node newNode = new Node(tempX, tempY);
      nodes.add(newNode);
      newNode.setIndex(index);  
      springs.add(new Spring(newNode, nodes.get(index-1)));  

      float val = theta;
      int times = 0;
      while (val > TWO_PI)
      {
        val-=TWO_PI;
        times++;
      }

      if (times > 0)
      {
        springs.add(new Spring(newNode, nodes.get(index-7)));
      }
      else
      {
        springs.add(new Spring(newNode, nodes.get(0)));
      }
    }

    player = new Spider(this);
    spidey = new Spider(this);
    player.curr = nodes.get(7);
    player.player = true;
  }

  public void update()
  {
    if (start)
    {
      items = new ArrayList<Item>();
      makeWeb();
      start = !start;
    }
    else
    {
      spidey.curr = Center();
      if (keyPressed && !player.moving)
      {
        if (keyCode == UP)
        {
          if (!player.curr.edge && !(player.curr.index==0 && lastCen!=null))
          {
            spiderNode = nodes.get(player.curr.index+7);
            player.setTarget(nodes.get(player.curr.index+7));
            player.clickSpot = null;
          }
          else if (player.curr.index==0 && lastCen!=null)
          {
            spiderNode = lastCen;
            player.setTarget(lastCen);
            player.clickSpot = null;
          }
          else if (player.curr.index+7 == 42)
          {
            spiderNode = nodes.get(player.curr.index+7);
            player.setTarget(nodes.get(player.curr.index+7));
            player.clickSpot = null;
          }
        }
        if (keyCode == DOWN)
        {
          if (player.curr.index>7 && player.target == null)
          {
            spiderNode = nodes.get(player.curr.index-7);
            player.setTarget(nodes.get(player.curr.index-7));
            player.clickSpot = null;
          }
          else if (player.curr.index<=7)
          {
            lastCen = player.curr;
            spiderNode = nodes.get(0);
            player.setTarget(nodes.get(0));
            player.clickSpot = null;
          }
        }
        if (keyCode == LEFT)
        {
          if (player.curr.index>0 && player.target == null)
          {
            spiderNode = nodes.get(player.curr.index-1);
            player.setTarget(nodes.get(player.curr.index-1));
            player.clickSpot = null;
          }
        }
        if (keyCode == RIGHT)
        {
          if (player.curr.index<42 && player.target == null)
          {
            spiderNode = nodes.get(player.curr.index+1);
            player.setTarget(nodes.get(player.curr.index+1));
            player.clickSpot = null;
          }
        }
      }
    }

    timer++;
    int limit = 200-(level*50);
    if (limit<25)
      limit = 50-level;
    if (level > 40)
      limit = 10;
    if (timer>limit)
    {
      if (stockPile>0)
      {
        if (random(15)>9+log(level) && level > 2 && items.size()<1)
        {
          items.add(new Item(nodes.get(PApplet.parseInt(random(24, 42)))));
          stockPile--;
        }
        else if (random(100)>95 && level > 15 && items.size()<3)
        {
          items.add(new Item(nodes.get(PApplet.parseInt(random(15, 42)))));
          stockPile--;
        }
      } 
      Enemy e = new Enemy(this, 1, nodes.get(PApplet.parseInt(random(36, 42))));
      e.moving = true;
      enemies.add(e);
      timer = 0;
    }
  }

  public void draw()
  {
    hurt = false;
    pts = score - (level * 450);
    if (pts>=450 && level < 24)
    {
      audio.LevelUp();  
      level++;
      stockPile = 3;
    }

    for (Spring spring: springs)
    {
      spring.draw();
    }  

    for (Node node: nodes)
    {
      node.draw();
      if (node.index<8)
      {
        line(node.p.x, node.p.y, nodes.get(0).p.x, nodes.get(0).p.y);
      }
    }   
    
    for (int i = enemies.size()-1; i>=0 && (!lose.yes||freeOn); i--)
    {
      Enemy e = enemies.get(i);
      if (e.time==0)
        image(e.img, e.pos.x, e.pos.y);
      else
        image(e.altImg, e.pos.x, e.pos.y);

      e.update();

      if (e.collision(player) && (!e.Type || player.special))
      {
        remove.add(i);
        audio.KillSound();
        enemies.remove(e);
      }
    }  
    for (int i = remove.size() - 1; i>=0; i--)
    {
      enemies.remove(remove.get(i));
      if ((!lose.yes||freeOn))
      {
        score+=100;
        player.boostHealth(3);
        //spidey.img.resize(spidey.img.width+1,spidey.img.height+1);
      }
    }

    remove = new ArrayList<Integer>();

    if ((!lose.yes||freeOn))
    {
      spidey.update();
      player.update();
    }
    imageMode(CENTER);

    pushMatrix();
    translate(player.pos.x, player.pos.y);
    rotate(player.direction + HALF_PI);
    image(spidey.img, 0, 0);
    popMatrix();

    ArrayList<Item> remover = new ArrayList<Item>();
    for (int i =0; i < items.size(); i++)
    {
      pushMatrix();
      translate(items.get(i).p.x, items.get(i).p.y);
      image(items.get(i).img, 0, 0);
      if (PVector.dist(player.pos, items.get(i).p)<spidey.img.width/2)
        remover.add(items.get(i));
      popMatrix();
    }

    for (Item i : remover)
    {
      items.remove(i);
      player.specTime+=25;
      if (player.specTime>80)
        player.specTime = 80;
    }

    if (remover.size()>0)
      audio.PowerUp();
      
    if(hurt)
      audio.OW();
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "SpiderWeb" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
