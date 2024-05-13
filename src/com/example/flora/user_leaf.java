package com.example.flora;



 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

 
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
 

public class user_leaf extends Activity {
	
	Button details,upload;
	TextView imgname;
    ImageView viewImage;
     String ppath;
     public static double f_volume=0,f_surface=0,f_multiple=0,fs4=0;
     public static String ph="0",soil="";   
     public static Bitmap in_image;
     private ProgressDialog dialog = null;
    private int serverResponseCode = 0;
    private String upLoadServerUri = null;
    public static String aheight="";
    public static String awidth="";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_leaf);
        upLoadServerUri="http://"+MainActivity.sip+"/upload_image.php";
        
	imgname = (TextView) findViewById(R.id.textView9);
	details = (Button) findViewById(R.id.button1);
	upload = (Button) findViewById(R.id.button2);
    viewImage=(ImageView)findViewById(R.id.imageView1);

		///////////
	        imgname.setOnClickListener(new OnClickListener() 
	        { 
				public void onClick(View arg0) { 
					 selectImage();
				}
	        });
	        details.setOnClickListener(new OnClickListener() 
	        { 
				public void onClick(View arg0) { 
					  Intent in = new Intent(getApplicationContext(), user_leaf_1.class);
			           startActivity(in);
				}
	        });
	        upload.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	final int w = in_image.getWidth();
	            	  final int h = in_image.getHeight();
	        
	            	//  Toast.makeText(getApplicationContext(),""+w+"-"+h, Toast.LENGTH_LONG).show();
	                          Histogram1();

	                          Toast.makeText(getApplicationContext(), "Test Ok", Toast.LENGTH_LONG).show();
		                           
	                          
	                          
	                  
	                 // Color v1=Color((int)sum_red, (int)sum_green,(int) sum_blue);
	                // TODO Auto-generated method stub
