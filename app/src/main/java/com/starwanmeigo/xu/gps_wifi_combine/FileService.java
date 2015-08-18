package com.starwanmeigo.xu.gps_wifi_combine;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by xu on 18.08.15.
 */
public class FileService {
    private Context context;
    public FileService(Context context){
        super();
        this.context = context;
    }

    public void save(String filename, String content) throws Exception{
        FileOutputStream outStream = context.openFileOutput(filename,Context.MODE_WORLD_WRITEABLE);
        outStream.write(content.getBytes());
        outStream.close();
    }

    public String read(String filename) throws Exception{
        FileInputStream inStream = context.openFileInput(filename);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len= inStream.read(buffer))!=-1){
            outStream.write(buffer,0,len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }
}
