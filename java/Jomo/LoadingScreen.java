package Jomo;

import com.JomoFramework.Game;
import com.JomoFramework.Screen;
import com.JomoFramework.Graphics;
import com.JomoFramework.Graphics.ImageFormat;


public class LoadingScreen extends Screen
{
    Graphics g = game.getGraphics();
    private int timer;

    public LoadingScreen(Game game)
    {
        super(game);
        timer = 0;
    }

    @Override
    public void update(float deltaTime)
    {
        /**      Load Assets (Images and Audio)       **/

        loadPlayer();
        loadToken();
        loadEnemy();
        loadBackground();
        loadMusic();
        loadScore();
        loadOptions();
        loadingScreen();
    }


    @Override
    public void paint(float deltaTime)
    {
        g.drawImage(Assets.splash, 0, 0);
    }


    @Override
    public void pause(){ }


    @Override
    public void resume(){ }


    @Override
    public void dispose(){ }


    @Override
    public void backButton(){}


    private void loadPlayer()
    {
        Assets.xPlayer          = 300;
        Assets.yPlayer          = 500;
        Assets.playerballoon0   = g.newImage("player0.png", ImageFormat.ARGB4444);
        Assets.playerGameOver   = g.newImage("playerGameOver.png", ImageFormat.ARGB4444);
    }

    private void loadEnemy()
    {
        Assets.enemybird0       = g.newImage("enemy-0.png", ImageFormat.ARGB4444);
        Assets.enemybird1       = g.newImage("enemy-1.png", ImageFormat.ARGB4444);
        Assets.enemybird2       = g.newImage("enemy-2.png", ImageFormat.ARGB4444);
        Assets.enemybird3       = g.newImage("enemy-3.png", ImageFormat.ARGB4444);

        Assets.enemybuilding0   = g.newImage("enemybuilding0.png", ImageFormat.ARGB4444);
        Assets.enemybuilding1   = g.newImage("enemybuilding1.png", ImageFormat.ARGB4444);
        Assets.enemybuilding2   = g.newImage("enemybuilding2.png", ImageFormat.ARGB4444);
    }

    private void loadToken()
    {
        Assets.token0 = g.newImage("token0.png", ImageFormat.RGB565);
        Assets.coinSound = game.getAudio().createSound("smb_coin.wav");
    }

    private void loadBackground()
    {
        Assets.skybackground  = g.newImage("skybg.png", ImageFormat.RGB565);
        Assets.sunbackground  = g.newImage("sun.png", ImageFormat.RGB565);
        Assets.bgcloud1       = g.newImage("bg1.png", ImageFormat.RGB565);
        Assets.bgcloud2       = g.newImage("bg2.png", ImageFormat.RGB565);
        Assets.bgcloud3       = g.newImage("bg3.png", ImageFormat.RGB565);
        Assets.bgcloud4       = g.newImage("bg4.png", ImageFormat.RGB565);
        Assets.bgcloud5       = g.newImage("bg5.png", ImageFormat.RGB565);
    }

    private void loadMusic()
    {
        Assets.theme = game.getAudio().createMusic("sandwich_song.ogg");
        Assets.theme.setLooping(true);
        Assets.theme.setVolume(0.45f);

        Assets.explosion0 = g.newImage("explosion0.png", ImageFormat.ARGB4444);
        Assets.explosionSound = game.getAudio().createSound("explosionsound.wav");
    }

    private void loadOptions()
    {
        Assets.settingsButton   = g.newImage("settings.png",ImageFormat.RGB565);
        Assets.exitButton       = g.newImage("xbutton.png", ImageFormat.RGB565);
        Assets.soundButton      = g.newImage("soundon.png", ImageFormat.RGB565);
        Assets.musicButton      = g.newImage("musicon.png", ImageFormat.RGB565);
        Assets.noSoundButton    = g.newImage("soundoff.png", ImageFormat.RGB565);
        Assets.noMusicButton    = g.newImage("musicoff.png", ImageFormat.RGB565);
    }

    private void loadScore()
    {
        Assets.xScore   = 600;
        Assets.yScore   = 180;
        Assets.trophy   = g.newImage("trophy.png", ImageFormat.RGB565);
    }

    private void loadAd()
    {
    }

    private void loadingScreen()
    {
        if(timer < 2)
            timer++;
        else
            game.setScreen(new GameScreen(game));
    }
}