//	                System.out.println("path value..."+ppath);
//
//	                dialog = ProgressDialog.show(userhome.this, "", "Uploading file...", true);
//	                //messageText.setText("uploading started.....");
//	                 new Thread(new Runnable() {
//	                     public void run() {
//	                int y=uploadFile(ppath);
//	                }
//	}).start();  
	            }
	        });
	}
	 
	
	
	public static void Histogram1()
	{
		 int[] counts = new int[20];
	 
		     double ent[]=new double[in_image.getWidth()* in_image.getHeight()]; 
		  int a=0;
		 //  System.out.print("Height: "+in_image.getHeight()); 
		 // System.out.print("Width : "+in_image.getWidth()); 
		   aheight=""+in_image.getWidth();
		   awidth=""+in_image.getHeight();
	        for (int r = 0; r < in_image.getHeight(); r++) {
	            for (int c = 0; c < in_image.getWidth(); c++) {
	                int v = (in_image.getPixel(c, r) & 0xff) * 20 / 256;
	                counts[v]++;
	                ent[a]=v;
	                 
	                  // System.out.println(""+v); 

	                int pixel = in_image.getPixel(c, r);
	                 double radius = 0;
	    double volume = 0;
	    double area = 0;
	    double mor=0;    double mor1=0;
	  //  System.out.print("Radius 1of Sphere: "); 
	   //  System.out.print("Radius 1of Sphere: "); 
	    radius = v;//pixel;
	 
	    volume = (4 * Math.PI * Math.pow(radius, 3)) / 3;
	    area = 4 * Math.PI * Math.pow(radius, 2);
	    
	    if((a%2)==0)
	    {
	        mor=volume;  
	    }
	    else
	    {
	     mor=area;     
	    }
	      
	 f_volume= f_volume+volume;
	f_surface=f_surface+area;
	 f_multiple=f_multiple+mor;
	a++;
	      }
	        }
	            f_volume=f_volume/(a-1);   
	     f_surface=f_surface/(a-1); 
	   //  f_multiple=f_volume+f_surface;
	    // f_multiple=f_multiple/2;
	     f_multiple=f_multiple/(a-1);
	     System.out.println("f_volume : "+f_volume);         
         System.out.println("f_surface : "+f_surface);         
         System.out.println("f_multiple : "+f_multiple); 
         
//         double h=f_volume;
//         double s=f_surface;
//         double f=fs4;
              
//         System.out.println("f_volume : "+f_volume);
//         System.out.println("f_surface : "+f_surface);
//         System.out.println("fs4 : "+fs4);
	}
	
	
	public static int[][] getSampleFeature( ) {
        int t=0;
	int height = in_image.getHeight(), width = in_image.getWidth();
	int sampleCount = 8;
	int[][] features = new int[sampleCount][sampleCount];
	for (int i = 0; i < sampleCount; i++) {
		for (int j = 0; j < sampleCount; j++) {
			int count = 0;
			int x0 = (int)Math.rint(width * i * 1.0 / sampleCount), y0 = (int)Math.rint(height * j * 1.0 / sampleCount);
			for (int x = x0; x < (int)Math.rint(width * (i + 1) * 1.0 / sampleCount); x++) {
				for (int y = y0; y < (int)Math.rint(height * (j + 1) * 1.0 / sampleCount); y++) {
					// int colour = in_image.getPixel(x, y);
					if (in_image.getPixel(x, y) != -1) {
						count++;
					}
				}
			}
			features[i][j] = count;
                       fs4=fs4+count;
                       t++;
                       			}
	}
            
             fs4= fs4/t;
            
            
	return features;
}
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void selectImage() {

        final CharSequence[] options = {"Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(user_leaf.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                     System.out.println("before call ...");
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (resultCode == RESULT_OK) {
	            if (requestCode == 1) {
	                File f = new File(Environment.getExternalStorageDirectory().toString());
	                for (File temp : f.listFiles()) {
	                    if (temp.getName().equals("temp.jpg")) {
	                        f = temp;
	                        break;
	                    }
	                }
	                Bitmap bitmap;
	                try {
	                    
	                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

	                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
	                            bitmapOptions); 

	                    viewImage.setImageBitmap(bitmap);


	                    String path = android.os.Environment
	                            .getExternalStorageDirectory()
	                            + File.separator+"temp.jpg";
	                          //  + "Phoenix" + File.separator + "default";

	                    System.out.println("image path..."+path);
	                    
	                    ppath=path;
/////////////////////////////////////////
	                    
	                    
	                    ////////////////////////////////////////
	                    //uploadFile(ppath);

	                  //  f.delete();
	                    OutputStream outFile = null;
	                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
	                    try {
	                        outFile = new FileOutputStream(file);
	                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
	                        outFile.flush();
	                        outFile.close();
	                    } catch (FileNotFoundException e) {
	                        e.printStackTrace();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            } else if (requestCode == 2) {

	                Uri selectedImage = data.getData();
	                String[] filePath = { MediaStore.Images.Media.DATA };
	                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
	                c.moveToFirst();
	                int columnIndex = c.getColumnIndex(filePath[0]);
	                String picturePath = c.getString(columnIndex);
	                c.close();
	                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
	                Log.w("path of image from gallery......******************.........", picturePath+"");

	                String selectedImagePath;
	                selectedImagePath = getPath(selectedImage);

	                System.out.println("before set image...");
	                viewImage.setImageBitmap(thumbnail);
	                in_image=thumbnail;
	               // uploadFile(ppath);
	                System.out.println("image path from gallery..."+selectedImagePath);

	                ppath=selectedImagePath;
	            }
	        }
	    }
	 public int uploadFile(String ppath2) {

         String fileName = ppath2;

         HttpURLConnection conn = null;
         DataOutputStream dos = null;  
         String lineEnd = "\r\n";
         String twoHyphens = "--";
         String boundary = "*****";
         int bytesRead, bytesAvailable, bufferSize;
         byte[] buffer;
         int maxBufferSize = 1 * 1024 * 1024; 
         File sourceFile = new File(ppath2); 

         if (!sourceFile.isFile()) {

              dialog.dismiss(); 

              Log.e("uploadFile", "Source File not exist :"+ppath);

              runOnUiThread(new Runnable() {
                  public void run() {
                    //  messageText.setText("Source File not exist :"+ ppath);
                  }
              }); 

              return 0;

         }
         else
         {
              try { 

                    // open a URL connection to the Servlet
                  FileInputStream fileInputStream = new FileInputStream(sourceFile);
                  String name="arun";//login.uemail;
                //  String stype=spinner.getSelectedItem().toString();
                 // String location=lat+","+lan;
             //  String plevel=spinner1.getSelectedItem().toString();
                  upLoadServerUri="http://"+MainActivity.sip+"/upload_image.php?name="+URLEncoder.encode(name, "UTF-8");
//
                   URL url = new URL(upLoadServerUri);
                   Log.i("path and values", upLoadServerUri);

                  // Open a HTTP  connection to  the URL
                  conn = (HttpURLConnection) url.openConnection(); 
 
                  conn.setDoInput(true); // Allow Inputs
                  conn.setDoOutput(true); // Allow Outputs
                  conn.setUseCaches(false); // Don't use a Cached Copy
                  conn.setRequestMethod("POST");
                  conn.setRequestProperty("Connection", "Keep-Alive");
                  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                  conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                  conn.setRequestProperty("uploaded_file", fileName); 

                  dos = new DataOutputStream(conn.getOutputStream());

                  dos.writeBytes(twoHyphens + boundary + lineEnd); 
                  /////////////ar test
                   
	               
//	               
// 	                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
//	                DataInputStream dis = new DataInputStream(uc.getInputStream());
//	                String txt="";
//                 DataInputStream dis = new DataInputStream(conn.getInputStream());
//                String t = "";
//
//                while ((t = dis.readLine()) != null) {
//                    txt += t;
//                }
//                Log.i("Read", txt);
// 
//                               
     
          ////////////end ar test
                  
                  
                  
                //  dos.writeBytes("name=arun"); 
                  dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
 

                  dos.writeBytes(lineEnd);

                  // create a buffer of  maximum size
                  bytesAvailable = fileInputStream.available(); 

                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  buffer = new byte[bufferSize];

                  // read file and write it into form...
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

                  while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

                   }

                  // send multipart form data necesssary after file data...
                  dos.writeBytes(lineEnd);
                  dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                  // Responses from the server (code and message)
                  serverResponseCode = conn.getResponseCode();
                  String serverResponseMessage = conn.getResponseMessage();

                  Log.i("uploadFile", "HTTP Response is : "
                          + serverResponseMessage + ": " + serverResponseCode);

                  if(serverResponseCode == 200){

                      runOnUiThread(new Runnable() {
                           public void run() {
                               String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                     +" F:/wamp/wamp/www/uploads";
                               //messageText.setText(msg);
                               Toast.makeText(user_leaf.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                               
                               
                           }
                       });                
                  }    

                  //close the streams //
                  fileInputStream.close();
                  dos.flush();
                  dos.close();
               


             } catch (MalformedURLException ex) {

                 dialog.dismiss();  
                 ex.printStackTrace();

                 runOnUiThread(new Runnable() {
                     public void run() {
                         //messageText.setText("MalformedURLException Exception : check script url.");
                         Toast.makeText(user_leaf.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                     }
                 });

                 Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
             } catch (Exception e) {

                 dialog.dismiss();  
                 e.printStackTrace();

                 runOnUiThread(new Runnable() {
                     public void run() {
                         //messageText.setText("Got Exception : see logcat ");
                         Toast.makeText(user_leaf.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                     }
                 });
                 Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
             }
              
          
              
              
             dialog.dismiss();       
             return serverResponseCode; 

          } // End else block 
        }


    private String getPath(Uri uri) {
        // TODO Auto-generated method stub

        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();

    }   
}
