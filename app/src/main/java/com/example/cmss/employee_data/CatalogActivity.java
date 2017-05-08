
package com.example.cmss.employee_data;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.employee.R;
import com.example.cmss.employee_data.data.EmployeeContract;


public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EMPLOYEE_LOADER = 0;

    EmployeeCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView EmployeeListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        EmployeeListView.setEmptyView(emptyView);


        mCursorAdapter = new EmployeeCursorAdapter(this, null);
        EmployeeListView.setAdapter(mCursorAdapter);

        EmployeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentEmpUri = ContentUris.withAppendedId(EmployeeContract.EmployeeEntry.CONTENT_URI, id);

                intent.setData(currentEmpUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(EMPLOYEE_LOADER, null, this);
    }

    private void insertEmployee() {
        ContentValues values = new ContentValues();
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Yashica");
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_SIRNAME, "Taneja");
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DATE,"01/11/2016");
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER, EmployeeContract.EmployeeEntry.GENDER_FEMALE);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL, "yashica.taneja@");
        Uri newUri = getContentResolver().insert(EmployeeContract.EmployeeEntry.CONTENT_URI, values);
    }

    private void deleteAllEmployee() {
        int rowsDeleted = getContentResolver().delete(EmployeeContract.EmployeeEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from employee database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertEmployee();
                return true;
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
//                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_SIRNAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DATE,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL };

        return new CursorLoader(this,   // Parent activity context
                EmployeeContract.EmployeeEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllEmployee();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}