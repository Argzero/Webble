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
