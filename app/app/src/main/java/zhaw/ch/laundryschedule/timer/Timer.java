package zhaw.ch.laundryschedule.timer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.util.FormatUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Timer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Timer extends Fragment {
     private TextView timerTitle;



    public Timer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Timer newInstance() {
        Timer fragment = new Timer();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootV = inflater.inflate(R.layout.fragment_timer, container, false);
        timerTitle = rootV.findViewById(R.id.timer_title);
        timerTitle.setText(
        FormatUtil.colorized(timerTitle.getText().toString(),"R.", Color.RED));

        return rootV;
    }

}
