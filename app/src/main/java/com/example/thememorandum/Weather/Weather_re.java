package com.example.thememorandum.Weather;

public class Weather_re
{
    private String re_days;
    private int image01;
    private String sun;
    private String min;
    private int image02;
    private String max;
    public Weather_re(String re_days,int image01,String sun,String min,int image02,String max)
    {
        this.re_days=re_days;
        this.image01=image01;
        this.sun=sun;
        this.min=min;
        this.image02=image02;
        this.max=max;
    }
    public String getRe_days()
    {
        return re_days;
    }
    public int getImage01()
    {
        return image01;
    }
    public String getSun()
    {
        return sun;
    }
    public String getmin()
    {
        return min;
    }

    public int getImage02()
    {
        return image02;
    }
    public String getmax()
    {
        return max;
    }

}
