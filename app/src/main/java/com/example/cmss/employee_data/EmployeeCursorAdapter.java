
package com.example.cmss.employee_data;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.employee.R;
import com.example.cmss.employee_data.data.EmployeeContract;


public class EmployeeCursorAdapter extends CursorAdapter {

    public EmployeeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME);
        int emailColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL);

        String employeeName = cursor.getString(nameColumnIndex);
        String employeeemail= cursor.getString(emailColumnIndex);

        if (TextUtils.isEmpty(employeeemail)) {
            employeeemail = context.getString(R.string.email);
        }

        nameTextView.setText(employeeName);
        summaryTextView.setText(employeeemail);
    }
}