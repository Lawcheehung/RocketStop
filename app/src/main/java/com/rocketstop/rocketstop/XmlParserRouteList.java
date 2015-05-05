package com.rocketstop.rocketstop;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * XML Parser for Route List.
 */
public class XmlParserRouteList
{

    List<RouteInfo> list = new ArrayList<>();


    private static final String ns = null; //No namespaces
    private int indexNum = 0;  //the index number of the route list

    //A Route object consists of a routeNumber & routeName
    //i.e. routeNumber = 5, routeName = 5-Avenue Rd
//    public static class Route
//    {
//        public final String routeNumber;
//        public final String routeName;
//        public int num;
//
//        public Route(String routeNumber, String routeName, int num)
//        {
//            this.routeNumber = routeNumber;
//            this.routeName = routeName;
//            this.num = num;
//        }
//    }

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Return a List containing our Route objects
    public List<RouteInfo> routeParser(InputStream in) throws XmlPullParserException, IOException
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
    private List<RouteInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
       // List<Route> routes = new ArrayList<>();
        String tag = null;
        String title = null;
        int num = 0;
      //  Route route = new Route(tag, title, num);


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
               // route = readRoute(parser);
                String routeTag = parser.getAttributeValue(null, "tag");/////////////////////////////////

                //---------------------------------------
                //create url

                XmlParserRouteConfig abc = new XmlParserRouteConfig();

                InputStream input;
                try
                {
                    URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=" + routeTag);
                    URLConnection urlConnection = url.openConnection();
                    input = new BufferedInputStream(urlConnection.getInputStream());
                    list.add(abc.routeParser(input));
                }
                catch (IOException e1)
                {
                    System.out.println("The URL is not valid.");
                    System.out.println(e1.getMessage());
                }
                catch (XmlPullParserException e)
                {
                    System.out.println("xml error");
                }

                //---------------------------------------
                parser.nextTag();

            }
            else
            {
                skip(parser);
            }

            //routes.add(route);


        }

        for (int i = 0; i < list.size(); i++)
        {
            RouteInfo value = list.get(i);
            System.out.println("Stop Location:::: " + value.routeTitle);
        }

        return list;
    }


    // Processes the route tag for "tag" and "title"
//    private Route readRoute(XmlPullParser parser) throws IOException, XmlPullParserException
//    {
//        parser.require(XmlPullParser.START_TAG, ns, "route");
//
//        String routeTag = parser.getAttributeValue(null, "tag");
//        String routeTitle = parser.getAttributeValue(null, "title");
//        parser.nextTag();
//
//        Route route = new Route(routeTag, routeTitle, this.indexNum);
//        this.indexNum++;
//        return route;
//    }

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
