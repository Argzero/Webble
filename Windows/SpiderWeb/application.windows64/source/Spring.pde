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
    
    L = force.mag()/0.5;
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


