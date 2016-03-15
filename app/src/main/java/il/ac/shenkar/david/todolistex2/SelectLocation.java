package il.ac.shenkar.david.todolistex2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SelectLocation extends AppCompatActivity
{
    private static final int ACTIVITY_RESULT_QR_DRDROID = 0;

    private Button scan_btn;
    private Spinner location_spinner;
    private ImageView info_img;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = SelectLocation.this;

        final TextView msg = new TextView(context);
        final SpannableString s = new SpannableString("https://play.google.com/store/apps/details?id=com.google.zxing.client.android");
        Linkify.addLinks(s, Linkify.WEB_URLS);
        msg.setText(Html.fromHtml("<br>In order to use the QR scan, the BradcodeScanner app must be installed on your phone\n" +
                "If the app is not installed, click on the link below.<br><br><a href=\"" + s + "\">Download Barcode Scanner</a>"));
        msg.setMovementMethod(LinkMovementMethod.getInstance());

        info_img = (ImageView) findViewById(R.id.qr_info);
        info_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                new AlertDialog.Builder(context)
                        .setTitle("QR Location Scan")
                        .setView(msg)
                        /*.setMessage("In order to use the QR scan, the BradcodeScanner app must be installed on your phone\n" +
                                "If the app is not installed, click on the link below.\n\n" + Html.fromHtml("<a href='https://play.google.com/store/apps/details?id=com.google.zxing.client.android'>Download Barcode Scanner</a>"))*/
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
            }
        });

        location_spinner = (Spinner) findViewById(R.id.selectlocationSpinner);

        scan_btn = (Button) findViewById(R.id.scanqr);

        scan_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);
                }

            }
        });
    }

    public void doneLoc(View view) {
        int position = location_spinner.getSelectedItemPosition();
        Globals.temp=position;
        Log.w("doneLoc",""+Globals.temp);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                int x = Integer.parseInt(contents);
                new AlertDialog.Builder(context)
                        .setTitle("QR Location Scan")
                        .setMessage("The location scanned is: " + Location.fromInteger(x).toString())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                location_spinner.setSelection(x);
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }
}