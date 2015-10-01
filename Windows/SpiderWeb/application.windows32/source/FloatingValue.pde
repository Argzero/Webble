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
  
  void update()
  {
    timer--;
    fade=timer;
  }
  
  void display()
  {
    fill(0,0,255,2*timer+105);
    textSize(50-2*map(timer,0,125,4,0));
    text(((number>0)? "+ ":"- ") + abs(number), loc.x, loc.y-(125f-2*timer));
  }
}
