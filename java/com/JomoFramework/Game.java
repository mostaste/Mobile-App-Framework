package com.JomoFramework;


public interface Game
{
    public Audio getAudio();

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getInitScreen();

    public int getHighScore();

    public void updateHighScore(int newHighScore);

    public void showInterstitial();

}
