package com.rocketstop.rocketstop;

/**
 * XML Parser for Route List.
 */
public class XmlParserRouteList
{
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

    //live 4:16pm
    //test change
}
