package Jomo;

import com.JomoFramework.Screen;

import Implementation.AndroidGame;

/**     This is where the App Begins       **/

public class GameApp extends AndroidGame
{
    @Override
    public Screen getInitScreen()
    {
        return new SplashLoadingScreen(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onBackPressed()
    {
        getCurrentScreen().backButton();
    }
}
