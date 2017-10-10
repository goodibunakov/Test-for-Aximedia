package ru.goodibunakov.testaximedia;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by GooDi on 07.10.2017.
 */

public class Splash extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {

        //Customize Circular Reveal
        configSplash.setBackgroundColor(android.R.color.background_light); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(100); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.aximedialogo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(800); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

        //Customize Title
        configSplash.setTitleSplash(getResources().getString(R.string.splash_text));
        configSplash.setTitleTextColor(android.R.color.tertiary_text_light);
        configSplash.setTitleTextSize(15f); //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.BounceInUp);
    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
    }
}