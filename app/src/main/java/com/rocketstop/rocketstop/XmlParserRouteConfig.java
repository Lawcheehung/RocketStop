package com.rocketstop.rocketstop;

import android.content.res.XmlResourceParser;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * XML Parser for Route List.
 */

////////////
//Read all the stag tags and store them in a stopLocation list.

//


// I didn't get the route info::: <route tag="5" title="5-Avenue Rd" color="ff0000" oppositeColor="ffffff" latMin="43.6564899" latMax="43.7061999" lonMin="-79.40603" lonMax="-79.3867799">
public class XmlParserRouteConfig
{
    private static final String ns = null; //No namespaces

    //-----------------------------
    //routeInfo variables
   RouteInfo routeInfoList ;
    String routeTag;
    String routeTitle;
    String routeColor;
    String routeOppositeColor;
    double latMin;
    double latMax;
    double lonMin;
    double lonMax;
    //-----------------------------
    //Stop variables
    List<Stop> stoplocation = new ArrayList<>();
    String stopTag;
    String title;
    double lat;
    double lon;
    String stopID;
    Boolean getAllStops = false;    //Did we get all the stop locations yet?

    //------------------------
    //Directions variables
    List<Directions> dir = new ArrayList<>();
    String directionTag = null;
    String dTitle = null;
    String dName = null;
    String useForUI = null;
    String branch = null;
    List<Stop> stopList = new ArrayList<>();
    //-----------------------------------------
    List<String> stopTagList = new ArrayList<>();



    public RouteInfo routeParser(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser); //Call readFeed to do processing
        }
        finally
        {
            in.close();
        }
    }

    private RouteInfo readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String name = "ignore points for now!!!";
        parser.require(XmlPullParser.START_TAG, ns, "body");      //exception is thrown if the test fails

        while ((parser.getEventType() != XmlResourceParser.END_DOCUMENT) && (!name.equals("point")))
        {
            name = parser.getName();

            if (parser.getEventType() == XmlResourceParser.START_TAG)        //starting tag
            {
                if (name.equals("route"))
                {
                    //get route information
                    routeInfo(parser);

                    //create the RouteInfo object after we retrieved the list of Directions
                }


                if (name.equals("stop") && getAllStops == false)
                {
                    readStop(parser);

                    Stop s = new Stop(this.stopTag, this.title, this.lat, this.lon, this.stopID);
                    stoplocation.add(s);

                    // System.out.println(this.stopTag + " " + this.title + " " + this.lat + " " + this.lon + " " + this.stopID);
                }

                if (name.equals("direction"))
                {
                    getAllStops = true;      //we read all the stop locations
                    readDirection(parser);
                }
                if (name.equals("stop") && getAllStops == true)
                {
                    String stopTemp = parser.getAttributeValue(null, "tag");
                    this.stopTagList.add(stopTemp);
                }
                else
                {
                    // skip(parser);
                }
            }
            else if (parser.getEventType() == XmlResourceParser.TEXT)
            {

            }
            else if (parser.getEventType() == XmlPullParser.END_TAG)
            {
                if (name.equals("direction"))        //create Direction object
                {
                    boolean found;
                    for (int i = 0; i < stopTagList.size(); i++)
                    {
                        found = false;

                        for (int j = 0; (j < stoplocation.size()) || (found == false); j++)
                        {

                            if (stopTagList.get(i).equals(stoplocation.get(j).stopRouteNumber))
                            {
                                found = true;
                                stopList.add(stoplocation.get(j));
                            }
                        }
                    }

                    Directions d = new Directions(this.directionTag, this.dTitle, this.dName, this.useForUI, this.branch, this.stopList);
                    dir.add(d);



                    /*
                    System.out.println("//////////////////////////////////////////////////////////////////");
                    System.out.println(this.directionTag + " " + this.dTitle + " " + this.dName + " " + this.useForUI + " " + this.branch);
                    for (int i = 0; i < stopList.size(); i++)
                    {
                        Stop value = stopList.get(i);
                        System.out.println("Stop Location: " + value.stopRouteNumber + " " + value.stopRouteName
                                + " " + value.stopLat + " " + value.stopLong + " " + value.stopID);
                    }

                    */
                    //empty out the stopList
                    stopList = new ArrayList<>();
                }
            }
            parser.nextTag();
        }

        //Okay, we got retrieved all the Directions for this route. Create a routeinfo list and store it in the routeinfo list.
        routeInfoList = new RouteInfo(this.routeTag, this.routeTitle, this.routeColor, this.routeOppositeColor, this.latMin, this.latMax, this.lonMin, this.lonMax, this.dir);



//        for (int i = 0; i < routeInfoList.size(); i++)
//        {
//            RouteInfo value = routeInfoList.get(i);
//            System.out.println("Stop Location: " + value.routeTitle);
//        }



        return routeInfoList;
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    // Processes attribute values within the tag.
    private void readStop(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "stop");
        this.stopTag = parser.getAttributeValue(null, "tag");
        this.title = parser.getAttributeValue(null, "title");
        this.lat = Double.parseDouble(parser.getAttributeValue(null, "lat"));
        this.lon = Double.parseDouble(parser.getAttributeValue(null, "lon"));
        this.stopID = parser.getAttributeValue(null, "stopId");
    }

    // Processes attribute values within the tag.
    private void readDirection(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "direction");
        this.directionTag = parser.getAttributeValue(null, "tag");
        this.dTitle = parser.getAttributeValue(null, "title");
        this.dName = parser.getAttributeValue(null, "name");
        this.useForUI = parser.getAttributeValue(null, "useForUI");
        this.branch = parser.getAttributeValue(null, "branch");
    }

    // Processes attribute values within the tag.
    private void routeInfo(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "route");
        this.routeTag = parser.getAttributeValue(null, "tag");
        this.routeTitle = parser.getAttributeValue(null, "title");
        this.routeColor = parser.getAttributeValue(null, "color");
        this.routeOppositeColor = parser.getAttributeValue(null, "oppositeColor");

        this.latMin = Double.parseDouble(parser.getAttributeValue(null, "latMin"));
        this.latMax = Double.parseDouble(parser.getAttributeValue(null, "latMax"));
        this.lonMin = Double.parseDouble(parser.getAttributeValue(null, "lonMin"));
        this.lonMax = Double.parseDouble(parser.getAttributeValue(null, "lonMax"));
    }


    ///////////////////////////////////////////////////////////////////

    // For the tags "tag" and "title", extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    //Skip tags we don't care about
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if (parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0)
        {
            switch (parser.next())
            {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
