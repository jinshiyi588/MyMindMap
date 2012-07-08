package com.loading;

import com.mindMap.MyMindMapActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class loading extends Activity {

	int MSG_INIT_OK = 1;
	int MSG_INIT_INFO = 2;
	int MSG_INIT_TIMEOUT = 9;

	boolean isTimeout = false;

	TextView tvInfo ;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//设置全屏  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading);//loading界面
		tvInfo = (TextView)findViewById(R.id.tvInfo);//为了显示进度说明，如：初始化1...
		initSystem();//初始化

	}

	private void initSystem(){
		initThread();
		mHandler.postDelayed(timeOutTask,60000);//60秒超时
	}


	public Handler mHandler = new Handler(){     
		public void handleMessage(Message msg){
			if(msg.what == MSG_INIT_TIMEOUT){    //超时动作
				if (mHandler != null && timeOutTask != null ){
					mHandler.removeCallbacks(timeOutTask);
				}
				//初始化失败的显示信息
				Toast.makeText(loading.this, "timeout", Toast.LENGTH_LONG).show();
				loading.this.finish();

			}else if(msg.what == MSG_INIT_OK){
				if (mHandler != null && timeOutTask != null ){
					mHandler.removeCallbacks(timeOutTask);
				}
				
				//初始化成功，进入主界面MyMindMapActivity
				startActivity(new Intent(getApplication(),MyMindMapActivity.class));
				loading.this.finish();
			}else if(msg.what == MSG_INIT_INFO){
				//正在初始化
				String info = msg.obj.toString();
				tvInfo.setText(info);       
			}
		}     
	};

	Runnable timeOutTask = new Runnable() {
		public void run() {
			//超时
			isTimeout = true;

			Message msg = Message.obtain();
			msg.what = MSG_INIT_TIMEOUT;
			mHandler.sendMessage(msg);
		}    
	};

	private void initThread(){
		new Thread(){
			public void run() {
				try {
					if(!isTimeout){
						sendInitInfo("初始化 step 1");         
						Thread.sleep(2000);//TODO 1
					}

					if(!isTimeout){
						sendInitInfo("初始化 step 2");
						Thread.sleep(3000);//TODO 2        
					}

					if(!isTimeout){
						sendInitInfo("初始化 step 3");
						Thread.sleep(4000);//TODO 2
					}

					if(!isTimeout){
						//初始化完成
						sendInitInfo("初始化完成");

						Message msg2 = Message.obtain();
						msg2.what = MSG_INIT_OK;
						mHandler.sendMessage(msg2);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}           
		}.start();
	}
	//初始化时
	private void sendInitInfo(String info){
		Message msg1 = Message.obtain();
		msg1.what = MSG_INIT_INFO;
		msg1.obj = info;
		mHandler.sendMessage(msg1);
	}
}