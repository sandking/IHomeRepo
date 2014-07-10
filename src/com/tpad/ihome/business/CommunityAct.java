package com.tpad.ihome.business;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tpad.ihome.R;
import com.tpad.ihome.hardfunc.AudioPlayer;
import com.tpad.ihome.hardfunc.AudioRecorder;
import com.tpad.ihome.hardfunc.VideoCapture;
import com.tpad.ihome.inter.VPCalloutListener;
import com.tpad.ihome.inter.VPConnect;
import com.tpad.ihome.inter.VPConnectListener;
import com.tpad.ihome.inter.VPDeInitCompleteListener;
import com.tpad.ihome.inter.VPInitCompleteListener;
import com.tpad.ihome.inter.VPMediaListener;

/**
 * For communication with .
 * 
 * @author sk
 * 
 */
public class CommunityAct extends BaseActivity implements VPInitCompleteListener, VPConnectListener, VPMediaListener, VPCalloutListener, VPDeInitCompleteListener
{
	static
	{
		System.loadLibrary("vp");
	}

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((((( Constant Fields ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	public final static int INITIALIZE_FUNC_SURFACE_CREATED = 0x01;
	public final static int INITIALIZE_FUNC_VIDEO_CAPTURED = 0x02;
	public final static int INITIALIZE_FUNC_ALL = INITIALIZE_FUNC_SURFACE_CREATED | INITIALIZE_FUNC_VIDEO_CAPTURED;

	private final static int BMP_WIDTH = 640;
	private final static int BMP_HEIGHT = 480;
	private final static int VIDEO_CAPTURE_BUF = (int) (BMP_WIDTH * BMP_HEIGHT * 1.5f);

	private final static int AUDIO_BUF = 20 * 1024;
	private final static int TIME_OUT = 60 * 1000;

	// ////////////////////////////////////////////////////////////////////
	// ((((((((((((((((((((((((((( Controller ))))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	private SurfaceView //
			surface_preview,//
			surface_self;

	private SurfaceHolder //
			mSelfHolder,//
			mPreviewHolder;

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((((( Buffer & Hardware ))))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	/* Audio Player */
	private AudioPlayer audio_player;

	/* Audio Recorder */
	private AudioRecorder audio_recorder;

	/* Video Capture */
	private VideoCapture video_capture;

	private byte[] //
			audio_player_buf,//
			audio_recorder_buf,//
			video_capture_buf;

	// ////////////////////////////////////////////////////////////////////
	// (((((((((((((((((((((( Parameter & Locker )))))))))))))))))))))))))
	// ////////////////////////////////////////////////////////////////////

	/********************* Bundle ********************/

	private String bundle_ip;
	private int bundle_port;
	private String bundle_key;
	private int bundle_link_dir;
	private int bundle_udp_socket;

	/********************* Locker ********************/

	private byte[] lock_capture = new byte[0];
	private byte[] lock_preview = new byte[0];
	private byte[] lock_connected = new byte[0];

	/********************* Preview Operation ********************/

	private Bitmap preview_bmp;
	private Canvas mPreviewCanvas;
	private final Paint mPreviewPaint = new Paint();

	private boolean isCalled = false;
	private boolean isPreview = false;
	private boolean isConnected = false;

	private Rect mNativeRect = new Rect();
	private Rect mPreviewRect = new Rect();

	private final Runnable init_runn = new Runnable()
	{
		@Override
		public void run()  
		{ 
			VPConnect.init(CommunityAct.this, preview_bmp, BMP_WIDTH, BMP_HEIGHT, 100001);
		}
	};

	private final Runnable deinit_runn = new Runnable()
	{
		@Override
		public void run()
		{
			checkVM("post delay - { deinit } ");

			VPConnect.deInit(CommunityAct.this);
		}
	};

	private final Callback self_holder_callback = new Callback()
	{
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			printf("self_holder_callback - surfaceCreated !!!");
			capture_start(holder);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			printf("self_holder_callback - surfaceDestroyed !!!");
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			printf("self_holder_callback - surfaceChanged - %d - %d - %d !!!", width, height, format);
		}
	};

	private final Callback pre_holder_callback = new Callback()
	{
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			printf("pre_holder_callback - surfaceCreated !!!");

			if (!isCalled)
			{
				init();
				isCalled = true;
			}

			synchronized (lock_preview)
			{
				isPreview = true;
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			printf("pre_holder_callback - surfaceChanged - %d - %d - %d !!!", width, height, format);
			synchronized (lock_preview)
			{
				mPreviewRect.set(0, 0, width, height);
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			printf("pre_holder_callback - surfaceDestroyed !!!");
			synchronized (lock_preview)
			{
				isPreview = false;
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.act_community);

		preview_bmp = Bitmap.createBitmap(BMP_WIDTH, BMP_HEIGHT, Config.ARGB_8888);

		Bundle bundle = get_bundle();

		bundle_ip = bundle.getString(RemoteActivity.BUNDLE_ADDR_IP);
		bundle_port = bundle.getInt(RemoteActivity.BUNDLE_ADDR_PORT);
		bundle_key = bundle.getString(RemoteActivity.BUNDLE_SERV_KEY);
		bundle_link_dir = bundle.getInt(RemoteActivity.BUNDLE_LINK_DIR);
		bundle_udp_socket = bundle.getInt(RemoteActivity.BUNDLE_UDP_SOCKET);

		surface_self = (SurfaceView) findViewById(R.id.surface_community_self);
		surface_preview = (SurfaceView) findViewById(R.id.surface_community_preview);

		mSelfHolder = surface_self.getHolder();
		mPreviewHolder = surface_preview.getHolder();
		mPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		
		mSelfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSelfHolder.addCallback(self_holder_callback);  

		mPreviewHolder.addCallback(pre_holder_callback);

		mPreviewPaint.setAntiAlias(true);
      
		audio_player_buf = new byte[AUDIO_BUF];
		audio_recorder_buf = new byte[AUDIO_BUF];
		video_capture_buf = new byte[VIDEO_CAPTURE_BUF];
	}

	void init()
	{
		new Thread(init_runn, "VP-Init").start();
	}

	@Override
	public void onInitCompleted()
	{
		printf(">>>> onInitCompleted !!!");

		init_audio_hardware();

		VPConnect.setAudioBuf(audio_player_buf, audio_recorder_buf);
		VPConnect.setCameraBuffer(video_capture_buf);

		VPConnect.setConnectListener(this);
		VPConnect.setMediaListener(this);
		VPConnect.setCalloutListener(this);
		printf("dir : %d , in : %d", bundle_link_dir, RemoteActivity._RAZEM_LINK_DIR_IN);
		if (bundle_link_dir > RemoteActivity._RAZEM_LINK_DIR_IN)
		{
			// post_delay(deinit_runn, TIME_OUT);
			printf("Bundle Ip : %s", bundle_ip);

			printf("VPConnect call %s - %d - %s - %d - %d", bundle_ip, bundle_port, bundle_key, bundle_link_dir, bundle_udp_socket);

			VPConnect.call(bundle_ip, bundle_port, bundle_key, bundle_link_dir, bundle_udp_socket);
		}
	}

	@Override
	public void onDeInitCompleted()
	{
		printf(">>>> onDeInitCompleted !!!");

		release_audio_hardware();

		preview_bmp.recycle();
		finish();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		printf("onResume !!!");

		capture_create();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		printf("onPause !!!");

		capture_stop();
		capture_release();
	}

	@Override
	public void onCallSuccess()
	{
		printf("onCallSuccess!!!");
	}

	@Override
	public void onCallFailed()
	{
		printf("onCallFailed!!!");
		post_ui(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(CommunityAct.this, "Call Out Failed!!!", Toast.LENGTH_SHORT).show();
			}
		});

		post_delay(deinit_runn, 100);
	}

	@Override
	public void onConnected()
	{
		printf("onConnected!!!");

		synchronized (lock_connected)
		{
			isConnected = true;
		}

		printf("remove - deinit runnable !!!");

		remove_run(deinit_runn);

		// if (_TEST_CALL_OUT_)
		// testEndCall();
	}

	@Override
	public void onDisconnected()
	{
		printf("onDisconnected!!!");

		synchronized (lock_connected)
		{
			isConnected = false;
		}

		post_delay(deinit_runn, 100);
	}

	@Override
	public void onVideoPlayed(int x, int y, int w, int h)
	{
		synchronized (lock_preview)
		{
			if (isPreview && mPreviewHolder != null)
			{
				try
				{
					mPreviewCanvas = mPreviewHolder.lockCanvas();

					if (mPreviewCanvas == null)
					{
						printf("onRefresh - canvas is null!!");
						return;
					}

					onDrawPreview(mPreviewCanvas, x, y, w, h);

				} finally
				{
					if (mPreviewCanvas != null)
						mPreviewHolder.unlockCanvasAndPost(mPreviewCanvas);
				}
			}
		}
	}

	@Override
	public int onAudioPlayed(int size)
	{
		int psize = 0;

		if (audio_player != null)
		{
			psize = audio_player.play(audio_player_buf, 0, size);
			// printf("onAudioPlayed ( %d - %d )", psize, size);
		}

		return psize;
	}

	@Override
	public int captureAudio(int size)
	{
		int rsize = 0;

		if (audio_recorder != null)
		{
			rsize = audio_recorder.record(audio_recorder_buf, 0, size);
			// printf("captureAudio ( %d - %d )", rsize, size);
		}

		return rsize;
	}

	@Override
	public int captureVideo(int size)
	{
		synchronized (lock_capture)
		{
			if (video_capture != null)
			{
				video_capture.capture(video_capture_buf);
			}
		}
		return VIDEO_CAPTURE_BUF;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			VPConnect.endCall();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onDrawPreview(Canvas canvas, int x, int y, int w, int h)
	{
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));

		int width = mPreviewRect.right;
		int height = mPreviewRect.bottom;

		int space = ((int) (w - h * 1.0f / height * width) >> 1);

		mNativeRect.set(x + space, y, w - space, h);

		canvas.drawBitmap(preview_bmp, mNativeRect, mPreviewRect, mPreviewPaint);
	}

	private void capture_create()
	{
		if (video_capture == null)
		{
			video_capture = VideoCapture.create();
			video_capture.setCaptureConfig(BMP_WIDTH, BMP_HEIGHT, ImageFormat.NV21);

			printf("capture_create !!!");
		}
	}

	private void capture_start(SurfaceHolder holder)
	{
		if (video_capture != null)
		{
			video_capture.startCapture(holder, video_capture_buf);
			printf("capture_start !!!");
		}
	}

	private void capture_stop()
	{
		if (video_capture != null)
		{
			video_capture.stopCapture();
			printf("capture_stop !!!");
		}
	}

	private void capture_release()
	{
		if (video_capture != null)
		{
			video_capture.release();
			video_capture = null;
			printf("capture_release !!!");
		}
	}

	private void init_audio_hardware()
	{
		// initialize the audio player.
		audio_player = new AudioPlayer();
		audio_player.start();

		// initialize the audio recorder.
		audio_recorder = new AudioRecorder();
		audio_recorder.start();

		printf("init_audio_hardware !!!");
	}

	private void release_audio_hardware()
	{
		if (audio_player != null)
		{
			audio_player.stop();
			audio_player = null;
		}

		if (audio_recorder != null)
		{
			audio_recorder.stop();
			audio_recorder = null;
		}

		printf("release_audio_hardware !!!");
	}

	// /******************* Test Pattern ******************/
	// public void testEndCall()
	// {
	// post_delay(new Runnable()
	// {
	// @Override
	// public void run()
	// {
	// VPConnect.endCall();
	// printf("!!!!!!!!- EndCall");
	// }
	// }, 400);
	// }
}
