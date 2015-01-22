package protego.com.tcpdump;

import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by chanijindal on 19/01/15.
 */
public class Root  {

    public static Process process=null;
    public static DataInputStream dataInputStream=null;
    public static DataOutputStream dataOutputStream=null;
    public static boolean  checkRootAccess;

   public static StringBuilder script = new StringBuilder();
   public static StringBuilder result=new StringBuilder();

    public static boolean processRunning;
    /*public Root(String script ,String result)
    {
        this.script=script;
        this.result=result;
    }*/


    public static void RootStatusDisplay()
    {
       checkRootAccess= checkRoot();

    }


    public static boolean checkRoot()
    {
         Process proc = null;
        try {
            proc=Runtime.getRuntime().exec("su");
            dataOutputStream=new DataOutputStream(proc.getOutputStream());
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            proc.waitFor();
            if(proc.exitValue()!=225) {

                return true;

            }
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;

        }

    }


    public static int startShell()
    {
       if(checkRootAccess)
        try {
            process= Runtime.getRuntime().exec("su");
            dataOutputStream=new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
             return  0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
          return -1;
    }

    public static void stopShell()
    {

        if(checkRootAccess)
        {
            if(process!=null)
            {
                try {
                   dataOutputStream.writeBytes("exit\n");
                    process.wait();
                    process.destroy();
                    process=null;
                    dataOutputStream=null;
                    dataInputStream=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static int runScript(String script)
    {

        if(checkRootAccess )
        {
            try {
                dataOutputStream.writeBytes(script + "\n");
                dataOutputStream.flush();

                return 0;

            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }

        }

        return -1;
    }


    public boolean isProcessRunning()
    {
        if(process!=null)
            return true;
        else
            return false;
    }


    public static DataInputStream getDataInputStream()
    {
        return dataInputStream;
    }

    public static DataOutputStream getDataOutputStream()
    {
        return dataOutputStream;
    }

}
