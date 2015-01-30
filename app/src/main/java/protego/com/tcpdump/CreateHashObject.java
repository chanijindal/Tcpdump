package protego.com.tcpdump;

import android.util.Log;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by chanijindal on 30/01/15.
 */
public class CreateHashObject {






    public static void createObject(String checkBufferValue )
    {

        String[] splited = checkBufferValue.split("\\s+");

        Log.e("LOg", splited[0] + " " + splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8]);
        //String a= splited[4];

        String[] src_ip= splited[4].split(Pattern.quote("."));
        //Log.d("TSG",src_ip[0]);
        String[] dst_ip= splited[5].split(Pattern.quote("."));


        Packet.TIMESTAMP=splited[0];
        Packet.FLAG_IP= splited[1];
        Packet.PROTOCOL=splited[2];
        Packet.DATA_LENGTH=splited[3];
        Packet.SRC_IP=src_ip[0]+"."+src_ip[1]+"."+src_ip[2]+"."+src_ip[3];
        Packet.DEST_IP=dst_ip[0]+"."+dst_ip[1]+"."+dst_ip[2]+"."+dst_ip[3];
        Packet.SRC_PORT=src_ip[4];
        Packet.DEST_PORT=dst_ip[4];
        Packet.FLAG_TCP=splited[6];
        Packet.CHECKSUM=splited[7];
        Packet.DATA_LENGTH=splited[8];

        Log.d("Destination IP",Packet.DEST_IP);


        HashMap<String,String> hash = new HashMap<>();
        hash.put("TIMESTAMP",Packet.TIMESTAMP);
        hash.put("FLAG_IP",Packet.FLAG_IP);
        hash.put("PROTOCOL",Packet.PROTOCOL);
        hash.put("LENGTH",Packet.LENGTH);
        hash.put("SRC_IP",Packet.SRC_IP);
        hash.put("DEST_IP",Packet.DEST_IP);
        hash.put("SRC_PORT",Packet.SRC_PORT);
        hash.put("DEST_PORT",Packet.DEST_PORT);
        hash.put("FLAG_TCP",Packet.FLAG_TCP);
        hash.put("CHECKSUM",Packet.CHECKSUM);
        hash.put("DATA_LENGTH",Packet.DATA_LENGTH);



    }



}
