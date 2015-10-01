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
  
  void boostHealth(int value)
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
