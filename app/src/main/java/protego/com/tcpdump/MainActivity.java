package protego.com.tcpdump;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import RootTools.RootTools;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.DialogInterface;


public class MainActivity extends ActionBarActivity implements OnClickListener{

    public static StringBuilder result = new StringBuilder();
   // public static StringBuilder tcpdump= new StringBuilder();
    TextView textView;
    EditText parameters;
    Button startButton,stopButton;
    ActionBar actionBar;
    String m_chosenDir = "";
    boolean m_newFolderEnabled = true;
    int button_running =0;
    int chosen_dir_changed =0;
    RootMethods rootMethods ;
    RootRunnable rootRunnable;
    TCPdump tcpdump;
    TCPdumpHandler tcpDumpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initialize();
       installTcpdumpBinary();
        parameters.setText("-nvv>/mnt/sdcard/chani.pcap");

        //tcpdump.append("/data/data/protego.com.tcpdump/files/tcpdump -nvv >"+m_chosenDir+"/tcpdump.pcap");
        // RootAccess.hasRoot(this);
         //RootAccess.runAsRootUser(tcpdump.toString(), result, 1000);


        tcpdump = new TCPdump();

        // Creating a TCPdump handler for the TCPdump object created after.
        tcpDumpHandler = new TCPdumpHandler(tcpdump, this, this, true);


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
                chosen_dir_changed=1;
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
        rootMethods= new RootMethods();
         rootRunnable = new RootRunnable(this,this,rootMethods);
        parameters = (EditText) findViewById(R.id.editTextView);

    }


    public void installTcpdumpBinary()
    {

        if(RootTools.installBinary(this,R.raw.tcpdump,"tcpdump")==false)
            showAlert(this,"extraction error");
    }







    public  void startTCPdump1()
    {

       // RootAccess.scriptStarted=true;
        if(button_running==0  || chosen_dir_changed==1) {
             if(m_chosenDir.isEmpty()) {
                // tcpdump.setLength(0);
                 //tcpdump.append("/data/data/protego.com.tcpdump/files/tcpdump -nvv >mnt/sdcard/tcpdump.pcap");
                 Toast.makeText(this,"No directory chosen ..File stored in sdcard",Toast.LENGTH_SHORT).show();
             }
            else {
                 //tcpdump.setLength(0);
                 //tcpdump.append("/data/data/protego.com.tcpdump/files/tcpdump -nvv >" + m_chosenDir + "/tcpdump.pcap");
             }
            if (rootRunnable.start(tcpdump.toString())==0){//RootAccess.runAsRootUser(tcpdump.toString(), result, 1000) == 0) {

                showAlert(this, "Result:" + result.toString());
                textView.setText(tcpdump.toString());
            } else {
                showAlert(this, "Sorry!!! ");
                textView.setText(tcpdump.toString());
            }

            button_running=1;
            chosen_dir_changed=0;
        }
        else
        {
            if (rootRunnable.start(tcpdump.toString())==0/*RootAccess.runAsRootUser(tcpdump.toString(), result, 1000) == 0*/) {

                showAlert(this, "Result:" + result.toString());
                textView.setText(tcpdump.toString());
            } else {
                showAlert(this, "Sorry!!! ");
                textView.setText(tcpdump.toString());
            }
        }

    }

    public void stopTCPdump1()
    {
        //rootMethods.stop();

        switch(rootRunnable.stop())
        {
            case 0: showAlert(this, "Everything is fine");
                break;
            case -1: showAlert(this,"I/O exception in root");
                break;
            case -2: showAlert(this, "Interrupted Exception in root");
                break;
            case -3: showAlert(this,"Root status is false");
                break;
            case -4:
                showAlert(this,"process is null in root ");
                break;
            case -5: showAlert(this,"problem in root methods stop");
                break;
            case -6:
                showAlert(this,"problem in rootRunnable stop");
                break;
        }


        //RootAccess.scriptStarted=false;
        //RootAccess.runAsRootUser("killall tcpdump",result,1000);
        showAlert(this, result.toString());

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





    private void startTCPdump() {
        if (tcpDumpHandler.checkNetworkStatus()) {

            switch (tcpDumpHandler.start(parameters.getText().toString())) {
                case 0:
                    Toast.makeText(MainActivity.this, "tcpdump started",
                            Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(MainActivity.this,
                            "tcpdump already started",
                            Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Device not Rooted")
                            .setMessage(
                                    "Device not rooted")
                             .setNeutralButton("OK", null).show();
                    break;
                case -4:
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                            .setMessage("Command error")
                            .setNeutralButton("OK", null).show();
                    break;
                case -5:
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                            .setMessage("outputstream error")
                            .setNeutralButton("OK", null).show();
                    break;
                default:
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                            .setMessage("Unknown error")
                            .setNeutralButton("OK", null).show();
            }
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Network connection error")
                    .setMessage(
                            "Network connection error message")
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    startActivity(new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }).setNegativeButton("NO", null)
                    .show();
        }
    }

    /**
     * Calls TCPdumpHandler to try to stop the packet capture.
     */
    private void stopTCPdump() {
        switch (tcpDumpHandler.stop()) {
            case 0:
                Toast.makeText(MainActivity.this,"tcpdump stopped",
                        Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(MainActivity.this,"tcpdump already stopped",
                        Toast.LENGTH_SHORT).show();
                break;
            case -2:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Device not rooted")
                        .setMessage("Device not rooted")
                        .setNeutralButton("OK", null).show();
                break;
            case -4:
                new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage("Command error")
                        .setNeutralButton("OK", null).show();
            case -5:
                new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage("output stream error")
                        .setNeutralButton("OK", null).show();
                break;
            case -6:
                new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage("close shell error")
                        .setNeutralButton("OK", null).show();
                break;
            case -7:
                new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage("process finish error")
                        .setNeutralButton("OK", null).show();
            default:
                new AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage("unkmown error")
                        .setNeutralButton("OK", null).show();
        }

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
