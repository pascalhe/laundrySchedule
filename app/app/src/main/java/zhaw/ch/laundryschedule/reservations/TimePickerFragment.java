package zhaw.ch.laundryschedule.reservations;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * The class TimePickerFragment creates a time picker dialog for the reservation ui.
 *
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private EditText textbox = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * sets the selected time in the text box
     * @param view
     * @param hourOfDay
     * @param minute
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(textbox == null)
            return;

        String hourString = Integer.toString(hourOfDay);
        String minuteString = Integer.toString(minute);

        if(hourString.length() == 1) hourString = "0" + hourString;
        if(minuteString.length() == 1) minuteString = "0" + minuteString;

        textbox.setText(hourString+":"+minuteString);
    }

    public EditText getTextBox(){
        return textbox;
    }

    public void setEditBox(EditText textbox){
        this.textbox = textbox;
    }
}