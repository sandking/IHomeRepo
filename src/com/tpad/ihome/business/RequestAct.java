package com.tpad.ihome.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.tpad.ihome.R;
import com.tpad.ihome.entity.MemberInfo;
import com.tpad.ihome.serv.IEventCallback;
import com.tpad.ihome.serv.IMemberChangedCallback;
import com.tpad.ihome.serv.IPhoneStateCallback;
import com.tpad.ihome.serv.IServManager;
import com.tpadsz.ihome.fancycoverflow.FancyCoverFlow;
import com.tpadsz.ihome.fancycoverflow.FancyCoverFlowAdapter;

/**
 * Navigate activity. Request for calling out.
 * 
 * @author sk
 * 
 */
public class RequestAct extends RemoteActivity implements OnCheckedChangeListener, OnItemClickListener
{
	public static final int _RAZEM_INFO_TITLE = 0x01;   
	public static final int _RAZEM_INFO_CALL_STATUS = 0x02;    
	public static final int _RAZEM_INFO_ICON = 0x04;
	public static final int _RAZEM_INFO_ID = 0x08;
	public static final int _RAZEM_INFO_ALL = 0x0F;   
   
	public final static int LOADING_TIME_OUT = 1000;
	public final static int ITEM_FANCY_WIDTH = 300;
	public final static int ITEM_FANCY_HEIGHT = 250;

	private static final String DEVICE_TITLE = "sk.";
	private static final String DEVICE_NICK = "sk.";
    
	private FancyCoverFlow converflow_preview;
	private RadioGroup radiogp;
	private View view_loading;     

	private IMemberChangedCallback mMemberCallback;
	private IPhoneStateCallback mPhoneStateCallback;
	private IEventCallback mEventCallback;
	private IServManager mRemoteServ;

	// Paramter
	private List<MemberInfo> infos = new ArrayList<MemberInfo>();
	private PreviewAdapter adapter_preview;

	private boolean isShown = true;

	private byte[] lock_shown = new byte[0];
	private byte[] self_icon = null;

	private final Runnable action_update_preview = new Runnable()
	{
		@Override
		public void run()
		{
			if (adapter_preview != null)
				adapter_preview.notifyDataSetChanged();
		}
	};

	private final Runnable start_loading = new Runnable()
	{
		@Override
		public void run()
		{
			synchronized (lock_shown)
			{
				if (!isShown)
				{
					view_loading.setVisibility(View.VISIBLE);
					isShown = true;
				}
			}
		}
	};

	private final Runnable dimiss_loading = new Runnable()
	{
		@Override
		public void run()
		{
			synchronized (lock_shown)
			{
				if (isShown)
				{
					view_loading.setVisibility(View.INVISIBLE);
					isShown = false;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_request);

		converflow_preview = (FancyCoverFlow) findViewById(R.id.cf_request_preview);
		radiogp = (RadioGroup) findViewById(R.id.radiogp_request_group);
		view_loading = findViewById(R.id.view_request_loading);

		adapter_preview = new PreviewAdapter();

		radiogp.setOnCheckedChangeListener(this);
		converflow_preview.setOnItemClickListener(this);
		converflow_preview.setAdapter(adapter_preview);

		mMemberCallback = new MemberChangedCallback();
		mPhoneStateCallback = new PhoneStateCallback();
		mEventCallback = new EventCallback();

		self_icon = readFromRaw(R.raw.sk);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		printf(">>>>>>>> Count : %d", count);
		setTitle("count is :" + count);
//		if (_TEST_CALL_OUT_)
//			testCallOut();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		infos.clear();
	}

	/****************** Test Pattern *********************/

	public void testCallOut()
	{
		post_delay(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					mRemoteServ.rzCall(0xffff0009);
				} catch (RemoteException e)
				{
					e.printStackTrace();
				}
			}
		}, 10);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		super.onServiceConnected(name, service);

		mRemoteServ = getRemoteService();

