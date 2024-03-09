package com.example.statsexpert.navigation

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment(private val listener: DatePickerListener) : DialogFragment() {

    interface DatePickerListener {
        fun onDateSelected(date: Long)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            // Set the calendar to the picked date
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            // Pass the selected date back to the listener
            listener.onDateSelected(c.timeInMillis)
        }, year, month, day)
    }
}
