package com.rocketstop.rocketstop;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends Activity
{
    private static final int SPLASH_SHOW_TIME = 5000;
    private static final String MYDEBUG = "MYDEBUG";

    static List<RouteInfo> routeConfig = new ArrayList<>();
    static List<String> routeNames = new ArrayList<>();

    public List<String> getRouteNames()
    {
        return routeNames;
    }

    public List<RouteInfo> getRouteConfig()
    {
        return routeConfig;
    }

    //Test
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new MyTask().execute();

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
//            try
//            {
//                Thread.sleep(SPLASH_SHOW_TIME);
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }

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
                }
                catch (IOException e1)
                {
                    System.out.println("The URL is not valid.1");
                    System.out.println(e1.getMessage());
                }
                catch (XmlPullParserException e)
                {
                    System.out.println("xml error1");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
        protected void onPostExecute(Void result)     //[5]
        {
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            finish();
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

}
