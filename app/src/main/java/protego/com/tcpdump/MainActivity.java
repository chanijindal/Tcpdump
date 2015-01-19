package protego.com.tcpdump;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import RootTools.RootTools;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnClickListener{

    public static StringBuilder result = new StringBuilder();
    public static StringBuilder tcpdump= new StringBuilder();
    TextView textView;
    Button startButton,stopButton;
    ActionBar actionBar;
    String m_chosenDir = "";
    boolean m_newFolderEnabled = true;
    int button_running =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initialize();
       installTcpdumpBinary();

        //tcpdump.append("/data/data/protego.com.tcpdump/files/tcpdump -nvv >"+m_chosenDir+"/tcpdump.pcap");
         RootAccess.hasRoot(this);
         RootAccess.runAsRootUser(tcpdump.toString(), result, 1000);
                                                       }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_settings:
                Log.e("directory", m_chosenDir );
                break;

            case R.id.select_dir:
                showDirectoryDialog();
                //tcpdump.append("/data/data/protego.com.tcpdump/files/tcpdump -nvv >"+m_chosenDir+"/tcpdump.pcap");
                Log.e("directory", m_chosenDir );
                break;

        }

        return super.onOptionsItemSelected(item);
    }





    public void initialize()
    {
        setContentView(R.layout.activity_main);

        textView= (TextView) findViewById(R.id.textView);
        startButton= (Button) findViewById(R.id.startButton);
        stopButton= (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);


    }


    public void installTcpdumpBinary()
    {

        if(RootTools.installBinary(this,R.raw.tcpdump,"tcpdump")==false)
            showAlert(this,"extraction error");
    }







    public  void startTCPdump()
    {
        if(button_running==0) {

            tcpdump.append("/data/data/protego.com.tcpdump/files/tcpdump -nvv >"+m_chosenDir+"/tcpdump.pcap");

            if (RootAccess.runAsRootUser(tcpdump.toString(), result, 1000) == 0) {

                showAlert(this, "Result:" + result.toString());
                textView.setText(tcpdump.toString());
            } else {
                showAlert(this, "Sorry!!! ");
                textView.setText(tcpdump.toString());
            }

            button_running=1;
        }
        else
        {
            if (RootAccess.runAsRootUser(tcpdump.toString(), result, 1000) == 0) {

                showAlert(this, "Result:" + result.toString());
                textView.setText(tcpdump.toString());
            } else {
                showAlert(this, "Sorry!!! ");
                textView.setText(tcpdump.toString());
            }
        }

    }

    public void stopTCPdump()
    {
        RootAccess.runAsRootUser("killall tcpdump",result,1000);
        showAlert(this,result.toString());

    }




    public void showAlert(Context context, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.show();
    }


    public  void showDirectoryDialog()
    {




        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(MainActivity.this,
                        new DirectoryChooserDialog.ChosenDirectoryListener()
                        {
                            @Override
                            public void onChosenDir(String chosenDir)
                            {
                                m_chosenDir = chosenDir;
                                Toast.makeText(
                                        MainActivity.this, "Chosen directory: " +
                                                chosenDir, Toast.LENGTH_LONG).show();
                            }
                        });
        // Toggle new folder button enabling
        directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
        // Load directory chooser dialog for initial 'm_chosenDir' directory.
        // The registered callback will be called upon final directory selection.
        directoryChooserDialog.chooseDirectory(m_chosenDir);
        m_newFolderEnabled = ! m_newFolderEnabled;


    }




    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
           case R.id.startButton :
            startTCPdump();
            break;

            case R.id.stopButton:
            stopTCPdump();
            break;

        }
    }
}
