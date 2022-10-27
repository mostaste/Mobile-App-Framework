package Jomo;

import android.graphics.Rect;

public class Player
{
    private int x, y;
    private int acceleration;
    private boolean isMoving;

    protected Rect rPlayer;
    protected int playerXRadius = 80;
    protected int playerYRadius = 100;


    public Player(int x, int y)
    {
        this.x          = x;
        this.y          = y;
        acceleration    = 5;
        isMoving        = false;
        rPlayer = new Rect(x, y , x + playerXRadius, y + playerYRadius);
    }

    public void update()
    {
        if (isMoving)
        {
            acceleration = 5;
            y -= 10;
        }
        else
        {
            y += acceleration;
            acceleration++;

            if (acceleration >= 12)
                acceleration = 12;
        }

        rPlayer.set(x, y , x + playerXRadius, y + playerYRadius);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public boolean isMoving(){return isMoving;}

    public void setMoving(boolean moving){isMoving = moving;}

    public void setAcceleration(int acceleration){this.acceleration = acceleration;}


}
