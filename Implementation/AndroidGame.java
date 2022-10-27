package Implementation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.JomoFramework.Audio;
import com.JomoFramework.FileIO;
import com.JomoFramework.Game;
import com.JomoFramework.Graphics;
import com.JomoFramework.Input;
import com.JomoFramework.Screen;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;




public abstract class AndroidGame extends Activity implements Game
{
    private InterstitialAd mInterstitialAd;
    private AdView mBannerAd;

    // Test Ads ID
    //private static final String AD_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    //private static final String AD_INTER_ID = "ca-app-pub-3940256099942544/1033173712";

    // Real Ads ID
    private static final String AD_BANNER_ID = "ca-app-pub-9529968537497743/5103755542";
    private static final String AD_INTER_ID = "ca-app-pub-9529968537497743/1038912677";

    RelativeLayout layout;
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
    Context context;
    Activity activity;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int highscore;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int frameBufferWidth = isPortrait ? 800 : 1280;
        int frameBufferHeight = isPortrait ? 1280 : 800;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);

        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame::MyWakeLogTag");

        prefs = this.getSharedPreferences("Jomo", Context.MODE_PRIVATE);
        highscore = prefs.getInt("score", 0);
        editor = prefs.edit();

        this.context = this;
        this.activity = this;

        initAds();
        loadInterstitialAd();
        loadBannerAd();

        setContentView(layout);
        //setContentView(renderView);
    }

    private void initAds()
    {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
    }


    public void loadBannerAd()
    {
        // Set the Banner Ad
        mBannerAd = new AdView(this);
        mBannerAd.setAdSize(AdSize.BANNER);
        mBannerAd.setAdUnitId(AD_BANNER_ID);

        // Request and Load Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);

        // Create a Layout so you can apply 2 Content (Game and Banner Ad)
        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        // Add 2 Views to the Layout
        layout.addView(renderView);
        layout.addView(mBannerAd, adParams);
    }

    private void loadInterstitialAd()
    {
        // Request and Load Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_INTER_ID,
                adRequest,
                new InterstitialAdLoadCallback()
                {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd)
                    {
                        // The mInterstitialAd reference will be null until an ad is loaded.
                        AndroidGame.this.mInterstitialAd = interstitialAd;
                        //Log.i(TAG, "onAdLoaded");
                        //Toast.makeText(AndroidGame.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback()
                                {
                                    @Override
                                    public void onAdDismissedFullScreenContent()
                                    {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't show it a second time.
                                        AndroidGame.this.mInterstitialAd = null;
                                        //Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError)
                                    {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't show it a second time.
                                        AndroidGame.this.mInterstitialAd = null;
                                        //Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent()
                                    {
                                        // Called when fullscreen content is shown.
                                        //Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
                    {
                        mInterstitialAd = null;
                        /* Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        String error;
                        error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Toast.makeText(AndroidGame.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                         */
                    }
                });
    }

    @Override
    public void showInterstitial()
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                // Show the ad if it's ready. Otherwise Toast and reload a new Ad
                if (mInterstitialAd != null)
                {
                    //Toast.makeText(context, "Showing Ad", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show(activity);
                }
                else
                {
                    //Toast.makeText(context, "Reloading Ad", Toast.LENGTH_SHORT).show();
                    loadInterstitialAd();
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() { return input; }

    @Override
    public FileIO getFileIO() { return fileIO; }

    @Override
    public Graphics getGraphics() { return graphics; }

    @Override
    public Audio getAudio() { return audio; }

    @Override
    public void setScreen(Screen screen)
    {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen()
    {
        return screen;
    }

    public int getHighScore()
    {
        highscore = prefs.getInt("score", 0);
        return highscore;
    }

    public void updateHighScore(int newHighScore)
    {
        editor.putInt("score", newHighScore);
        editor.commit();
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
        private void reloadInterstitialAd()
    {
        // Request a new ad if one isn't already loaded
        if (mInterstitialAd == null)
        {
            Toast.makeText(context, "Reloading Ad", Toast.LENGTH_SHORT).show();
            loadInterstitialAd();
            Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
            reloadInterstitialAd();
        }
    }

    private void ShowAd()
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(
                        context,
                        AD_INTER_ID,
                        adRequest,
                        new InterstitialAdLoadCallback()
                        {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd)
                            {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                AndroidGame.this.mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                                Toast.makeText(AndroidGame.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                                interstitialAd.setFullScreenContentCallback(
                                        new FullScreenContentCallback()
                                        {
                                            @Override
                                            public void onAdDismissedFullScreenContent()
                                            {
                                                // Called when fullscreen content is dismissed.
                                                // Make sure to set your reference to null so you don't show it a second time.
                                                AndroidGame.this.mInterstitialAd = null;
                                                Log.d("TAG", "The ad was dismissed.");
                                            }

                                            @Override
                                            public void onAdFailedToShowFullScreenContent(AdError adError)
                                            {
                                                // Called when fullscreen content failed to show.
                                                // Make sure to set your reference to null so you don't show it a second time.
                                                AndroidGame.this.mInterstitialAd = null;
                                                Log.d("TAG", "The ad failed to show.");
                                            }

                                            @Override
                                            public void onAdShowedFullScreenContent()
                                            {
                                                // Called when fullscreen content is shown.
                                                Log.d("TAG", "The ad was shown.");
                                            }
                                        });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
                            {
                                // Handle the error
                                Log.i(TAG, loadAdError.getMessage());
                                mInterstitialAd = null;

                                String error =
                                        String.format("domain: %s, code: %d, message: %s",
                                                loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                                Toast.makeText(AndroidGame.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void configureBanner()
    {
        mBannerAd = new AdView(this);
        mBannerAd.setAdSize(AdSize.BANNER);
        mBannerAd.setAdUnitId(AD_BANNER_ID);

        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);


        /*
        RelativeLayout mAdRelativeLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        mAdRelativeLayout.addView(mBannerAd);

        setContentView(mAdRelativeLayout, rlp);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.BOTTOM| Gravity.CENTER);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 9f);
        layoutParams1.gravity = Gravity.TOP;

        int heightPixels = AdSize.FULL_BANNER.getHeightInPixels(this);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPixels, 0f);
        layoutParams2.gravity = Gravity.BOTTOM;
        mBannerAd.setLayoutParams(layoutParams2);
        mBannerAd.setBackgroundColor(Color.TRANSPARENT);

        layout.addView(mBannerAd);

        // Start loading the ad.
        setContentView(layout);

         */

        /*
        // Add the AdView to the view hierarchy. The view will have no size until the ad is loaded.
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        //Request full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Create a display Metrics object to get pixel width and height
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //Create and set GL view (OpenGL View)
        myView = new MyGLSurfaceView(activity);
        layout.addView(myView);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(mBannerAd, adParams);

        //Set main renderer
        setContentView(layout);
    }
     */
