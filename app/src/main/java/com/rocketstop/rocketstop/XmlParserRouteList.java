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

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Return a List containing our Route objects
    public List<Route> routeParser(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);    //Use this call to change the general behaviour of the parser, such as namespace processing or doctype declaration handling.
            parser.setInput(in, null);   //Sets the input stream the parser is going to process.
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
        List<Route> routes = new ArrayList<>();
        String tag = null;
        String title = null;
        Route route = new Route(tag, title);


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
            {
                route = readRoute(parser);
                //skip(parser);
            }
            else
            {
                skip(parser);
            }

            routes.add(route);
        }
        return routes;
    }


    // Processes the route tag for "tag" and "title"
    private Route readRoute(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "route");
        //String relType = parser.getAttributeValue(null, "tag");
        String routeTag = parser.getAttributeValue(null, "tag");
        String routeTitle = parser.getAttributeValue(null, "title");
        parser.nextTag();
        //routeTitle = parser.getText();
        Route route = new Route(routeTag, routeTitle);
        return route;
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