		try
		{
			mRemoteServ.rzSetIPhoneStateCallback(mPhoneStateCallback);
			mRemoteServ.rzSetIMemberChangedCallback(mMemberCallback);
			mRemoteServ.rzSetIEventCallback(mEventCallback);

			mRemoteServ.rzSetInfor(_RAZEM_INFO_ALL, DEVICE_NICK, DEVICE_TITLE, 0, self_icon);

			request_for_online();

		} catch (RemoteException e)
		{
			processRemoteException(e);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		switch (checkedId)
		{
		case R.id.radio_request_online:
			try
			{
				request_for_online();
			} catch (RemoteException e)
			{
				processRemoteException(e);
			}
			break;
		case R.id.radio_request_local:

			request_for_local();

			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		MemberInfo info = infos.get(position);
		try
		{
			final int account = info.getAccountId();

			mRemoteServ.rzCall(account);
		} catch (RemoteException e)
		{
			processRemoteException(e);
		}
	}

	void request_for_online() throws RemoteException
	{
		infos.clear();
		mRemoteServ.rzGetMemberList();

		post_ui(start_loading);
		post_delay(dimiss_loading, LOADING_TIME_OUT);
	}

	void request_for_local()
	{
		infos.clear();
		post_ui(action_update_preview);

		post_ui(start_loading);
		post_delay(dimiss_loading, LOADING_TIME_OUT);
	}

	final class EventCallback extends IEventCallback.Stub
	{
		@Override
		public void onReceiveEvent(int event) throws RemoteException
		{
			printf("onReceiveEvent - %d", event);

			if (event == 3)
				finish();
		}
	}

	final class PhoneStateCallback extends IPhoneStateCallback.Stub
	{
		@Override
		public void onCallSuccess(String ip, int port, String key, int link_dir, int udp_socket) throws RemoteException
		{
			count++;
			goto_communication(TYPE_CALL_OUT, ip, port, key, link_dir, udp_socket);
		}

		@Override
		public void onCallFailed(final int state) throws RemoteException
		{
			printf("onCallFailed - %d", state);

			post_ui(new Runnable()
			{
				@Override
				public void run()
				{
					Toast.makeText(RequestAct.this, String.format("onCalloutFailed - %d", state), Toast.LENGTH_SHORT).show();
				}
			});

//			if (_TEST_CALL_OUT_)
//				testCallOut();
		}
	}

	private MemberInfo forgeMemberInfo(int accid)
	{
		return new MemberInfo(accid, "", "", 0, null);
	}

	final class MemberChangedCallback extends IMemberChangedCallback.Stub
	{
		@Override
		public void onMemberAcquired(int accountid, String id, String title, int callstatus, byte[] icon) throws RemoteException
		{
			printf("onMemberAcquired - %d - %s - %s - %d - %d", accountid, id, title, callstatus, icon == null ? -1 : icon.length);

			MemberInfo info = new MemberInfo(accountid, id, title, callstatus, icon);
			int infoIndex = infos.indexOf(info);

			if (infoIndex == -1)
				infos.add(info);
			else 
				infos.add(infoIndex, info);

			post_ui(action_update_preview);   
		}

		@Override
		public void onMemberOnlineStateChanged(int account_id, int online_offline) throws RemoteException
		{
			printf("onMemberOnlineStateChanged - %s - %d", Integer.toHexString(account_id), online_offline);
			if (online_offline == 0)
			{
				int infoIndex = infos.indexOf(forgeMemberInfo(account_id));

				if (infoIndex != -1)
				{
					infos.remove(infoIndex);
					post_ui(action_update_preview);
				}
			} else
				mRemoteServ.rzGetMemberInfo(account_id, 0x07);
		}

		@Override
		public void onMemberChanged(int account_id, int changed_bits) throws RemoteException
		{
			printf("onMemberChanged - %d - %d", account_id, changed_bits);
			// mRemoteServ.rzGetMemberInfo(account_id, changed_bits);
		}
	}

	private final static class ViewHolder
	{
		public ImageView img_icon;
		public TextView txt_nick, txt_status;
	}

	private final class PreviewAdapter extends FancyCoverFlowAdapter
	{
		ViewHolder mHolder;

		@Override
		public int getCount()
		{
			return infos.size();
		}

		@Override
		public Object getItem(int position)
		{
			return infos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;  
		}

		@Override
		public View getCoverFlowItem(int position, View reusableView, ViewGroup parent)
		{
			if (reusableView == null)
			{
				reusableView = getLayoutInflater().inflate(R.layout.item_request_preview, null);

				mHolder = new ViewHolder();

				mHolder.img_icon = (ImageView) reusableView.findViewById(R.id.img_item_request_icon);
				mHolder.txt_nick = (TextView) reusableView.findViewById(R.id.txt_item_request_nick);
				mHolder.txt_status = (TextView) reusableView.findViewById(R.id.txt_item_request_status);
			} else
				mHolder = (ViewHolder) reusableView.getTag();

			final MemberInfo info = (MemberInfo) getItem(position);
			final byte[] icon = info.getIcon();

			final int account = info.getAccountId();   
			final String title = info.getTitle();   
			final String status = String.valueOf(info.getCallstatus());

			if (icon == null || icon.length == 0)
				mHolder.img_icon.setImageResource(R.drawable.ic_launcher);   
			else
				mHolder.img_icon.setImageBitmap(BitmapFactory.decodeByteArray(icon, 0, icon.length));

			mHolder.txt_nick.setText(String.format("%s (%d)", title, account));   
			mHolder.txt_status.setText(status);

			reusableView.setLayoutParams(new FancyCoverFlow.LayoutParams(ITEM_FANCY_WIDTH, ITEM_FANCY_HEIGHT));
			return reusableView;
		}
	}
}
