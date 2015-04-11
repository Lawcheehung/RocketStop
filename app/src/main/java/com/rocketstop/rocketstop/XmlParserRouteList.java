package com.rocketstop.rocketstop;

import java.util.ArrayList;
import java.util.List;

/**
 * XML Parser for Route List.
 */
public class XmlParserRouteList
{
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
    public List<Route> routeParser()
    {
        //Under construction...
        List<Route> routes = new ArrayList<Route>();
        return routes;
    }
}
