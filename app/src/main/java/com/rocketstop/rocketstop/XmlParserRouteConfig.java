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
public class XmlParserRouteConfig
{
    private static final String ns = null; //No namespaces

    public List<Directions> routeParser(InputStream in) throws XmlPullParserException, IOException
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

    private List<Directions> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
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
        String directionTag=null;
        String dTitle=null;
        String dName=null;
        String useForUI=null;
        String branch=null;


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
                stopTag = readTag(parser);
                title = readTitle(parser);
                lat = readLat(parser);
                lon = readLon(parser);
                stopID = readstopId(parser);
                // skip(parser);       I don't think we need this

                Stop s = new Stop(stopTag, title, lat, lon, stopID);   //create a Stop object
                stoplocation.add(s);     //now, add it to the list
            }

            if (name.equals("direction"))
            {
                getAllStops = true;      //we read all the stop locations
                directionTag = readTag(parser);     //read tag
                dTitle = readTitle(parser);
                dName = readName(parser);
                useForUI = readUseForUI(parser);
                branch = readBranch(parser);
            }

            if (name.equals("direction") && getAllStops == true)
            {
                //
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
    // Processes "tag" tags in the feed.
    private String readTag(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "tag");
        String tag = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "tag");
        return tag;
    }

    // Processes "title" tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes "lat" tags in the feed.
    private String readLat(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "lat");
        String lat = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lat");
        return lat;
    }


    // Processes "lon" tags in the feed.
    private String readLon(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "lon");
        String lon = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lon");
        return lon;
    }


    // Processes "stopId" tags in the feed.
    private String readstopId(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "stopId");
        String stopId = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "stopId");
        return stopId;
    }


    // Processes "name" tags in the feed.
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String stopId = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return stopId;
    }

    // Processes "useForUI" tags in the feed.
    private String readUseForUI(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "useForUI");
        String stopId = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "useForUI");
        return stopId;
    }

    // Processes "branch" tags in the feed.
    private String readBranch(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "branch");
        String stopId = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "branch");
        return stopId;
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
