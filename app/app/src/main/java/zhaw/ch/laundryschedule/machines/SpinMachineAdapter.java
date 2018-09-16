package zhaw.ch.laundryschedule.machines;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import zhaw.ch.laundryschedule.models.WashingMachine;

public class SpinMachineAdapter extends ArrayAdapter<WashingMachine> {
    private Context context;
    private WashingMachine[] values;

    public SpinMachineAdapter(Context context, int textViewResourceId, WashingMachine[] values){
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public WashingMachine getItem(int position){
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
        String lableText = values[position].getName() + " " + values[position].getCapacity();
        label.setText(lableText);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setPadding(20,20,20,20);
        label.setTextSize(18);
        String lableText = values[position].getName() + " " + values[position].getCapacity();
        label.setText(lableText);
        return label;
    }
}
