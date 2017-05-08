
package com.example.cmss.employee_data;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.employee.R;
import com.example.cmss.employee_data.data.EmployeeContract;

import java.util.Calendar;


public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private int mYear, mMonth, mDay;
    private static final int EXISTING_EMPLOYEE_LOADER = 0;
//    private static final int CAMERA_REQUEST = 1888;
    private Uri mCurrentEmpUri;
     ImageView imgview;
    private EditText mNameEditText;
    private EditText mSirnameEditText;
    private TextView mDateEditText;
    private EditText mEmailEditText;
    private Spinner mGenderSpinner;


    private int mGender = EmployeeContract.EmployeeEntry.GENDER_UNKNOWN;

    private boolean mEmployeeHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mEmployeeHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentEmpUri = intent.getData();

        if (mCurrentEmpUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_pet));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_pet));

            getLoaderManager().initLoader(EXISTING_EMPLOYEE_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.Name);
        mSirnameEditText = (EditText) findViewById(R.id.Last_Name);
        mDateEditText = (TextView) findViewById(R.id.date);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
//        imgview = (ImageView) findViewById(R.id.image);



        mNameEditText.setOnTouchListener(mTouchListener);
        mSirnameEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);

        mGenderSpinner.setOnTouchListener(mTouchListener);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mDateEditText) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditorActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    mDateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
//        imgview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//        });
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imgview.setImageBitmap(photo);
//        }

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = EmployeeContract.EmployeeEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = EmployeeContract.EmployeeEntry.GENDER_FEMALE;
                    } else {
                        mGender = EmployeeContract.EmployeeEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = EmployeeContract.EmployeeEntry.GENDER_UNKNOWN;
            }
        });
    }

    private void saveEmployee() {
        String nameString = mNameEditText.getText().toString().trim();
        String sirnameString = mSirnameEditText.getText().toString().trim();
        String dateString = mDateEditText.getText().toString().trim();
        final String emailString = mEmailEditText.getText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        mEmailEditText .addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                if (emailString.matches(emailPattern) && s.length() > 0) {
                    Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                    // or
//            textView.setText("valid email");

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    //or
//                    textView.setText("invalid email");
                }
            }
        });

        if (mCurrentEmpUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(sirnameString) && TextUtils.isEmpty(dateString) &&
                TextUtils.isEmpty(emailString) && mGender == EmployeeContract.EmployeeEntry.GENDER_UNKNOWN) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, nameString);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_SIRNAME, sirnameString);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DATE, dateString);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL, emailString);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER, mGender);

        if (mCurrentEmpUri == null) {
            Uri newUri = getContentResolver().insert(EmployeeContract.EmployeeEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_employee_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_employee_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentEmpUri, values, null, null);

            if (rowsAffected == 0) {
                Log.d("", "saveEmployee: ");
                Toast.makeText(this, getString(R.string.editor_update_employee_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_employee_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentEmpUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (mNameEditText.length() == 0) {
                    mNameEditText.setError("Enter  your Name");
//                } else if (edtadd.length() == 0) {
//                    edtadd.setError("Enter your Address");
//                } else if (edtcname.length() == 0) {
//                    edtcname.setError("Enter Contact Person");
//                } else if (edtcnum.length() == 0 || edtcnum.length() < 10) {
//                    edtcnum.setError("Enter  Valid Contact Number");
//                } else if (spCategory.getSelectedItemPosition() == 0) {
//                    Toast.makeText(getApplicationContext(), "Select Category ", Toast.LENGTH_LONG).show();
//                } else if (spShopType.getSelectedItemPosition() == 0) {
//                    Toast.makeText(getApplicationContext(), "Select ShopType ", Toast.LENGTH_LONG).show();
//                } else if (VDate.length() == 0) {
//                    VDate.setError("EnterDate");
//                } else {
                }else {
                    saveEmployee();
                }
                    finish();
                    return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mEmployeeHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mEmployeeHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_SIRNAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DATE,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL};

        return new CursorLoader(this,   // Parent activity context
                mCurrentEmpUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME);
            int sirnameColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_SIRNAME);
            int dateColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DATE);
            int genderColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER);
            int emailColumnIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL);

            String name = cursor.getString(nameColumnIndex);
            String sirname = cursor.getString(sirnameColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int email = cursor.getInt(emailColumnIndex);

            mNameEditText.setText(name);
            mSirnameEditText.setText(sirname);
            mDateEditText.setText(date);
            mEmailEditText.setText(Integer.toString(email));
//
            switch (gender) {
                case EmployeeContract.EmployeeEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case EmployeeContract.EmployeeEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mSirnameEditText.setText("");
        mDateEditText.setText("");
        mEmailEditText.setText("");
        mGenderSpinner.setSelection(0); // Select "Unknown" gender
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteEmployee();
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

    private void deleteEmployee() {
        if (mCurrentEmpUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentEmpUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_employee_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_employee_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}