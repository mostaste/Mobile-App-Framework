package Jomo;

import java.util.ArrayList;

import com.JomoFramework.Image;

public class Animation
{
    private ArrayList<AnimFrame> frames;

    private int currentFrame;
    private long animTime;
    private long totalDuration;

    private boolean bAnimated;

    public Animation()
    {
        frames = new ArrayList<>();
        totalDuration = 0;
        bAnimated = false;

        synchronized (this)
        {
            animTime = 0;
            currentFrame = 0;
        }
    }

    public synchronized void addFrame(Image image, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    public synchronized void update(long elapsedTime)
    {
        if (frames.size() > 1)
        {
            animTime += elapsedTime;
            if (animTime >= totalDuration)
            {
                animTime = animTime % totalDuration;
                currentFrame = 0;
                bAnimated = true;
            }

            while (animTime > getFrame(currentFrame).endTime)
            {
                currentFrame++;
            }
        }
    }

    public synchronized Image getImage()
    {
            if (frames.size() == 0)
                return null;
            else
                return getFrame(currentFrame).image;
    }

    public int getCurrentFrame()
    {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame)
    {
        this.currentFrame = currentFrame;
    }

    public boolean getbAnimated()
    {
        return bAnimated;
    }

    public void setbAnimated(boolean bAnimated) {
        this.bAnimated = bAnimated;
    }

    private AnimFrame getFrame(int i) { return (AnimFrame) frames.get(i);}

    private class AnimFrame
    {
        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime)
        {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
