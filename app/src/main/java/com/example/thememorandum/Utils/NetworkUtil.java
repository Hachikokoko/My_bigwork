package com.example.thememorandum.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/*判断网络是否可用工具类*/
public class NetworkUtil
{
    public static boolean isNetworkAbailable(Context context)
    {
        ConnectivityManager manager= (ConnectivityManager) context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        if(manager==null)
        {
            return false;
        }
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isAvailable())
        {
            return false;
        }
        return true;
    }

}
