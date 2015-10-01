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
