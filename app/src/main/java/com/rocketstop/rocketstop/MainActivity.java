package com.rocketstop.rocketstop;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

//jason's changeg
public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    XmlParserRouteList abc = new XmlParserRouteList();


                    InputStream input;
                    try
                    {
                        URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=ttc");
                        URLConnection urlConnection = url.openConnection();
                        input = new BufferedInputStream(urlConnection.getInputStream());
                        List<XmlParserRouteList.Route> routesList;


                        routesList=abc.routeParser(input);


                        //print out the route list
                        for (int i = 0; i < routesList.size(); i++)
                        {
                            XmlParserRouteList.Route value = routesList.get(i);
                            System.out.println("Route: " + value);
                        }

                    }
                    catch (IOException e1)
                    {
                        System.out.println("The URL is not valid.");
                        System.out.println(e1.getMessage());
                    }
                  //      catch (XmlPullParserException e)
                    {
                 //       System.out.println("xml error");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


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
}
