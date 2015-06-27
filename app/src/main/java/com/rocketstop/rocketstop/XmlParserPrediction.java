package com.rocketstop.rocketstop;

import android.content.res.XmlResourceParser;
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
public class XmlParserPrediction
{
    private static final String ns = null; //No namespaces
    public int sec = 0;
    public int min = 0;

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Return a List containing our Route objects
    public void routeParser(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            System.out.println("ONE");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);    //Use this call to change the general behaviour of the parser, such as namespace processing or doctype declaration handling.
            parser.setInput(in, null);   //Sets the input stream the parser is going to process.
            parser.nextTag();

            readFeed(parser);
            //return readFeed(parser); //Call readFeed to do processing
        }
        finally
        {
            in.close();
        }
    }

    boolean out = false;

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Processes the feed
    private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        boolean flag = false;

        //Long prediction = null;
        boolean end = false;
        parser.require(XmlPullParser.START_TAG, ns, "body");
        while ((parser.getEventType() != XmlResourceParser.END_DOCUMENT) && end == false)
        {
            parser.nextTag();
            String name = parser.getName();

            // Starts by looking for the route tag
            System.out.println("name!!!: " + name);

            if (parser.getEventType() == XmlResourceParser.START_TAG)        //starting tag
            {
                System.out.println("START_TAG:::name!!!: " + name);
                if (name.equals("prediction")&& (flag == false))
                {
                    flag = true;

                    //prediction = readEpochTime(parser);

                    sec = Integer.parseInt(parser.getAttributeValue(null, "seconds"));
                    min = Integer.parseInt(parser.getAttributeValue(null, "minutes"));

                    System.out.println("PREDICTION TIME: " + min + ", " + sec);
                }
            }
            else if (parser.getEventType() == XmlResourceParser.TEXT)
            {
                System.out.println("TEXT:::name!!!: " + name);
            }
            else if (parser.getEventType() == XmlPullParser.END_TAG)
            {
                System.out.println("END_TAG:::name!!!: " + name);
                if (name.equals("body"))
                {
                    end = true;
                    System.out.println("END IT, PLEASE");
                }
            }
            System.out.println("help:" + parser.getName());
        }

        if (flag == false)
        {
            sec= -1;
        }

       // return prediction;
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
