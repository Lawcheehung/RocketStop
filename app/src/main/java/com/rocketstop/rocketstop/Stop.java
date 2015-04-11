package com.rocketstop.rocketstop;

//The user's stopping destination.
//consist of the stop tag number, title, lat, long, stop id.
public class Stop
{
    public final String stopRouteNumber;
    public final String stopRouteName;
    public final String stopLat;
    public final String stopLong;
    public final String stopID;

    //constructor
    public Stop(String num, String name, String lat, String lon, String id)
    {
        stopRouteNumber = num;
        stopRouteName = name;
        stopLat = lat;
        stopLong = lon;
        stopID = id;
    }


}
