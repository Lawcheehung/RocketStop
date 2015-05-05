package com.rocketstop.rocketstop;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.*;
import android.graphics.Path;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


//http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=ttc
//http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=5

/*
Authority: TTC
Route:
Direction:
Stop:
Time Remaining:
*/

public class MainActivity extends Activity
{

    Spinner spinner;
    ArrayAdapter<String> adapter;
    List<String> routeNames = new ArrayList<>();
    List<XmlParserRouteList.Route> routesList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinnerRoute);

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
              /*       XmlParserRouteConfig abc = new XmlParserRouteConfig();

                    InputStream input;
                    try
                    {
                        URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=5");
                        URLConnection urlConnection = url.openConnection();
                        input = new BufferedInputStream(urlConnection.getInputStream());
                        List<Directions> DirectionsList;
                        DirectionsList = abc.routeParser(input);


                        //print out the directions list
                        for (int i = 0; i < DirectionsList.size(); i++)
                        { System.out.println("////////////////////////////////////////////////////////////////////////////////////");
                            Directions value = DirectionsList.get(i);
                            System.out.println("Direction: " + value.directionTag+ " " + value.name+ " " + value.title
                                    + " " + value.name
                                    + " " + value.useForUI
                                    + " " + value.branch);

                            for (int j = 0; j < value.dStops.size(); j++)
                            {
                                Stop oneStop = value.dStops.get(j);
                                System.out.println("Stop Location: " + oneStop.stopRouteNumber + " " + oneStop.stopRouteName
                                        + " " + oneStop.stopLat + " " + oneStop.stopLong + " " + oneStop.stopID);
                            }
                        }
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
*/
                    XmlParserRouteList abc = new XmlParserRouteList();

                    InputStream input;
                    try
                    {
                        URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=ttc");
                        URLConnection urlConnection = url.openConnection();
                        input = new BufferedInputStream(urlConnection.getInputStream());


                        routesList = abc.routeParser(input);

                        //print out the route list
                        for (int i = 0; i < routesList.size(); i++)
                        {
                            XmlParserRouteList.Route value = routesList.get(i);
                            System.out.println("Route: " + value.routeNumber + " " + value.routeName);
                        }

                        //create an array list with only route names.
                        for (int i = 0; i < routesList.size(); i++)
                        {
                            String name = routesList.get(i).routeName;
                            routeNames.add(name);
                        }

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


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();



        //Set up adapter.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                System.out.println(parent.getItemAtPosition(position) + " selected!!!!!!!!!!!!!!!!!!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
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