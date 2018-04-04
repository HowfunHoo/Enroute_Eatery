package com.enroute.enroute.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enroute.enroute.MapsActivity;
import com.enroute.enroute.R;
import com.enroute.enroute.model.Restaurant;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RestaurantRecommendAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Restaurant> restaurants;

    public RestaurantRecommendAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.model_restaurant_recommend, parent,false);
        }

        ImageView iv_rpic = (ImageView)convertView.findViewById(R.id.iv_rpic);
        TextView tv_rname = (TextView)convertView.findViewById(R.id.tv_rname);
        TextView tv_rcuisine = (TextView)convertView.findViewById(R.id.tv_rcuisine);
        TextView tv_rrate = (TextView)convertView.findViewById(R.id.tv_rrate);
        TextView tv_raddress = (TextView)convertView.findViewById(R.id.tv_raddress);


        final Restaurant restaurant= (Restaurant) this.getItem(position);

        try{
            if (restaurant.getRpicurl()!=null) {
                iv_rpic.setImageDrawable(getDrawable(restaurant.getRpicurl()));
            }
        }catch (Exception e){
            System.out.println("Exception: catch pic url: " + e.getMessage());
        }

        tv_rname.setText(restaurant.getRname());
        tv_rcuisine.setText(restaurant.getRcuisines());
        tv_rrate.setText(restaurant.getRrate());
        tv_raddress.setText(restaurant.getRaddress());

        //Click event for each restaurant
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Map
                reachMap(restaurant.getRlat(), restaurant.getRlng());

                ///////////
                System.out.println("getRlat(): "+restaurant.getRlng());

                Log.d("clicked_res", String.valueOf(restaurant.getRlat()));
            }
        });

        return convertView;

    }

    /**
     * When a restaurant is clicked, the app will go to Map and show the route to the selected restaurant
     * @param
     */
    public void reachMap(double lat, double lng){

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("ResLat", lat);
        intent.putExtra("ResLng", lng);

        Log.d("ResLat ", "1");

        Log.d("ResLat111 ", String.valueOf(lat));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //go to MapsActivity
        context.startActivity(intent);
    }


    /**
     * NEED CITATION HERE!!!!!!!!!!!!!!!!!!!!
     * Reference: https://blog.csdn.net/sinat_21376777/article/details/75157912
     * @param urlpath
     * @return
     */
    public static Drawable getDrawable(String urlpath){
        Drawable drawable = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            drawable = Drawable.createFromStream(in, "background.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
