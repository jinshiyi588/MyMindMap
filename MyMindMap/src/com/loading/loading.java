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
		
		//����ȫ��  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading);//loading����
		tvInfo = (TextView)findViewById(R.id.tvInfo);//Ϊ����ʾ����˵�����磺��ʼ��1...
		initSystem();//��ʼ��

	}

	private void initSystem(){
		initThread();
		mHandler.postDelayed(timeOutTask,60000);//60�볬ʱ
	}


	public Handler mHandler = new Handler(){     
		public void handleMessage(Message msg){
			if(msg.what == MSG_INIT_TIMEOUT){    //��ʱ����
				if (mHandler != null && timeOutTask != null ){
					mHandler.removeCallbacks(timeOutTask);
				}
				//��ʼ��ʧ�ܵ���ʾ��Ϣ
				Toast.makeText(loading.this, "timeout", Toast.LENGTH_LONG).show();
				loading.this.finish();

			}else if(msg.what == MSG_INIT_OK){
				if (mHandler != null && timeOutTask != null ){
					mHandler.removeCallbacks(timeOutTask);
				}
				
				//��ʼ���ɹ�������������MyMindMapActivity
				startActivity(new Intent(getApplication(),MyMindMapActivity.class));
				loading.this.finish();
			}else if(msg.what == MSG_INIT_INFO){
				//���ڳ�ʼ��
				String info = msg.obj.toString();
				tvInfo.setText(info);       
			}
		}     
	};

	Runnable timeOutTask = new Runnable() {
		public void run() {
			//��ʱ
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
						sendInitInfo("��ʼ�� step 1");         
						Thread.sleep(2000);//TODO 1
					}

					if(!isTimeout){
						sendInitInfo("��ʼ�� step 2");
						Thread.sleep(3000);//TODO 2        
					}

					if(!isTimeout){
						sendInitInfo("��ʼ�� step 3");
						Thread.sleep(4000);//TODO 2
					}

					if(!isTimeout){
						//��ʼ�����
						sendInitInfo("��ʼ�����");

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
	//��ʼ��ʱ
	private void sendInitInfo(String info){
		Message msg1 = Message.obtain();
		msg1.what = MSG_INIT_INFO;
		msg1.obj = info;
		mHandler.sendMessage(msg1);
	}
}