package com.AppValley.TaskMet.PostTask;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.Calendar;

public class DialogFragment extends androidx.fragment.app.DialogFragment implements DatePickerDialog.OnDateSetListener {
    String dateText;

    @NonNull
    @Override
    public DatePickerDialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        dateText = DateFormat.getDateInstance().format(c.getTime());
    }
}
