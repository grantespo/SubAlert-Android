package gespanet.com.subscriptionreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by grantespanet on 2/16/18.
 */

public class CustomListViewAdapter extends BaseAdapter {

    private HashMap<String, String> mHash = new HashMap<>();
    private Context mContext;
    static LayoutInflater notifyInflater = null;
    private Activity mActivity;
   // private SQLiteDatabase sqLiteDatabase;
    private String type;
    CustomListViewAdapter(Activity activity, Context context, ArrayList<HashMap<String, String>> data, String type) {
        super();
        this.mContext = context;
        this.mActivity = activity;
        //this.sqLiteDatabase = database;
        this.type = type;
        if(this.type.equals("free")){
            MainActivity.arrayFeedList = data;
        }
        else{
            MainActivity.arrayFeedListSub = data;
        }


        notifyInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        int size;
        if(this.type.equals("free")){
            size = MainActivity.arrayFeedList.size();
        }
        else{
            size = MainActivity.arrayFeedListSub.size();
        }
        return size;
    }

    @Override
    public Object getItem(int i) {
        Object object;
        if(this.type.equals("free")){
            object = MainActivity.arrayFeedList.get(i);
            //Log.i("WTFF", "getItemId (Free): " + MainActivity.arrayFeedList.get(i));
        }
        else{
            object = MainActivity.arrayFeedListSub.get(i);
        }
        return object;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        final ViewHolder holder;



        if(view == null){

            view = notifyInflater.inflate(R.layout.list_row, viewGroup, false);
            holder = new ViewHolder();

            holder.name = (TextView) view.findViewById(R.id.subNameRowId);
            holder.timeLeft = (TextView) view.findViewById(R.id.subTimeLeftRowId);
            holder.remove = (ImageView) view.findViewById(R.id.sub_removeId);

            if(this.type.equals("free")){
                holder.names = new String[MainActivity.arrayFeedList.size()];
                holder.websites = new String[MainActivity.arrayFeedList.size()];
                // Log.i("WTFF", "Size (Free): " + MainActivity.arrayFeedList.size());
            }
            else{
                holder.names = new String[MainActivity.arrayFeedListSub.size()];
                holder.websites = new String[MainActivity.arrayFeedListSub.size()];
                //  Log.i("WTFF", "Size (Subs): " + MainActivity.arrayFeedList.size());
            }

            view.setTag(holder);
            holder.remove.setTag(i);


            if(this.type.equals("free")){
                mHash = MainActivity.arrayFeedList.get(i);
               // Log.i("WTFF", "mHash (Free): " + MainActivity.arrayFeedList.get(i));

            }
            else{
                mHash = MainActivity.arrayFeedListSub.get(i);

            }

        }
        else{
            holder = (ViewHolder) view.getTag();
            holder.remove.setTag(i);
            if(this.type.equals("free")){
                mHash = MainActivity.arrayFeedList.get(i);

            }
            else{
                mHash = MainActivity.arrayFeedListSub.get(i);
            }
        }

        holder.names[i] = mHash.get("name");
        Log.i("WTFF", "names[i] (Free): " + holder.names[i]);
        holder.websites[i] = mHash.get("link");

        holder.name.setText(mHash.get("name"));
        holder.timeLeft.setText(mHash.get("timeLeft"));

        if(this.type.equals("free")){
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int pos = (Integer) v.getTag();
                    Log.i("WTFF", "(free) pos (before Dialoge): " + pos);
                    Log.i("WTFF", "(free) names[pos] (before Dialoge): " + holder.names[pos]);



                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    try{


                                        // sqLiteDatabase.delete("freetrials","id=? and name=?",);

                                        Log.i("WTFF", "(free) pos (in Dialoge): " + pos);
                                        MainActivity.freeTrialDatabase.delete("freetrials", "name" + "='" + holder.names[pos]+"'", null);
                                        Toast.makeText(mContext, holder.names[pos] +" was removed.", Toast.LENGTH_SHORT).show();
                                        MainActivity.freeTrialDatabase.close();
                                        MainActivity.freeTrialDatabase.close();
                                        MainActivity.subDatabase.close();
                                        Intent i = new Intent(mContext, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);

                                    }
                                    catch(Exception e){

                                        Toast.makeText(mContext, "Something went wrong. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.i("ROFLCOPTER", "sup " + e.getMessage());

                                    }


                                    //Yes button clicked
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    Log.i("WTFF", "(free) pos (in Alert): " + pos);
                    Log.i("WTFF", "(free) names[pos] (in Alert): " + holder.names[pos]);
                    builder.setMessage("Would you like to remove " + holder.names[pos] + "? (Make sure to cancel your freetrial at " + holder.websites[pos] +".)").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();



                }
            });
        }
        else{
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int pos = (Integer) v.getTag();



                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    try{


                                        // sqLiteDatabase.delete("freetrials","id=? and name=?",);

                                        MainActivity.subDatabase.delete("subs", "name" + "='" + holder.names[pos]+"'", null);
                                        Toast.makeText(mContext, holder.names[pos] +" was removed.", Toast.LENGTH_SHORT).show();
                                        MainActivity.subDatabase.close();
                                        MainActivity.freeTrialDatabase.close();
                                        MainActivity.subDatabase.close();
                                        Intent i = new Intent(mContext, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);

                                    }
                                    catch(Exception e){

                                        Toast.makeText(mContext, "Something went wrong. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.i("ROFLCOPTER", "sup " + e.getMessage());

                                    }


                                    //Yes button clicked
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Would you like to remove " + holder.names[pos] + "? (Make sure to cancel your subscription at " + holder.websites[pos] +".)").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();



                }
            });
        }



//TODO: Focus on REMOVE BUTTON For FREETRIAL,
        return view;
    }

    private class ViewHolder {

        //String[] likesString = new String[GlobalFeedTab.arrayFeedList.size()];
        private String[] names, websites;

        ImageView remove;
        TextView name;
        TextView timeLeft;


    }
}
