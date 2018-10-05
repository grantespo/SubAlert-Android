package gespanet.com.subscriptionreminder;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    static SQLiteDatabase freeTrialDatabase, subDatabase;
    ImageView addFreeTrial, addSubscription;
    ListView freetrials, subs;
    TextView privacy;




    static ArrayList<HashMap<String, String>> arrayFeedList;
    static HashMap<String, String> feedData;

    static ArrayList<HashMap<String, String>> arrayFeedListSub;
    static HashMap<String, String> feedDataSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        privacy = (TextView) findViewById(R.id.privacyPolicyId);
        addFreeTrial = (ImageView) findViewById(R.id.freetiral_add_buttonId);
        addSubscription = (ImageView) findViewById(R.id.sub_addButtonId);
        freetrials = (ListView) findViewById(R.id.freetrialListView);
        subs = (ListView) findViewById(R.id.sublistview);
        subDatabase = this.openOrCreateDatabase("Subscriptions", MODE_PRIVATE, null);
        freeTrialDatabase = this.openOrCreateDatabase("FreeTrials", MODE_PRIVATE, null);
        freeTrialDatabase.execSQL("CREATE TABLE IF NOT EXISTS freetrials(name VARCHAR, web_link VARCHAR, recent_sub INT, re_sub INT)");
        subDatabase.execSQL("CREATE TABLE IF NOT EXISTS subs(name VARCHAR, web_link VARCHAR, recent_sub INT, re_sub INT)");
        arrayFeedList = new ArrayList<>();
        arrayFeedListSub = new ArrayList<>();
        Date currentDate = new Date();

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(i);
            }
        });


        Cursor trialC = freeTrialDatabase.rawQuery("SELECT * FROM freetrials", null);
        int trialNameIndex = trialC.getColumnIndex("name");
        int trialLinkIndex = trialC.getColumnIndex("web_link");
        int trialRecentIndex = trialC.getColumnIndex("recent_sub");
        int trialNextIndex = trialC.getColumnIndex("re_sub");

        Cursor subC = subDatabase.rawQuery("SELECT * FROM subs", null);
        int subNameIndex = subC.getColumnIndex("name");
        int subLinkIndex = subC.getColumnIndex("web_link");
        int subRecentIndex = subC.getColumnIndex("recent_sub");
        int subNextIndex = subC.getColumnIndex("re_sub");


        //freetrialRows = DatabaseUtils.queryNumEntries(freeTrialDatabase, "freetrials");
        //subRows = DatabaseUtils.queryNumEntries(subDatabase, "subs");
        Log.i("APPLOL", "Curser length: " + trialC.getCount());

        trialC.moveToFirst();
        if(trialC.getCount() <=0){

        }
        else{


           // while(trialC.getPosition() < trialC.getCount()){
            while(!trialC.isAfterLast()){

                feedData = new HashMap<>();

                feedData.put("name", trialC.getString(trialNameIndex)); Log.i("APPLOL", "Name: " + trialC.getString(trialNameIndex));
                feedData.put("link", trialC.getString(trialLinkIndex));
                long recentLong = TimeUnit.MILLISECONDS.toDays(trialC.getLong(trialRecentIndex));
                long nextLong  = TimeUnit.MILLISECONDS.toDays(trialC.getLong(trialNextIndex)); //crash here?

                long currentLong = TimeUnit.MILLISECONDS.toDays(currentDate.getTime());
                long lengthOfFreeTrial = nextLong - recentLong;

                if((nextLong - currentLong) <= 0){
                    feedData.put("timeLeft", "Expired");
                }
                else if ((nextLong - currentLong) > 0 && (nextLong - currentLong) <= 1){
                    feedData.put("timeLeft", "EXPIRES TODAY");
                }
                else{
                    feedData.put("timeLeft", "Expires in " + String.valueOf(nextLong - currentLong) + " days");
                }
                arrayFeedList.add(feedData);

                trialC.moveToNext();  Log.i("APPLOL", "Curser Position (MoveToNext): " + trialC.getPosition());

            }
            CustomListViewAdapter trialCustomListAdapter = new CustomListViewAdapter(MainActivity.this,MainActivity.this, arrayFeedList, "free");
            freetrials.setAdapter(trialCustomListAdapter);
        }


freetrials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        try{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            String websiteString = "http://" + Uri.parse(arrayFeedList.get(i).get("link"));
            intent.setData(Uri.parse(websiteString));
            startActivity(intent);
        }
        catch (Exception e){
            Log.i("ROFLCOPTER", "sup " + e.getMessage());

        }



    }
});



        subC.moveToFirst();
        if(subC.getCount() <=0){

        }
        else{


           // while(subC.getPosition() < subC.getCount()){
            while(!subC.isAfterLast()){

                feedDataSub = new HashMap<>();

                feedDataSub.put("name", subC.getString(subNameIndex)); Log.i("APPLOL", "Name: " + subC.getString(subNameIndex));
                feedDataSub.put("link", subC.getString(subLinkIndex));
                long recentLong = TimeUnit.MILLISECONDS.toDays(subC.getLong(subRecentIndex));
                long nextLong  = TimeUnit.MILLISECONDS.toDays(subC.getLong(subNextIndex)); //crash here?

                long currentLong = TimeUnit.MILLISECONDS.toDays(currentDate.getTime());
                long lengthOfSub = nextLong - recentLong;


                if((nextLong - currentLong) < 0){

                    while((nextLong - currentLong) < 0) {
                        nextLong += lengthOfSub;
                        Log.i("LOLZA", "NextLong - CurrentLong: " + (nextLong - currentLong));
                    }

                }
                 if ((nextLong - currentLong) >= 0 && (nextLong - currentLong) <= 1){
                    feedDataSub.put("timeLeft", "RENEWS TODAY");
                }
                else{
                    feedDataSub.put("timeLeft", "Renews in " + String.valueOf(nextLong - currentLong) + " days");
                }
                arrayFeedListSub.add(feedDataSub);

                subC.moveToNext();  Log.i("APPLOL", "Curser Position (MoveToNext): " + subC.getPosition());

            }
            CustomListViewAdapter trialCustomListAdapter = new CustomListViewAdapter(MainActivity.this,MainActivity.this, arrayFeedListSub, "sub");
            subs.setAdapter(trialCustomListAdapter);
        }


        subs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    String websiteString = "http://" + Uri.parse(arrayFeedListSub.get(i).get("link"));
                    intent.setData(Uri.parse(websiteString));
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.i("ROFLCOPTER", "sup " + e.getMessage());

                }



            }
        });



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        addFreeTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFreeTrialRow = new Intent(MainActivity.this, AddRow.class);
                addFreeTrialRow.putExtra("type", "freetrial");
                startActivity(addFreeTrialRow);
            }
        });
        addSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addSub = new Intent(MainActivity.this, AddRow.class);
                addSub.putExtra("type", "subscription");
                startActivity(addSub);

            }
        });


}
}
