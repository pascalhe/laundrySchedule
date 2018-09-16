package zhaw.ch.laundryschedule.reservations;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private EditText textbox = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(textbox == null)
            return;

        String yearString = Integer.toString(year);
        String monthString = Integer.toString(month);
        String dayString = Integer.toString(day);

        if(monthString.length() == 1) monthString = "0" + monthString;
        if(dayString.length() == 1) dayString = "0" + dayString;

        textbox.setText(dayString+"."+monthString+"."+yearString);
    }

    public EditText getTextBox(){
        return textbox;
    }

    public void setEditBox(EditText textbox){
        this.textbox = textbox;
    }
}
