package tna.VolumeAdjuster;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.media.AudioManager;

public class VolumeAdjuster extends Activity {
	public static final String PREFS_NAME = "VolumeAdjusterSettingsFile";
	public TimePicker timeToMute;
	public TimePicker timeToRestore;
	public AudioManager Manager;
	public Button knappen;
	public int NotificationVolume=0;
	public int RingVolume=0;
	public int SystemVolume=0;
	public boolean restore = false;
	public Timer timer;
	public long TimeToMute =0;
	public long TimeToRestore = 0;

	public static final ComponentName COMPONENT = new ComponentName("tna","tna.MyService");
	public class MyTimerTask extends TimerTask
	{

		@Override
		public void run() {
			if(restore)
			{
				Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, NotificationVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
				Manager.setStreamVolume(AudioManager.STREAM_RING, RingVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
				Manager.setStreamVolume(AudioManager.STREAM_SYSTEM, SystemVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
				restore = false;
			}
			else
			{
				SystemVolume = Manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
				RingVolume = Manager.getStreamVolume(AudioManager.STREAM_RING);
				NotificationVolume = Manager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
				Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION|AudioManager.STREAM_RING|AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
				restore = true; 
			}	
			UpdateTimers();

		}

	}
	/*public TimerTask timerTask = new TimerTask()
	  {
		  @Override
			public void run() {

			if(restore)
   	    	 {
   	    	Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, NotificationVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		Manager.setStreamVolume(AudioManager.STREAM_RING, RingVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		Manager.setStreamVolume(AudioManager.STREAM_SYSTEM, SystemVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		restore = false;
   	 		timer.cancel();
   	 		timer = new Timer();
   	 		timer.schedule(timerTask,TimeToMute,TimeToMute);
   	    	 }
   	    	 else
   	    	 {
   	    	SystemVolume = Manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
   	 		RingVolume = Manager.getStreamVolume(AudioManager.STREAM_RING);
   	 		NotificationVolume = Manager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
   	 		Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION|AudioManager.STREAM_RING|AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		restore = true; 
   	 		timer.cancel();
   	 		timer = new Timer();
   	 		timer.schedule(timerTask,TimeToRestore,TimeToRestore);
   	 		 }	
			}
	  };
	 */
	public MyTimerTask myTimerTask;

	/*
	SystemVolume = Manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
				RingVolume = Manager.getStreamVolume(AudioManager.STREAM_RING);
				NotificationVolume = Manager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
				Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION|AudioManager.STREAM_RING|AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
				timer.start();
	 */		



	public void Click(View v) {
		//Manager.adjustVolume(AudioManager.ADJUST_LOWER, 0);
		//Manager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		SystemVolume = Manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
		RingVolume = Manager.getStreamVolume(AudioManager.STREAM_RING);
		NotificationVolume = Manager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION|AudioManager.STREAM_RING|AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
		//timer.start();
	}
	public void Click2(View v) {
		Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, NotificationVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
		Manager.setStreamVolume(AudioManager.STREAM_RING, RingVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
		Manager.setStreamVolume(AudioManager.STREAM_SYSTEM, SystemVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
	}
	public void CalculateRemainingTime()
	{
		//Set todays date.
		GregorianCalendar dNow = new GregorianCalendar();
		GregorianCalendar dTomorrow = new GregorianCalendar (dNow.get(Calendar.YEAR),dNow.get(Calendar.MONTH),dNow.get(Calendar.DATE)+1,0,0);
		GregorianCalendar d = new GregorianCalendar (dNow.get(Calendar.YEAR),dNow.get(Calendar.MONTH),dNow.get(Calendar.DATE),timeToMute.getCurrentHour(),timeToMute.getCurrentMinute());
		GregorianCalendar d2 = new GregorianCalendar (dNow.get(Calendar.YEAR),dNow.get(Calendar.MONTH),dNow.get(Calendar.DATE),timeToRestore.getCurrentHour(),timeToRestore.getCurrentMinute());

		TimeToMute =d.getTimeInMillis() - dNow.getTimeInMillis(); 
		TimeToRestore =  d2.getTimeInMillis() - dNow.getTimeInMillis(); 
		if (TimeToMute < 0 && TimeToRestore < 0)
		{
			d.add(Calendar.DATE,1);
			TimeToMute = dTomorrow.getTimeInMillis() - dNow.getTimeInMillis() + d.getTimeInMillis()-dTomorrow.getTimeInMillis();
		}
	}
	public void UpdateTimers()
	{
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		if (null!= timer){
			myTimerTask.cancel();
			timer.cancel();
		}
		myTimerTask = new MyTimerTask();
		timer = new Timer();

		if (TimeToMute > 0)
		{
			timer.schedule(myTimerTask,TimeToMute,TimeToMute);
			restore=false;
		}
		else if (TimeToRestore > 0 && TimeToMute < 0)
		{
			timer.schedule(myTimerTask,TimeToRestore,TimeToRestore);
			restore=true;
		}
		else if (TimeToMute < 0 && TimeToRestore < 0)
		{
			timer.schedule(myTimerTask,TimeToMute,TimeToMute);
			restore=false;

			//	22.00   8.00  22.00 8.00
			//What if an idiot wants to do the opposite.
		}

		editor.putInt("MuteHour", timeToMute.getCurrentHour());
		editor.putInt("MuteMinute",timeToMute.getCurrentMinute() );
		editor.putInt("RestoreHour", timeToRestore.getCurrentHour() );
		editor.putInt("RestoreMinute", timeToRestore.getCurrentMinute() );

		editor.commit();

	}

	/** Called when the activity is first created.*/
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Manager = (AudioManager)(this.getBaseContext().getSystemService(Context.AUDIO_SERVICE));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		timeToMute = (TimePicker)findViewById(R.id.MuteTimePicker);
		timeToRestore = (TimePicker)findViewById(R.id.RestoreTimePicker);
		
		TimePicker muteTime = (TimePicker)findViewById(R.id.MuteTimePicker);
		TimePicker restoreTime = (TimePicker)findViewById(R.id.RestoreTimePicker);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		int mutehour = settings.getInt("MuteHour", 22);
		int muteminute = settings.getInt("MuteMinute", 0);
		int restorehour = settings.getInt("RestoreHour", 8);
		int restoreminute = settings.getInt("RestoreMinute", 0);


		//read from file of what settings to use....
		muteTime.setCurrentHour(mutehour);
		restoreTime.setCurrentHour(restorehour);
		muteTime.setCurrentMinute(muteminute);
		restoreTime.setCurrentMinute(restoreminute);

		muteTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				UpdateTimers();
			}
		});
		restoreTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				UpdateTimers();
			}
		});
		UpdateTimers();




		/*
        timer = new CountDownTimer(10000, 10000) {

   	     public void onTick(long millisUntilFinished) {
   	         int i=0;
   	         i++;
   	         i--;
   	     }

   	     public void onFinish() {
   	    	 if(restore)
   	    	 {
   	    	Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, NotificationVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		Manager.setStreamVolume(AudioManager.STREAM_RING, RingVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		Manager.setStreamVolume(AudioManager.STREAM_SYSTEM, SystemVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		restore = false;
   	 		this.start();
   	    	 }
   	    	 else
   	    	 {
   	    		SystemVolume = Manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
   	 		RingVolume = Manager.getStreamVolume(AudioManager.STREAM_RING);
   	 		NotificationVolume = Manager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
   	 		Manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION|AudioManager.STREAM_RING|AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE|AudioManager.FLAG_SHOW_UI);
   	 		restore = true; 
   	 		this.start();
   	    	 }
   	     }
   	  }.start();
		 */
		/* 
        try {

            // setup and start MyService
            {
             s = new MyService();
              s.SetVolumeAdjuster(this);
              Intent svc = new Intent(this, MyService.class);
              //svc.setComponent("MyService");
              ComponentName test = startService(svc);
              Class<MyService> c = svc.getClass();
              Method[] a = c.getMethods();
              int a=0;
              a++;
            }

          }
          catch (Exception e) {
            int l=0;
            l++;
          }
		 */

	}
}






