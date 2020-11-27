package com.focasoft.beterraba.entity;

import java.awt.Graphics;

public abstract class Entity
{
  protected String name;
  protected int x;
  protected int y;
  
  public Entity(String name, int x, int y)
  {
    this.name = name;
    this.x = x;
    this.y = y;
  }
  
  public abstract void tick();
  
  public abstract void render(Graphics g);
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public int getX()
  {
    return x;
  }
  
  public void setX(int x)
  {
    this.x = x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public void setY(int y)
  {
    this.y = y;
  }
  
  public void moveX(int move)
  {
    this.x += move;
  }
  
  public void moveY(int move)
  {
    this.y += move;
  }
}
