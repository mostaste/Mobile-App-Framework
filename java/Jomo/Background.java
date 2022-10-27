package Jomo;

import com.JomoFramework.Image;

public class Background
{
    private int x;
    private int y;
    private int speed;
    private Image bgImage;

    public Background(int x, int y,  int speed)
    {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update()
    {
        x -= speed;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public Image getBgImage() { return bgImage; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public void setBgImage(Image bgImage) { this.bgImage = bgImage; }

}
