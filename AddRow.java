package gespanet.com.subscriptionreminder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddRow extends AppCompatActivity {

    static InterstitialAd interstitial;
    TextView addRowLabel;
    Button addButton;
    EditText addname, addlink, addrecent, addnext;
    String dataType;
   // SQLiteDatabase freeTrialDatabase, subDatabase;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener recentDate;
    DatePickerDialog.OnDateSetListener nextDate;
    SimpleDateFormat sdf;
    Date rDate, nDate;
    long recentDateMili, nextDateMili;
    static int adCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_row);

        addRowLabel = (TextView) findViewById(R.id.AddRowLabel);
        addButton = (Button) findViewById(R.id.add_button);
        addname = (EditText) findViewById(R.id.add_name);
        addlink = (EditText) findViewById(R.id.add_weblink);
        addrecent = (EditText) findViewById(R.id.add_recentsub);
        addnext = (EditText) findViewById(R.id.add_nextsub);
        Intent mainActivity = getIntent();
        dataType = mainActivity.getStringExtra("type");
        if(adCounter == 0){
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId("ca-app-pub-7887368925165458/3349599912");
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("08A52EC68785DAE6FF6F9732688F4172")
                    .build();
            interstitial.loadAd(adRequest);

        }Log.i("TEEG", "CounteR: " + adCounter);





         myCalendar = Calendar.getInstance();
         recentDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateRecent();
            }

        };
        nextDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateNext();
            }

        };
        addrecent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddRow.this, recentDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addnext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddRow.this, nextDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        if(dataType.equals("freetrial")){
            addRowLabel.setText("Insert FreeTrial");

          // freeTrialDatabase = this.openOrCreateDatabase("FreeTrials", MODE_PRIVATE, null);
          //  freeTrialDatabase.execSQL("CREATE TABLE IF NOT EXISTS freetrials(name VARCHAR, web_link VARCHAR, recent_sub INT, re_sub INT)");

        }
        else{
            addRowLabel.setText("Insert Subscription");

          //  subDatabase = this.openOrCreateDatabase("Subscriptions", MODE_PRIVATE, null);
          //  subDatabase.execSQL("CREATE TABLE IF NOT EXISTS subs(name VARCHAR, web_link VARCHAR, recent_sub INT, re_sub INT)");
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(dataType.equals("freetrial")){

                    if(addname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter name of FreeTrial", Toast.LENGTH_SHORT).show();
                    }
                    else if(addlink.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter website name of FreeTrial", Toast.LENGTH_SHORT).show();
                    }
                    else if(addrecent.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter The Date of when your FreeTrial Started", Toast.LENGTH_SHORT).show();
                    }
                    else if(addnext.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter the Date of when your FreeTrial Will End", Toast.LENGTH_SHORT).show();
                    }
                   else{

                        try {
                            rDate = sdf.parse(addrecent.getText().toString());
                           recentDateMili  = rDate.getTime();
                            nDate = sdf.parse(addnext.getText().toString());
                            nextDateMili  = nDate.getTime();
                            MainActivity.freeTrialDatabase.execSQL("INSERT INTO freetrials(name, web_link, recent_sub, re_sub) VALUES ('"+addname.getText().toString()+"', '"+addlink.getText().toString()+"', '"+recentDateMili+"', '"+nextDateMili+"')");
                            addlink.setText("");
                            addrecent.setText("");
                            addnext.setText("");
                            Toast.makeText(getApplicationContext(), addname.getText().toString() + " was Successfully added", Toast.LENGTH_SHORT).show();
                            addname.setText("");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Something Went Wrong. " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //freeTrialDatabase.close();
                        //subDatabase.close();
                        MainActivity.freeTrialDatabase.close();
                        MainActivity.subDatabase.close();
                        Intent i = new Intent(AddRow.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        if(adCounter == 0){
                            if(interstitial.isLoaded()){
                                interstitial.show();
                                adCounter++;
                            }
                        }
                        else if(adCounter >=2){
                            adCounter = 0;
                        }
                        else{
                            adCounter++;
                        }


                    }

                  //  freeTrialDatabase.execSQL("INSERT INTO freetrials(name, web_link_present, web_link, recent_sub, re_sub) VALUES ('Spotify', 'y', 'www.spotify.com','','')");

                }
                else{

                    if(addname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter name of Subscription", Toast.LENGTH_SHORT).show();
                    }
                    else if(addlink.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter website name of Subscription", Toast.LENGTH_SHORT).show();
                    }
                    else if(addrecent.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter The Date of when your Subscription Started", Toast.LENGTH_SHORT).show();
                    }
                    else if(addnext.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter the Date of when your Subscription Will Renew", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try {
                            rDate = sdf.parse(addrecent.getText().toString());
                            recentDateMili  = rDate.getTime();
                            nDate = sdf.parse(addnext.getText().toString());
                            nextDateMili  = nDate.getTime();
                            if(nextDateMili < recentDateMili){
                                Toast.makeText(getApplicationContext(), "The Billing Date must be after the Start Date", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    rDate = sdf.parse(addrecent.getText().toString());
                                    recentDateMili  = rDate.getTime();
                                    nDate = sdf.parse(addnext.getText().toString());
                                    nextDateMili  = nDate.getTime();
                                    MainActivity.subDatabase.execSQL("INSERT INTO subs(name, web_link, recent_sub, re_sub) VALUES ('"+addname.getText().toString()+"', '"+addlink.getText().toString()+"', '"+recentDateMili+"', '"+nextDateMili+"')");
                                    addlink.setText("");
                                    addrecent.setText("");
                                    addnext.setText("");
                                    Toast.makeText(getApplicationContext(), addname.getText().toString() + " was Successfully added", Toast.LENGTH_SHORT).show();
                                    addname.setText("");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Something Went Wrong. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                // freeTrialDatabase.close();
                                //subDatabase.close();
                                MainActivity.freeTrialDatabase.close();
                                MainActivity.subDatabase.close();
                                Intent i = new Intent(AddRow.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                if(adCounter == 0){
                                    if(interstitial.isLoaded()){
                                        interstitial.show();
                                        adCounter++;
                                    }
                                }
                                else if(adCounter >=2){
                                    adCounter = 0;
                                }
                                else{
                                    adCounter++;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                }


            }
        });



    }
    private void updateRecent() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
         sdf = new SimpleDateFormat(myFormat, Locale.US);

        addrecent.setText(sdf.format(myCalendar.getTime()));

    }
    private void updateNext() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
         sdf = new SimpleDateFormat(myFormat, Locale.US);

        addnext.setText(sdf.format(myCalendar.getTime()));
    }

}
