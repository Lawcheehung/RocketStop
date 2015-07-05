package com.rocketstop.rocketstop;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class StedmanSplashPage extends Activity
{
    public static List<RouteInfo> routeConfig = new ArrayList<>();
    public static List<String> routeNames = new ArrayList<>();
    TextView textView_percent;
    int counter = 0;
    InputStream in;
    List<RouteInfo> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        textView_percent = (TextView) findViewById(R.id.textView_percent);

        if (DetectConnection.checkInternetConnection(this))
        {
            new MyTask().execute();
        }
        else
        {
            Toast.makeText(getBaseContext(), "YOU DO NOT HAVE INTERNET CONNECTION!!!", Toast.LENGTH_LONG).show();
        }
    }


    class MyTask extends AsyncTask<Void, String, Void>
    {

        //runs on the main thread
        @Override
        protected void onPreExecute() //[1]
        {
            //assign mainList to adapter. Both are empty list...
            // adapter = (ArrayAdapter<String>) mainList.getAdapter();


        }

        //Runs on the background thread
        @Override
        protected Void doInBackground(Void... params)     //[2]
        {


                   /////////////////////////////////////////////////////////////////////////////////////////////////





return null;

        }

        //update widget in this method
        //String... values is an array of String objects
        @Override
        protected void onProgressUpdate(String... values)   //[4]
        {
            //update progress bar

            //adapter.add(values[0]);
        }

        //All methods below runs on the main thread
        @Override
        protected void onPostExecute(Void result)
        {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //This portion of code exits the app when the user press on "back" twice. Cool!!
    private static long back_pressed;

    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Press back again to exit!!!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class XmlParserRouteList2
    {



        //Return a List containing our Route objects
        public List<RouteInfo> routeParser(InputStream in) throws XmlPullParserException, IOException
        {

            return list;
        }


      //  private List<RouteInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
        {
//            parser.require(XmlPullParser.START_TAG, ns, "body");
//            while (parser.next() != XmlPullParser.END_TAG)
//            {
//                if (parser.getEventType() != XmlPullParser.START_TAG)
//                {
//                    continue;
//                }
//                String name = parser.getName();
//                // Starts by looking for the route tag
//                if (name.equals("route"))
//                {
//                    counter++;
//
//                    // route = readRoute(parser);
//                    String routeTag = parser.getAttributeValue(null, "tag");/////////////////////////////////
//                    XmlParserRouteConfig abc = new XmlParserRouteConfig();
//                    InputStream input;
//                    try
//                    {
//                        URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=" + routeTag);
//                        URLConnection urlConnection = url.openConnection();
//                        input = new BufferedInputStream(urlConnection.getInputStream());
//                        list.add(abc.routeParser(input));
//                    }
//                    catch (IOException e1)
//                    {
//                        System.out.println("The URL is not valid2.");
//                        System.out.println(e1.getMessage());
//                    }
//                    catch (XmlPullParserException e)
//                    {
//                        System.out.println("xml error2");
//                    }
//                    parser.nextTag();
//                }
//                else
//                {
//                    skip(parser);
//                }
//            }
            //return list;
        }


        //Skip tags we don't care about
        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
        {

        }
    }
}
