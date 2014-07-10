package com.tpad.ihome.business;

import android.content.ComponentName;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tpad.ihome.R;
import com.tpad.ihome.serv.IPhoneStateCallback;
import com.tpad.ihome.serv.IServManager;

/**
 * Response for answer or reject.
 * 
 * @author sk
 * 
 */
public class ResponseAct extends RemoteActivity implements OnClickListener
{
	private Button //
			btn_answer,//
			btn_reject;//

	private TextView txt_incomming;

	private IServManager mRemoteServ;
	private IPhoneStateCallback mPhoneStateCallback;

	private MediaPlayer mPlayer;

	private int target;
	private boolean is_play;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_response);

		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		txt_incomming = (TextView) findViewById(R.id.txt_response_incomming);
		btn_answer = (Button) findViewById(R.id.btn_response_answer);
		btn_reject = (Button) findViewById(R.id.btn_response_reject);

		btn_answer.setOnClickListener(this);
		btn_reject.setOnClickListener(this);

		target = get_bundle().getInt("target", -1);

		mPhoneStateCallback = new PhoneStateCallback();

		mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio_callin);
		mPlayer.setLooping(true);

//		if (_TEST_CALL_IN_)
//			testAnswer();
	}

	/****************** Test Pattern **********************/

	public void testAnswer()
	{
		post_delay(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					mRemoteServ.rzAnswer();
				} catch (RemoteException e)
				{
					processRemoteException(e);
				}
			}
		}, 200);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		final String txt = target == -1 ? "unknow page!!!" : String.valueOf(target);
		txt_incomming.setText(txt);
		start();
	}

	void start()
	{
		if (!is_play && target != -1)
		{
			is_play = true;
			mPlayer.start();
		}
	}

	void stop()
	{
		if (is_play)
		{
			is_play = false;
			mPlayer.stop();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		stop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mPlayer != null)
		{
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		super.onServiceConnected(name, service);
		mRemoteServ = getRemoteService();
		try
		{
			mRemoteServ.rzSetIPhoneStateCallback(mPhoneStateCallback);
		} catch (RemoteException e)
		{
			processRemoteException(e);
		}
	}

	@Override
	public void onClick(View v)
	{
		stop();
		switch (v.getId())
		{
		case R.id.btn_response_answer:
			try
			{
				mRemoteServ.rzAnswer();
			} catch (RemoteException e)
			{
				processRemoteException(e);
			}
			break;
		case R.id.btn_response_reject:
			try
			{
				mRemoteServ.rzReject();
				finish();
			} catch (RemoteException e)
			{
				processRemoteException(e);
			}
			break;
		}
	}

	final class PhoneStateCallback extends IPhoneStateCallback.Stub
	{
		@Override
		public void onCallSuccess(String ip, int port, String key, int link_dir, int udp_socket) throws RemoteException
		{
			printf("onCallSuccess - %s - %d - %s - %d", ip, port, key, link_dir);
			goto_communication(TYPE_CALL_IN, ip, port, key, link_dir, udp_socket);
			post_ui(new Runnable()
			{
				@Override
				public void run()
				{
					finish();
				}
			});
		}

		@Override
		public void onCallFailed(int state) throws RemoteException
		{
			printf("onCallFailed - %d", state);
		}
	}
}
