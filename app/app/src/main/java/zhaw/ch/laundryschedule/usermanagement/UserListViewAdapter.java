package zhaw.ch.laundryschedule.usermanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.models.User;

public class UserListViewAdapter extends BaseAdapter{

    protected List<User> userList;
    protected Context context;

    public UserListViewAdapter(List<User> _userList, Context _context){
        userList = _userList;
        context = _context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return userList.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if(vi == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.cell_user_list, null);
        }

        TextView firstname = vi.findViewById(R.id.firstname);
        TextView lastname = vi.findViewById(R.id.lastname);
        TextView userName = vi.findViewById(R.id.userName);

        User user = userList.get(i);

        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        userName.setText(user.getUserName());

        return vi;
    }

}
