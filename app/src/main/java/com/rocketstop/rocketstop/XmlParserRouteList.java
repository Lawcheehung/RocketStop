package com.rocketstop.rocketstop;

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
public class XmlParserRouteList
{
    private static final String ns = null; //No namespaces

    //A Route object consists of a routeNumber & routeName
    //i.e. routeNumber = 5, routeName = 5-Avenue Rd
    public static class Route
    {
        public final String routeNumber;
        public final String routeName;

        public Route(String routeNumber, String routeName)
        {
            this.routeNumber = routeNumber;
            this.routeName = routeName;
        }
    }

    //Return a List containing our Route objects
    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    public List<Route> routeParser(InputStream in) throws XmlPullParserException, IOException
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

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Processes the feed
    private List<Route> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        //Under construction...
        List<Route> routes = new ArrayList<Route>();
        String tag = null;
        String title = null;

        parser.require(XmlPullParser.START_TAG, ns, "body");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the route tag
            if (name.equals("route"))
            {   System.out.println("going to read tag number");
                tag = readTag(parser);
                System.out.println("the tag number is: "+tag);
                System.out.println("going to read title");
                title = readTitle(parser);
                System.out.println("the title is: "+title);
                skip(parser);
            }
            else
            {
                skip(parser);
            }

            Route route = new Route(tag, title);
            routes.add(route);
        }
        return routes;
    }

    // Processes "tag" tags in the feed.
    private String readTag(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "tag");
        System.out.println("I'm going to read text");
        String tag = readText(parser);
        System.out.println("The tag number is!!!!!!!!!:" + tag);
        parser.require(XmlPullParser.END_TAG, ns, "tag");
        return tag;
    }

    // Processes "title" tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String tag = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return tag;
    }
/////////////////////////////////////////////////////////////////////
    // For the tags "tag" and "title", extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    { System.out.println("I'm going to read text");
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
