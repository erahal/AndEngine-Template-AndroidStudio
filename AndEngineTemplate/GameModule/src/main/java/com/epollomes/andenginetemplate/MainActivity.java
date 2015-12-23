package com.epollomes.andenginetemplate;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class MainActivity extends SimpleBaseGameActivity {


    boolean ADS_DISABLED = false;
    float screenWidth = 0.0f;
    float screenHeight = 0.0f;
    private Scene splashScene;
    private SoundManager soundManager;
    private MusicManager musicManager;
    private FontManager fontManager;
    AssetManager assetManager;
    TextureManager textureManager;
    VertexBufferObjectManager vbm;
    private boolean isWindowFocused;
    private String versionNumber;
    private String appName;


    float CAMERA_WIDTH = 800;
    float CAMERA_HEIGHT = 800;
    float CENTERX = 0;
    float CENTERY = 0;
    SmoothCamera mCamera;

    private BitmapTextureAtlas spikeTextureAtlas;
    private ITextureRegion spikeTextureRegion;


    @Override
    @SuppressLint("NewApi")
    protected void onSetContentView() {

        if (!ADS_DISABLED) {

            final FrameLayout frameLayout = new FrameLayout(this);
            final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT, Gravity.FILL);
            AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("D59B314472E86D1EE5D10F3A2D3BEAE3")
                    .addTestDevice("CE18831FE56C4BF93E6346484BEFE919").addTestDevice("B802310805444FAA3411ED09E826E75E")
                    .addTestDevice("55886889B56D1ECFD39330375C77ECBE").addTestDevice("3387D993371A6A0DD34EB34EEBCF1EF0")
                    .addTestDevice("1BBEB1F9B4768A1D113935716875DA51").addTestDevice("1B8CB34AFDACF39875751C9552DCAB74")
                    .addTestDevice("BC3C59A2A87ADE2DB1B577A79D4F899A").addTestDevice("BEF990B50489D3144593D2671A86CEFF")
                    .addTestDevice("37BEF980434D16014A2962020239CF0F").build();


            this.mRenderSurfaceView = new RenderSurfaceView(this);
            mRenderSurfaceView.setRenderer(mEngine, this);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                mRenderSurfaceView.setPreserveEGLContextOnPause(true);
            }

            final FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceViewLayoutParams.gravity = Gravity.CENTER;
            frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
            this.setContentView(frameLayout, frameLayoutLayoutParams);

        } else {

            super.onSetContentView();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                mRenderSurfaceView.setPreserveEGLContextOnPause(true);
            }
        }
    }

    @Override
    public EngineOptions onCreateEngineOptions() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        if (screenWidth < screenHeight) {
            float temp = screenWidth;
            screenWidth = screenHeight;
            screenHeight = temp;
        }

        float ratio = screenWidth / screenHeight;

        CAMERA_WIDTH = 800;
        CAMERA_HEIGHT = (float) Math.floor(800 / ratio);
        CENTERX = CAMERA_WIDTH / 2;
        CENTERY = CAMERA_HEIGHT / 2;

        this.mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 1100, 750, 0.1f);

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getRenderOptions().setDithering(true);
        engineOptions.getAudioOptions().getSoundOptions().setMaxSimultaneousStreams(10);
        return engineOptions;
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
    }

    @Override
    protected void onCreateResources() {

        FontFactory.setAssetBasePath("font/");
        SoundFactory.setAssetBasePath("mfx/");
        MusicFactory.setAssetBasePath("mfx/");

//        int language = Integer.parseInt(getResources().getString(R.string.lang));
        assetManager = getAssets();
        textureManager = this.getTextureManager();
        vbm = getVertexBufferObjectManager();
        soundManager = this.mEngine.getSoundManager();
        musicManager = this.mEngine.getMusicManager();
        fontManager = this.getFontManager();

        createMenuFont();

        try {
            versionNumber = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            appName = getResources().getString(R.string.app_name);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (screenWidth < 600) {
            spikeTextureAtlas = new BitmapTextureAtlas(textureManager, 199, 199, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            spikeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spikeTextureAtlas, this,
                    "gfx/small/epollomesRound" +
                            ".png", 0, 0);
            spikeTextureAtlas.load();
        } else if (screenWidth < 1100) {
            spikeTextureAtlas = new BitmapTextureAtlas(textureManager, 332, 332, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            spikeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spikeTextureAtlas, this, "gfx/medium/epollomesRound.png", 0, 0);
            spikeTextureAtlas.load();
        } else {
            spikeTextureAtlas = new BitmapTextureAtlas(textureManager, 498, 498, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            spikeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spikeTextureAtlas, this, "gfx/epollomesRound.png", 0, 0);
            spikeTextureAtlas.load();
        }
    }


    @Override
    protected Scene onCreateScene() {

        this.splashScene = new Scene();
        this.mEngine.registerUpdateHandler(new FPSLogger());

        Sprite testSprite = new Sprite(0, 0, spikeTextureRegion, vbm);
        testSprite.setScaleCenter(0, 0);
        testSprite.setRotationCenter(0, 0);
        testSprite.setScale(StaticUtilities.getScaledBasedOnHDSize(screenWidth, 0.67f));
        testSprite.setPosition(CENTERX - testSprite.getWidthScaled() / 2, CENTERY - testSprite.getHeightScaled() / 2);
        splashScene.attachChild(testSprite);

        return splashScene;
    }


    @Override
    public synchronized void onGameCreated() {
        super.onGameCreated();
    }

    /**
     *
     * Make sure to compile the DebugDrawExtension in the projects gradle file
     *
     */
    private void debugStuff() {
        //        DebugRenderer debug = new DebugRenderer(mPhysicsWorld,
        //                getVertexBufferObjectManager());
        //        splashScene.attachChild(debug);
        //        debug.setZIndex(20);
        //        splashScene.sortChildren();
    }

    private void createMenuFont() {
        //        int textColor;
        //        textColor = android.graphics.Color.argb(255, 255, 255, 255);
        //        if (menuFontTextureAtlas == null) {
        //            if (screenWidth < 600) {
        //                this.menuFontTextureAtlas = new BitmapTextureAtlas(textureManager, 384, 384, TextureOptions.BILINEAR);
        //            } else if (screenWidth < 1100) {
        //                this.menuFontTextureAtlas = new BitmapTextureAtlas(textureManager, 512, 512, TextureOptions.BILINEAR);
        //            } else {
        //                this.menuFontTextureAtlas = new BitmapTextureAtlas(textureManager, 768, 768, TextureOptions.BILINEAR);
        //            }
        //        }
        //        if (fontTypeFace == null) {
        //            fontTypeFace = Typeface.createFromAsset(assetManager, "font/capture.ttf");
        //        }
        //
        //        if (screenWidth < 600) {
        //            this.menuFont = new Font(fontManager, this.menuFontTextureAtlas, fontTypeFace, 39.0f, true, textColor);
        //        } else if (screenWidth < 1100) {
        //            this.menuFont = new Font(fontManager, this.menuFontTextureAtlas, fontTypeFace, 64.5f, true, textColor);
        //        } else {
        //            this.menuFont = new Font(fontManager, this.menuFontTextureAtlas, fontTypeFace, 97.0f, true, textColor);
        //        }
        //        this.menuFont.load();
        //        menuFont.prepareLetters((df.format(1234567890) + "1234567890abcdefghiklmnoprstuABCDEFGHIKLMNOPRSTU").toCharArray());
    }


    @SuppressLint("NewApi")
    private void dimSoftButtonsIfPossible() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (soundManager != null)
                soundManager.setMasterVolume(0.0f);
            if (musicManager != null)
                musicManager.setMasterVolume(0.0f);
        } catch (Exception e) {
            e.printStackTrace();
            toastOnUIThread("Error while saving last car used please report to epollomes@gmail.com", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public synchronized void onPauseGame() {
        super.onPauseGame();
    }

    @Override
    public synchronized void onWindowFocusChanged(boolean pHasWindowFocus) {
        super.onWindowFocusChanged(pHasWindowFocus);
        isWindowFocused = pHasWindowFocus;
        if (isWindowFocused) {

            if (!ADS_DISABLED) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        DisplayToast(".", Toast.LENGTH_LONG);
                    }
                });
            }
            dimSoftButtonsIfPossible();
        }
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        MainActivity.this.onResumeGame();
        try {
            if (soundManager != null)
                soundManager.setMasterVolume(1.0f);
            if (musicManager != null)
                musicManager.setMasterVolume(1.0f);
        } catch (Exception e) {
            e.printStackTrace();
            toastOnUIThread("Error while saving last car used please report to epollomes@gmail.com", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public synchronized void onResumeGame() {
        super.onResumeGame();
    }

    private void DisplayToast(String textMessage, int messageLength) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customized_toast2, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(textMessage);
        Toast toastAmount;
        toastAmount = new Toast(getApplicationContext());
        toastAmount.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        toastAmount.setDuration(messageLength);
        toastAmount.setView(layout);
        toastAmount.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void loadAllBitmaps() {
        if (screenWidth < 600) {
        } else if (screenWidth < 1100) {
        } else {
        }
    }
}
