package com.rocketstop.rocketstop;

import android.graphics.*;
import android.graphics.Path;
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


// I didn't get the route info::: <route tag="5" title="5-Avenue Rd" color="ff0000" oppositeColor="ffffff" latMin="43.6564899" latMax="43.7061999" lonMin="-79.40603" lonMax="-79.3867799">
public class XmlParserRouteConfig
{
    private static final String ns = null; //No namespaces

    //Stop variables
    List<Stop> stoplocation = new ArrayList<>();
    String stopTag;
    String title;
    String lat;
    String lon;
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

    public List<Directions> routeParser(InputStream in) throws XmlPullParserException, IOException
    {System.out.println("hey");
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

    private List<Directions> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "body");     //starting tag: body
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the route tag
            if (name.equals("stop") && getAllStops == false)
            {
                readStop(parser);
                Stop s = new Stop(this.stopTag, this.title, this.lat, this.lon, this.stopID);
                stoplocation.add(s);

                System.out.println(this.stopTag + "" + this.title + "" + this.lat + "" + this.lon + "" + this.stopID);


            }

            if (name.equals("direction"))
            {
                getAllStops = true;      //we read all the stop locations
                readDirection(parser);
                Directions d = new Directions(this.directionTag, this.dTitle, this.dName, this.useForUI, this.branch);
                dir.add(d);

              //  System.out.println(this.directionTag + "" +  this.dTitle + "" +  this.dName + "" +  this.useForUI + "" +  this.branch);
            }
            if (name.equals("stop") && getAllStops == true)
            {
                //create
                //I'll do this part later.
                Directions d = new Directions(directionTag, dTitle, dName, useForUI, branch);
                dir.add(d);
            }
            else
            {
                skip(parser);
            }

            //  Directions route = new Directions(directionTag, title);
            // d.add(route);
        }
        return dir;
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    // Processes attribute values within the tag.
    private void readStop(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "stop");
        this.stopTag = parser.getAttributeValue(null, "tag");
        this.title = parser.getAttributeValue(null, "title");
        this.lat = parser.getAttributeValue(null, "lat");
        this.lon = parser.getAttributeValue(null, "lon");
        this.stopID = parser.getAttributeValue(null, "stopId");
        parser.nextTag();
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
        parser.nextTag();
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
