package com.rocketstop.rocketstop;

//The user's stopping destination.
//consist of the stop tag number, title, lat, long, stop id.
public class Stop
{
    public final String stopRouteNumber;
    public final String stopRouteName;
    public final double stopLat;
    public final double stopLong;
    public final String stopID;

    //constructor
    public Stop(String num, String name, double lat, double lon, String id)
    {
        stopRouteNumber = num;
        stopRouteName = name;
        stopLat = lat;
        stopLong = lon;
        stopID = id;
    }

    @Override
    public String toString()
    {
        return this.stopRouteName;
    }
}
