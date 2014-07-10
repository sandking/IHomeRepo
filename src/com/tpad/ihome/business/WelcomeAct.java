package com.tpad.ihome.business;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.tpad.ihome.R;
import com.tpad.ihome.serv.IServManager;
import com.tpad.ihome.serv.LoginManager.LoginState;

public class WelcomeAct extends RemoteActivity
{
	private final static int DELAY_SKIPPING = 1000;

	private IServManager mRemoteServ;

	private Runnable skipping_login_runnable = new Runnable()
	{   
		@Override
		public void run()
		{
			goto_act(new Bundle(), LoginAct.class);
			finish();
		}
	};

	private Runnable skipping_request_runnable = new Runnable()
	{
		@Override
		public void run()
		{
			goto_act(new Bundle(), RequestAct.class);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_welcome);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		super.onServiceConnected(name, service);
		mRemoteServ = getRemoteService();

		try
		{
			LoginState state = LoginState.values()[mRemoteServ.rzGetLoginState()];
			printf("Login State is %s  !", state);

			Runnable skipping_runnable = null;

			switch (state)
			{
			case ONLINE:
				skipping_runnable = skipping_request_runnable;
				break;
			default:
				skipping_runnable = skipping_login_runnable;
				break;
			}

			post_delay(skipping_runnable, DELAY_SKIPPING);

		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
}
