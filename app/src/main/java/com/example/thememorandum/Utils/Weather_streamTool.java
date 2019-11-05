package com.example.thememorandum.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Weather_streamTool {
    public static String transform(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len = 0;
        byte[] buf = new byte[1024];
        try {
            while((len=in.read(buf))>0){
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toString();
    }
}
