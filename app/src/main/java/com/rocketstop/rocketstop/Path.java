package com.rocketstop.rocketstop;

//The coordinates of the destination
public class Path
{

    public final String stopLong;
    public final String stopID;

    //constructor
    public Path(String lon, String id)
    {
        stopLong = lon;
        stopID = id;
    }
}
