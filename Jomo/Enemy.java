package Jomo;

import android.graphics.Rect;

import com.JomoFramework.Image;

public class Enemy
{
    private int x, y;
    private int xDiameter, yDiameter;
    private int acceleration;
    private Image enemyImage;

    protected Rect rEnemy;


    //TODO Set the Diameter on Create as there are 2 Different Enemies now
    public Enemy(int x, int y, int xDiameter, int yDiameter)
    {
        this.x          = x;
        this.y          = y;
        this.xDiameter  = xDiameter;
        this.yDiameter  = yDiameter;
        rEnemy = new Rect(x, y , x + xDiameter, y + yDiameter);
    }

    public void update(int speed)
    {
        x -= speed;

        rEnemy.set(x, y, x + xDiameter, y + yDiameter);
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public Image getEnemyImage() { return enemyImage; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public void setEnemyImage(Image enemyImage) { this.enemyImage = enemyImage; }

    public void setRect(int xDiameter, int yDiameter)
    {
        this.xDiameter = xDiameter; this.yDiameter = yDiameter;
    }
}
