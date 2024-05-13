package com.example.flora;

 

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 
 
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

public class user_leaf_1 extends Activity {
	 ProgressDialog pd;
	  ProgressDialog pDialog;
		// TextView register;
	 // List<String> categories = new ArrayList<String>();
		//ListView listView;
		Dialog dialog;
		TextView hist1,scatt1,soil1,yeild1,crps1;
		 Button btn;
		 public static String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_leaf_1);
        
        
        btn = (Button) findViewById(R.id.button1);
        hist1 = (TextView) findViewById(R.id.hist);
        scatt1 = (TextView) findViewById(R.id.scatt);
        
        soil1 = (TextView) findViewById(R.id.soil);
        yeild1 = (TextView) findViewById(R.id.yeild);
        crps1 = (TextView) findViewById(R.id.crps);
		 hist1.setText("0");
	        scatt1.setText("0");
	     
	        soil1 .setText("0");
	        yeild1.setText("0");
	        crps1.setText("0");
		 
		// register.setText(""+user_home.balance);

		        pd = new ProgressDialog(this);
		        pd.setMessage("Loading please wait");
		        pd.setCancelable(false);
			    commonRequestThread(); 
			    btn.setOnClickListener(new OnClickListener() 
		        { 
					public void onClick(View arg0) 
					{ 
					    Intent in = new Intent(getApplicationContext(), user_leaf_comments.class);
				           startActivity(in);  
					}
		        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }  
    public void commonRequestThread() {
        status = "Please try again later";

        pd.show();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        pd.dismiss();
                        if(user_id.size()<1){
                            if(isError) {
                                toast("No result found");
                               // finish();
                            }else{
                                toast("No data found");
                            }
                           // finish();
                        }else{
    						adapter = new ArrayAdapter<String>(context,
    								android.R.layout.simple_list_item_1,
    								android.R.id.text1, values);
    					 	//listView.setAdapter(adapter);
    					 
    					 
                        }
                    }
                };
                try {
                    runOnUiThread(runnable);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        Thread checkUpdate = new Thread() {
            public void run() {
                try {
                    commonRequest();
                } catch (Exception e) {
                    System.out.println("Error in fetching json : "
                            + e.toString());
                }
                handler.sendEmptyMessage(0);
            }
        };
        checkUpdate.start();
    }


    Boolean isError = true;
    public void commonRequest()
    {
        isError = true;
        System.out.println("Common request sent : ");
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://"+MainActivity.sip+"/search_leaf_view.php");
        InputStream is = null;
        String result = "";
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            
            nameValuePairs.add(new BasicNameValuePair("soil",""+(user_leaf.soil)));
            nameValuePairs.add(new BasicNameValuePair("hist",""+((int)user_leaf.f_volume)));
            nameValuePairs.add(new BasicNameValuePair("scat",""+((int)user_leaf.f_surface)));
            nameValuePairs.add(new BasicNameValuePair("fv",""+((int)user_leaf.f_multiple)));
            nameValuePairs.add(new BasicNameValuePair("ph",""+user_leaf.ph));
        
            
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out.println("Error 1 : "+e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error 2 : "+e.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            System.out.println("Error 2 : "+e.toString());
        }
        System.out.println("result : "+result);
        res = result;
        JSONObject food_object;

//	        TextView txtFirstName = (TextView) rootView.findViewById(R.id.txtFirstName);
//	        txtFirstName.setText(""+res);

        try {
            //food_object = new JSONObject(result);
            if(result.contains("result")){
                isError = false;
            }
            food_object = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            JSONArray foo_array = food_object.getJSONArray("result");

			values = new String[foo_array.length()];
            for (int i = 0; i < foo_array.length(); i++) {
                JSONObject js = foo_array.getJSONObject(i);

              user_id.add(js.getString("id"));
              String d=js.getString("id");//+"\nNOE : "+js.getString("noe")+"\nAddress : "+js.getString("address")+",\n"+"City : "+js.getString("city");
              id=js.getString("id");
				values[i] = d;
			     // categories.add(d);
			  //   categories1.add(js.getString("tname"));

		    // System.out.println("value q : "+values[i]);
				String soil11=js.getString("leaf");
				String yeild11=js.getString("yeild");
				String crops11=js.getString("crops");
				//String cls11=js.getString("cls");
			//	String phs1=js.getString("phs");
		 
				
				 hist1.setText(""+user_leaf.f_volume);
			        scatt1.setText(""+user_leaf.f_surface);
			     //   phval1.setText(""+user_leaf.ph);
			       // ph_rest1.setText(""+phs1);
			      //  classf1 .setText(cls11);
			        soil1 .setText(soil11);
			        yeild1.setText(yeild11);
			        crps1.setText(crops11);
				 

            }
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("Error 3 : "+e.toString());
            e.printStackTrace();
        }
    }

    String res = "", status = "";

	ArrayAdapter<String> adapter;
	String values[];

    List<String> user_id = new ArrayList<String>();
    List<String> ownername = new ArrayList<String>();
    
    Context context = this;
    
    Toast toast;
    public void toast(String str) {
        try {
            toast.cancel();
        } catch (Exception e) {
            // TODO: handle exception
        }
        toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
       
	 
     
}
