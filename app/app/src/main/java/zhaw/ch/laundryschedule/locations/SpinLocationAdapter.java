package zhaw.ch.laundryschedule.locations;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import zhaw.ch.laundryschedule.models.Location;

/**
 * Adapter class for the location list element
 */
public class SpinLocationAdapter extends ArrayAdapter<Location> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private Location[] values;

    public SpinLocationAdapter(Context context, int textViewResourceId,Location[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public Location getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setPadding(20,20,20,20);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        String lableText = values[position].getStreet() + " " + values[position].getStreetNumber()
                + " - " + values[position].getCity() + " " + values[position].getZipCode();
        label.setText(lableText);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setPadding(20,20,20,20);
        label.setTextSize(18);
        String lableText = values[position].getStreet() + " " + values[position].getStreetNumber()
                + " - " + values[position].getCity() + " " + values[position].getZipCode();
        label.setText(lableText);
        return label;
    }
}
