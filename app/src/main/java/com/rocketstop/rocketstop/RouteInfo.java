package com.rocketstop.rocketstop;

import java.util.List;

//<route tag="1S" title="1S-Yonge Subway Shuttle" color="ff0000" oppositeColor="ffffff" latMin="43.70442" latMax="43.7808499" lonMin="-79.4152999" lonMax="-79.39797">
public class RouteInfo
{

    String routeTag;
    String routeTitle;
    String routeColor;
    String routeOppositeColor;
    double latMin;
    double latMax;
    double lonMin;
    double lonMax;
    List<Directions> listOfDirections;

    public RouteInfo(String routeTag, String routeTitle, String routeColor, String routeOppositeColor, double latMin, double latMax, double lonMin, double lonMax, List<Directions> listOfDirections)
    {
        this.routeTag = routeTag;
        this.routeTitle = routeTitle;
        this.routeColor = routeColor;
        this.routeOppositeColor = routeOppositeColor;
        this.latMin = latMin;
        this.latMax = latMax;
        this.lonMin = lonMin;
        this.lonMax = lonMax;
        this.listOfDirections = listOfDirections;
    }

}