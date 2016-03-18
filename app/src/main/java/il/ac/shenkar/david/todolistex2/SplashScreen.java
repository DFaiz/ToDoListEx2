package il.ac.shenkar.david.todolistex2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.content.Intent;
import android.os.Handler;

import com.parse.Parse;

public class SplashScreen extends AppCompatActivity
{
    ImageView imageView;
    boolean lgState;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);

        Globals.team_name = sharedpreferences.getString("TeamName",null);
        Globals.refresh_minutes = sharedpreferences.getInt("RefreshInterval",5);

       // sharedpreferences.edit().clear().commit();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "aaQYWKgO1skn55Flg0vgT3SwYjpVXGxxcXd241Tw", "fAkWiu6GXGQkxEve7MaixZZj5P0bGjAywCXFPj46");

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
                lgState = sharedpreferences.getBoolean("LoginState",false);

                if(lgState)
                {
                    Intent mainIntent = new Intent(SplashScreen.this, Login_activity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
                else
                {
                    Intent mainIntent = new Intent(SplashScreen.this, Signup_Activity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
