package com.waveface.installer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.webkit.URLUtil;

public class UpdateActivity extends Activity {
	private static final String TAG = "Installer";
	//下載及安裝新版
	private String currentFilePath = "";
	private String currentTempFilePath = "";
	private String fileEx="apk";
	private String mApkName="Picture";
	private String mVersion ; 
	private String downloadURL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update);
		Log.i( TAG, "onCreate()");
		Intent intent = getIntent();
		mVersion = intent.getStringExtra("version");	
//		serverApkVersion = intent.getStringExtra("serverApkVersion");
		downloadURL = intent.getStringExtra("APK_URL");
//		mApkName = intent.getStringExtra("APK_NAME");
		try {
							
			Log.d(TAG, "server-->downloadURL:"+downloadURL);
			startProgress();
			
			//TODO 下載最新的 apk檔
			//取得欲安裝程式之檔案名稱
	        fileEx = downloadURL.substring(downloadURL.lastIndexOf(".")+1,downloadURL.length()).toLowerCase();
	        mApkName = downloadURL.substring(downloadURL.lastIndexOf("/")+1,downloadURL.lastIndexOf("."));
			getFile(downloadURL);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			updateResult(false);
		}
		
	}	
	/*當Activity處於onResume狀態時,刪除暫存檔案*/
    @Override 
    protected void onResume() 
    { 
      // TODO Auto-generated method stub   
      /* 刪除暫存檔案 */ 
      delFile(currentTempFilePath); 
      super.onResume(); 
    }
	
	public void updateResult(boolean sucess){
		stopProgress();
		Builder alertDialog = new AlertDialog.Builder(this);
    	alertDialog.setTitle("連線結果");
    	if(sucess){
    		alertDialog.setMessage("連線成功");
    	}else{
    		alertDialog.setMessage("連線位置無效");
    	}
		
		alertDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            	Log.v(TAG, "Close!!!!");
				
            	UpdateActivity.this.finish();

            }
        });
		alertDialog.show();
	}
	
	private Handler handlerFail =new Handler(){ 
		@Override
		    public void handleMessage(Message msg) {
		        super.handleMessage(msg);
		        stopProgress();
		        updateResult(false);
		        
		   } 
		};
	private Handler handlerOk =new Handler(){ 
			@Override
			    public void handleMessage(Message msg) {
			        super.handleMessage(msg);
			        stopProgress();
			        updateResult(true);
			        
			   } 
			};	
	
	private void getFile(final String strPath) {
		
	    try 
	    { 
	      if (strPath.equals(currentFilePath) ) 
	      { 
	          
	       getDataSource(strPath);  
	      }        
	      currentFilePath = strPath;      
	      Runnable r = new Runnable() 
	      {   
	        public void run() 
	        {   
	          try 
	          { 
	             getDataSource(strPath); 
	          } 
	          catch (Exception e) 
	          { 
	            Log.e(TAG, e.getMessage(), e); 
	          } 
	        } 
	      };   
	      new Thread(r).start(); 
	    } 
	    catch(Exception e) 
	    { 
	      e.printStackTrace();
	      handlerFail.sendMessage(new Message());
	    }
	  } 
	  
	   /*取得遠端檔案*/ 
	  private void getDataSource(String strPath) throws Exception 
	  { 
	    if (!URLUtil.isNetworkUrl(strPath)) 
	    { 
	      //mTextView01.setText("錯誤的URL");
	    	//stopProgress();
	    	//Message m = new Message();
	    	//m.obj=(String)"0";
	    	handlerFail.sendMessage(new Message());
	    	
	    	Log.d(TAG, "error url");
	    	//updateResult(false);
	    } 
	    else 
	    { 
	    	try{
	    		/*取得URL*/
		        URL myURL = new URL(strPath); 
		        /*建立連線*/
		        URLConnection conn = myURL.openConnection();   
		        conn.connect();   
		        /*InputStream 下載檔案*/
		        InputStream is = conn.getInputStream();   
		        if (is == null) 
		        { 
		          throw new RuntimeException("stream is null"); 
		        } 
		        /*建立暫存檔案*/ 
		        File myTempFile = File.createTempFile(mApkName, "."+fileEx); 
		        /*取得站存檔案路徑*/
		        currentTempFilePath = myTempFile.getAbsolutePath(); 
		        /*將檔案寫入暫存檔*/ 
		        FileOutputStream fos = new FileOutputStream(myTempFile); 
		        byte buf[] = new byte[128];   
		        do 
		        {   
		          int numread = is.read(buf);   
		          if (numread <= 0) 
		          { 
		            break; 
		          } 
		          fos.write(buf, 0, numread);   
		        }while (true);  
		        
		        /*開啟檔案進行安裝*/
		        openFile(myTempFile);
		        //openFile(c);
		        try 
		        { 
		          is.close(); 
		        } 
		        catch (Exception ex) 
		        { 
		          Log.e(TAG, "error: " + ex.getMessage(), ex); 
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		handlerShowAlertDialog.sendEmptyMessage(0);
	    	}
	      }
	    }  
	   
	  /* 在手機上開啟檔案的method */
	  private void openFile(File f) 
	  {  
		Log.d(TAG, "openFile = "+f.toString());
		handlerStopProgress.sendEmptyMessage(0);
	    Intent intent = new Intent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setAction(android.content.Intent.ACTION_VIEW);
	    
	    /* 呼叫getMIMEType()來取得MimeType */
	    String type = getMIMEType(f);
	    /* 設定intent的file與MimeType */
	    intent.setDataAndType(Uri.fromFile(f),type);
	    startActivity(intent);
	    finish();
	  }

	  /* 判斷檔案MimeType的method */
	  private String getMIMEType(File f) 
	  { 
	    String type="";
	    String fName=f.getName();
	    /* 取得副檔名 */
	    String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase(); 
	    
	    /* 依附檔名的類型決定MimeType */
	    if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
	    {
	      type = "audio"; 
	    }
	    else if(end.equals("3gp")||end.equals("mp4"))
	    {
	      type = "video";
	    }
	    else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||end.equals("jpeg")||end.equals("bmp"))
	    {
	      type = "image";
	    }
	    else if(end.equals("apk")) 
	    { 
	      /* android.permission.INSTALL_PACKAGES */ 
	      type = "application/vnd.android.package-archive"; 
	    } 
	    else
	    {
	      type="*";
	    }
	    /*如果無法直接開啟，就跳出軟體清單給使用者選擇 */
	    if(end.equals("apk")) 
	    { 
	    } 
	    else 
	    { 
	      type += "/*";  
	    } 
	    return type;  
	  } 

	  /*自訂刪除檔案方法*/
	  private void delFile(String strFileName) 
	  { 
	    File myFile = new File(strFileName); 
	    if(myFile.exists()) 
	    { 
	      myFile.delete(); 
	    } 
	  }
	
	    /**
	     * 日期比較
	     * @param now
	     * @param before
	     * @return
	     */
	    public boolean isSameDate(String now,String before){
	    	boolean result = false;
	    	if(now.equals(before)) result = true;
	    	return result;
	    }
	    
	    ProgressDialog progressDialog =  null;
	    public void startProgress(){
	    	//ProgressDialog progressDialog =  null;
	    	//progressDialog = ProgressDialog.show(UpdateActivity.this, "Download APK ", "下載中，請稍候。(Version:"+nowApkVersion+"-->"+serverApkVersion+")",true);
	    	progressDialog = ProgressDialog.show(UpdateActivity.this, "Download APP ", "下載中，請稍候。(Version:"+mVersion+")",true);
	    }
	  
	    public void stopProgress(){
	    	if(progressDialog != null){
	    		progressDialog.dismiss();
	    	}

	    }
	    
	    private Handler handlerStopProgress =new Handler(){ 
			@Override
			    public void handleMessage(Message msg) {
			        super.handleMessage(msg);
			        stopProgress();
			   } 
			};

	    
	    
	    private Handler handlerShowAlertDialog =new Handler(){ 
			@Override
			    public void handleMessage(Message msg) {
			        super.handleMessage(msg);
			        stopProgress();
			        Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
			    	alertDialog.setTitle("無法取得連線");
					alertDialog.setMessage("因網路連線問題，目前無法進行檔案下載");
					
					alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			                // TODO Auto-generated method stub
			            	Log.v(TAG, "Close!!!!");
			            	UpdateActivity.this.finish();
			            }
			        });
					
			        alertDialog.show();
			   } 
			};
}
