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

    //A Route object consists of a routeNumber & routeName, i.e. routeNumber = 5, routeName = 5-Avenue Rd
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
    public List<Route> routeParser(InputStream in) throws XmlPullParserException, IOException
    {
        //Under construction...
        List<Route> routes = new ArrayList<Route>();

        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Processed the feed
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                //entries.add(readEntry(parser));
            } else {
                //skip(parser);
            }
        }
        return entries;
    }
}
