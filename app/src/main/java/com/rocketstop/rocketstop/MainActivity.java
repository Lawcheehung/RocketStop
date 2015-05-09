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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//http://www.nextbus.com/xmlFeedDocs/NextBusXMLFeed.pdf

//Route list:
// http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=ttc

//Route config:
//http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=1S

//Predictions:
//http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=ttc&r=1S&s=9590


/*
Authority: TTC
Route:
Direction:
Stop:
Time Remaining:
*/

public class MainActivity extends Activity
{
    Spinner spinnerRoutes;
    Spinner spinnerDirections;
    Spinner spinnerStops;
    String routeSelected;
    TextView textViewTime;
    int routeSelectedPosition;
    String directionSelected;
    int directionSelectedPosition;
    String stopSelected;
    int stopSelectedPosition;

    ArrayAdapter adapterDirections;
    ArrayAdapter adapterRoutes;
    ArrayAdapter adapterStops;

    List<String> routeNames = new ArrayList<>();
    boolean done = false;
    List<RouteInfo> routeConfig = new ArrayList<>();

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

                        routeConfig = abc.routeParser(input);

                        //print out the route list
                        for (int i = 0; i < routeConfig.size(); i++)
                        {
                            String value = routeConfig.get(i).routeTitle;
                            //System.out.println(value);
                            routeNames.add(value);
                        }

                        done = true;
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


//Use this while loop for now. I need to learn about Asynctask.
        thread.start();
        while (done == false)
        {
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        spinnerRoutes = (Spinner) findViewById(R.id.spinnerRoutes);
        spinnerDirections = (Spinner) findViewById(R.id.spinnerDirections);
        spinnerStops = (Spinner) findViewById(R.id.spinnerStops);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        //Set up adapter.
        adapterRoutes = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routeNames);
        adapterRoutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoutes.setAdapter(adapterRoutes);
        spinnerRoutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                routeSelected = parent.getItemAtPosition(position).toString();
                routeSelectedPosition = position;
                updateDirection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }


    public void updateDirection()
    {
        //get the list of directions for the selected route
        List directionNames = routeConfig.get(routeSelectedPosition).listOfDirections;

        //Set up adapter.
        adapterDirections = new ArrayAdapter(this, android.R.layout.simple_list_item_1, directionNames);
        adapterDirections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirections.setAdapter(adapterDirections);
        spinnerDirections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                directionSelected = parent.getItemAtPosition(position).toString();
                directionSelectedPosition = position;
                updateStop();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }


    public void updateStop()
    {
        List stopNames = routeConfig.get(routeSelectedPosition).listOfDirections.get(directionSelectedPosition).dStops;

        //Set up adapter.
        adapterStops = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stopNames);
        adapterStops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStops.setAdapter(adapterStops);
        spinnerStops.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                stopSelected = parent.getItemAtPosition(position).toString();
                stopSelectedPosition = position;
                updateTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    long diffMinutes;
    long diffHours;
    long diffSeconds;
    long diffDays;
    boolean done2 = false;
    boolean no = false;

    public void updateTime()
    {
        final String routeTagg = routeConfig.get(routeSelectedPosition).routeTag;
        final String stopTagNumber = routeConfig.get(routeSelectedPosition).listOfDirections.get(directionSelectedPosition).dStops.get(stopSelectedPosition).stopRouteNumber;


        Thread thread2 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    XmlParserPrediction abc = new XmlParserPrediction();

                    InputStream input;
                    try
                    {

                        URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=ttc&r=" + routeTagg + "&s=" + stopTagNumber);
                        URLConnection urlConnection = url.openConnection();
                        input = new BufferedInputStream(urlConnection.getInputStream());


                        Long time = abc.routeParser(input);


                        if (time > -1)
                        {
                            System.out.println("time!!!!!!!!!!!!!!!!! " + time);
                            Date currentTime = new Date();
                            Date predictionTime = new Date(time * 1000);

                            long diff = predictionTime.getTime() - currentTime.getTime();

                            diffSeconds = diff / 1000 % 60;
                            diffMinutes = diff / (60 * 1000) % 60;
                            diffHours = diff / (60 * 60 * 1000) % 24;
                            diffDays = diff / (24 * 60 * 60 * 1000);
                            System.out.println(diffHours + "hours, " + diffMinutes + "minutes, " + diffSeconds + "seconds.");
                            no = false;

                        }
                        else
                        {
                            no = true;
                        }

                        done = true;
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
        thread2.start();
        while (done2 == false)
        {
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        if (no == false)
        {
            textViewTime.setText(diffHours + "hours, " + diffMinutes + "minutes, " + diffSeconds + "seconds.");
        }
        else
        {
            textViewTime.setText("no bus available at this time");
            no=false;
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
}