package com.rocketstop.rocketstop;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Directions
{
    private static final String ns = null; //No namespaces
    public final String directionTag;
    public final String title;
    public final String name;
    public final String useForUI;
    public final String branch;
    List<Stop> stop = new ArrayList<>();

    Directions(String tag, String title, String name, String u, String branch)//(List<Stop> sss)
    {
        this.directionTag = tag;
        this.title = title;
        this.name = name;
        this.useForUI = u;
        this.branch = branch;
        // we also need the list of stops from this direction
    }
}
