package il.ac.shenkar.david.todolistex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Welcome extends AppCompatActivity
{
    public final int REQUEST_CODE_USER_LOGIN = 1;
    public final int REQUEST_CODE_MANAGER_LOGIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onManagerSignUpbtn(View view)
    {
        Intent sendIntent = new Intent(this,Signup_Activity.class);
        setResult(RESULT_OK, sendIntent);
        startActivity(sendIntent);
    }

    public void onEmployeeLoginbtn(View view)
    {
        Intent sendIntent = new Intent(this,Login_activity.class);
        sendIntent.putExtra("ORIGIN",REQUEST_CODE_USER_LOGIN);
        setResult(RESULT_OK, sendIntent);
        startActivity(sendIntent);
    }

    public void onManagerLoginbtn(View view)
    {
        Intent sendIntent = new Intent(this,Login_activity.class);
        sendIntent.putExtra("ORIGIN", REQUEST_CODE_MANAGER_LOGIN);
        setResult(RESULT_OK, sendIntent);
        startActivity(sendIntent);
    }

    public void onExitbtn(View view)
    {
        finish();
        System.exit(0);
    }
}
