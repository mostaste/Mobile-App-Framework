package Jomo;

import com.JomoFramework.Game;
import com.JomoFramework.Screen;
import com.JomoFramework.Graphics;
import com.JomoFramework.Graphics.ImageFormat;


public class SplashLoadingScreen extends Screen
{
    Graphics g = game.getGraphics();

    public SplashLoadingScreen(Game game)
    {
        super(game);
    }

    @Override
    public void update(float deltaTime)
    {
        splashLoadingScreen();
        game.setScreen(new LoadingScreen(game));
    }

    @Override
    public void paint(float deltaTime)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void backButton()
    {

    }

    private void splashLoadingScreen()
    {
        // Load Loading Screen Logo here
        Assets.splash = g.newImage("Splash.png", ImageFormat.ARGB8888);
    }
}
