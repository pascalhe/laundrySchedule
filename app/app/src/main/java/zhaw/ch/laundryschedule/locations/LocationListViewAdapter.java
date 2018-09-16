package zhaw.ch.laundryschedule.locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.models.Location;

public class LocationListViewAdapter extends BaseAdapter {

    private List<Location> locationList;
    protected Context context;

    LocationListViewAdapter(List<Location> locationList, Context context) {
        this.locationList = locationList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return locationList.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            vi = inflater.inflate(R.layout.cell_location_list, null);
        }

        TextView street = vi.findViewById(R.id.street);
        TextView zipCode = vi.findViewById(R.id.zipCode);
        TextView city = vi.findViewById(R.id.city);

        Location location = locationList.get(i);

        street.setText(location.getStreet() + " " + location.getStreetNumber());
        zipCode.setText(Integer.toString(location.getZipCode()));
        city.setText(location.getCity());

        return vi;
    }
}
