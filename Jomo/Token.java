package Jomo;

import android.graphics.Rect;

public class Token
{
    private int x, y;
    private int speed;
    private boolean isAlive;

    protected Rect rStar;
    protected int tokenRadius = 32;


    public Token(int x, int y)
    {
        this.x = x;
        this.y = y;
        speed = 5;
        isAlive = true;
        rStar = new Rect(x, y, x + tokenRadius, y + tokenRadius);
    }

    public void update()
    {
        x -= speed;
        rStar.set(x, y, x + tokenRadius, y + tokenRadius);
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) {this.y = y; }

    public int getSpeed() { return speed; }

    public void setSpeed(int speed) { this.speed = speed; }

    public boolean isAlive() { return isAlive; }

    public void setAlive(boolean alive) { isAlive = alive; }
}
