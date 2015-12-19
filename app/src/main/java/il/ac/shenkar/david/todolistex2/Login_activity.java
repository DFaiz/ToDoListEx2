package il.ac.shenkar.david.todolistex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Login_activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onLoginbtn (View view)
    {
        Intent returnIntent = new Intent(this,MainActivity.class);
        setResult(RESULT_OK, returnIntent);
        startActivity(returnIntent);
    }
}
