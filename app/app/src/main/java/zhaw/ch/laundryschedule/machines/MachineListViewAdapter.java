package zhaw.ch.laundryschedule.machines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.models.AbstractMachine;

/**
 * Adapter class for the washing machine list element
 */
public class MachineListViewAdapter extends BaseAdapter {

    protected List<AbstractMachine> machineList;
    protected Context context;

    public MachineListViewAdapter(List<AbstractMachine> machineList, Context context) {
        this.machineList = machineList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return machineList.size();
    }

    @Override
    public Object getItem(int i) {
        return machineList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return machineList.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.cell_machine_list, null);
        }

        TextView name = vi.findViewById(R.id.machineName);
        TextView capacity = vi.findViewById(R.id.machineCapacity);

        AbstractMachine machine = machineList.get(i);

        name.setText(machine.getName());
        capacity.setText(machine.getCapacity());

        return vi;
    }
}