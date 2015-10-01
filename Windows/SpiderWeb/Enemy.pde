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
        type = int(random(2));
        target = eWeb.nodes.get(curr.index-7);
      }
    }
    else if (curr.index>0)
    {
      type = int(random(2));
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
              type = int(random(2));
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
              type = int(random(2));
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
