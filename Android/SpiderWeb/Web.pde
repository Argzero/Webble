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

  void makeWeb()
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
          items.add(new Item(nodes.get(int(random(24, 42)))));
          stockPile--;
        }
        else if (random(100)>95 && level > 15 && items.size()<3)
        {
          items.add(new Item(nodes.get(int(random(15, 42)))));
          stockPile--;
        }
      } 
      Enemy e = new Enemy(this, 1, nodes.get(int(random(36, 42))));
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

