package com.example.flora;

 
 

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
 
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class user_leaf_comments extends Activity {
	  List<String> categories = new ArrayList<String>();
	 
	EditText txt;
	Button btn;
	 ProgressDialog pDialog;
	    String id="";
	    ProgressDialog pd;
	    ListView listview;
	    TextView textview;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_leaf_comments);
		pd = new ProgressDialog(this);
        pd.setMessage("Loading please wait");
        pd.setCancelable(false);
      
	    commonRequestThread();
		
	 
	    textview = (TextView) findViewById(R.id.textView2);
		
		btn = (Button) findViewById(R.id.Button01);
		listview = (ListView) findViewById(R.id.listView1);

	 
		txt = (EditText) findViewById(R.id.editText1);
		//rating_bar.setRating(5);

		 listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	                      long arg3) {  
	                 //Log.i("Selected Item in list", arg1.toString());  
	                 String test = (String) listview.getAdapter().getItem(arg2);  
	                
	                // Toast.makeText(admin_view_disaster.this,test ,Toast.LENGTH_LONG);
	                 //String[] arrSplit = test.split(":");
	               //  Log.i("id", arrSplit[0]);  
	                Toast.makeText(user_leaf_comments.this,test.trim() ,Toast.LENGTH_LONG);
	                 //e1.setText(arrSplit[0]);
	                id=test.trim();
	                 Log.i("Selected Item in list", id);  
	             //    textview.setText(""+id);
	 
	               
	            }  
	       });	
	  
		
		btn.setOnClickListener(new OnClickListener() 
        {

			 
			public void onClick(View arg0)
			{
				 
				  String revioew=txt.getText().toString().trim();
				  if(revioew.isEmpty())
				  {
					  Toast.makeText(user_leaf_comments.this, "Enter Review", Toast.LENGTH_SHORT).show();  
				  }
				  
				  else
				  {
					  new userlogin().execute();
				  }
 				 
			}
        });
//		
//	 
//		
//		user.setOnClickListener(new OnClickListener() 
//        {
//
//			 
//			public void onClick(View arg0) {
//				 
//				 Intent in = new Intent(getApplicationContext(),user_reg.class);
//		        startActivity(in);		 
//			} 
//        });
//		logout.setOnClickListener(new OnClickListener() 
//        {
//
//			 
//			public void onClick(View arg0) {
//				 
//				finish();	 
//			} 
//        });
// 
//		 
//    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public class userlogin extends AsyncTask<String, String, String> {

	    
		  
		  String revioew=txt.getText().toString().trim();

		        @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(user_leaf_comments.this);
		            pDialog.setMessage("Requesting..");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }
		        protected String doInBackground(String... args) {
	            String txt = "";
		            try 
		            {
		            	
		                String ur = "http://"+MainActivity.sip+"/customer_review_insert.php?"
				       // + "uname=" +URLEncoder.encode( user_login.uemail , "UTF-8")  		                
		                //+ "uname=" +URLEncoder.encode( user_leaf_1.id , "UTF-8")  		                
		                + "&review=" +URLEncoder.encode( revioew , "UTF-8")
		                + "&cid=" +URLEncoder.encode( user_leaf_1.id , "UTF-8")
		                ;
		                URL url = new URL(ur);
		                Log.i("URL", ""+url);
		                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		                DataInputStream dis = new DataInputStream(uc.getInputStream());
		                String t = "";
		                while ((t = dis.readLine()) != null) 
		                {
		                    txt += t;
		                }
		                Log.i("Read", txt);
		              //  m=txt;
		                dis.close();
		                               
		            } catch (Exception e) {
		                Log.i("Login Ex", e.getMessage());
		            }
		            return txt;
		        }
		        protected void onPostExecute(String file_url) 
		        {
		      	  Log.i("file_url", file_url);
		          if (file_url.trim().equals("success")) 
		          {
		               	Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
		                	finish();
			            
		          
		          }
		           else if(file_url.trim().equals("failed")) {
		              Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
		          }
		          else
		          { Toast.makeText(getApplicationContext(), "Please Check Login...!", Toast.LENGTH_LONG).show();}

		          pDialog.dismiss();
		      }
		  
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
	                             listview.setAdapter(adapter); 
	    					 
	    					 
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
	        HttpPost httppost = new HttpPost("http://"+MainActivity.sip+"/customer_review_load_driver.php");
	        InputStream is = null;
	        String result = "";
	        try {
	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

	          nameValuePairs.add(new BasicNameValuePair("cid",user_leaf_1.id));
	           
	            
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

//		        TextView txtFirstName = (TextView) rootView.findViewById(R.id.txtFirstName);
//		        txtFirstName.setText(""+res);

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
	        
	              String d=js.getString("review")+"\n";

					values[i] = d;
				      categories.add(d);
				     // country.add(""+d);
				  //   categories1.add(js.getString("tname"));

			    // System.out.println("value q : "+values[i]);

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
