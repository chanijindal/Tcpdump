package protego.com.tcpdump;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;

import RootTools.RootTools;

/**
 * Created by chanijindal on 21/01/15.
 */
public class RootRunnable {


   static Handler runnableHandler;

    public static int inputLength;

   private static boolean refreshingActive = false;
   private static RootMethods rootMethods;

 static Context context;
   public static StringBuilder result=new StringBuilder();


    RootRunnable(Context context,StringBuilder result,RootMethods rootMethods)
    {
        this.context=context;
        this.result=result;
        runnableHandler = new Handler();
        this.rootMethods=rootMethods;
    }

    private static Runnable outputText = new Runnable() {
        @Override
        public void run() {

            byte buf[] = new byte[1024];



            try {
                if((rootMethods.dataInputStream.available()>0)==true)
                {
                    rootMethods.getDataInputStream().read(buf,0,1024);
                    result.append(new String(buf));
                }
            } catch (IOException e) {
                stopRefreshing();
                e.printStackTrace();
            }



           runnableHandler.postDelayed(outputText,1000);
           Log.e("testing", "inside runnable");

        }
    };


    public static int start(String script)
    {



        if(RootMethods.start(script)==0)
        {
            startRefreshing();
            return 0;
        }

        return -1;

    }

    public static  int stop()
    {
        if(RootMethods.stop()==0) {
            stopRefreshing();
            Log.e("Stop Value","0");
            return 0;

        }
        return -1;
    }

    private static void startRefreshing()
    {
        if(!refreshingActive)
        {
            runnableHandler.post(outputText);
            refreshingActive= true;
        }

    }

    private static void  stopRefreshing()
    {

        if(refreshingActive)
        {
            runnableHandler.removeCallbacks(outputText);
            refreshingActive=false;
        }

    }


}



