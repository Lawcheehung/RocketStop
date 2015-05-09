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


    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Return a List containing our Route objects
    public Long routeParser(InputStream in) throws XmlPullParserException, IOException
    {
        try
        {
            System.out.println("ONE");
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

    boolean out = false;

    //Adapted from http://developer.android.com/training/basics/network-ops/xml.html
    //Processes the feed
    private Long readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        boolean flag = false;
        Long prediction = null;
        parser.require(XmlPullParser.START_TAG, ns, "body");
        while (parser.getEventType() != XmlResourceParser.END_DOCUMENT && (flag == false))
        {
            String name = parser.getName();

            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            // Starts by looking for the route tag
            System.out.println("name!!!: " + name);

            if (parser.getEventType() == XmlResourceParser.START_TAG)        //starting tag
            {

                if (name.equals("prediction ") )
                {
                    flag = true;

                    prediction = readEpochTime(parser);
                    System.out.println("PREDICTION TIME: " + prediction);
                }
            }
            parser.nextTag();
        }

        if (flag == false)
        {
            prediction = (long) -1;
        }
        return prediction;
    }


    // Processes the route tag for "tag" and "title"
    private long readEpochTime(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, ns, "prediction");

        Long epochTime = Long.parseLong(parser.getAttributeValue(null, "epochTime"));
        System.out.println("epochTime:  " + epochTime);
        //parser.nextTag();

        return epochTime;
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
