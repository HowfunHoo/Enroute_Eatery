package com.enroute.enroute.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enroute.enroute.R;
import com.enroute.enroute.model.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Haofan.
 */

public class SpecialOffersAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Restaurant> restaurants;

    public SpecialOffersAdapter(Context context, ArrayList<Restaurant> restaurants) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.model_special_offers, parent,false);
        }

        ImageView iv_rpic = (ImageView)convertView.findViewById(R.id.iv_rpic);
        TextView tv_rname = (TextView)convertView.findViewById(R.id.tv_rname);
        TextView tv_rcuisine = (TextView)convertView.findViewById(R.id.tv_rcuisine);
        ImageView iv_dalcard = (ImageView)convertView.findViewById(R.id.iv_dalcard);
        TextView tv_rdalcard = (TextView)convertView.findViewById(R.id.tv_rdalcard);
        TextView tv_roffer = (TextView)convertView.findViewById(R.id.tv_roffer);

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

        //If the restaurant cannot be paid with Dalcard, make iv_dalcard and tv_rdalcard invisible
        if (!restaurant.getRdalcard()){
            iv_dalcard.setVisibility(View.GONE);
            tv_rdalcard.setVisibility(View.INVISIBLE);
        }

        tv_roffer.setText(restaurant.getRoffer());

        //Click event for each restaurant
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN TIMETABLE
            }
        });

        return convertView;

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

//    public static Bitmap getBitmap(String pic_url) {
//
//        Bitmap map = null;
//
//        try {
//            URL url = new URL(pic_url);
//            URLConnection conn = url.openConnection();
//            conn.connect();
//            InputStream in;
//            in = conn.getInputStream();
//            map = BitmapFactory.decodeStream(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return map;
//    }
}
