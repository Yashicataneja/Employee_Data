package com.example.cmss.employee_data.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class EmployeeProvider extends ContentProvider {


    public static final String LOG_TAG = EmployeeProvider.class.getSimpleName();

    private static final int EMPLOYEE = 100;

    private static final int EMPLOYEE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY, EmployeeContract.PATH_EMPLOYEE,EMPLOYEE);


        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY, EmployeeContract.PATH_EMPLOYEE + "/#", EMPLOYEE_ID);
    }



    private EmployeeDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new EmployeeDbHelper(getContext());

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEE:
                cursor = database.query(EmployeeContract.EmployeeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EMPLOYEE_ID:
                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(EmployeeContract.EmployeeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
        case EMPLOYEE:
                return insertEmployee(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEE:
                return updateEmployee(uri, contentValues, selection, selectionArgs);
            case EMPLOYEE_ID:

                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateEmployee(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateEmployee(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME)) {
            String name = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("employee requires a name");
            }
        }


        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER)) {
            Integer gender = values.getAsInteger(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER);
            if (gender == null || !EmployeeContract.EmployeeEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("employee requires valid gender");
            }
        }


        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL)) {
            String email = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL);
            if (email == null) {
                throw new IllegalArgumentException("employee requires a email");
            }
        }


        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(EmployeeContract.EmployeeEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted;

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEE:
                rowsDeleted = database.delete(EmployeeContract.EmployeeEntry.TABLE_NAME, selection, selectionArgs);

                break;
            case EMPLOYEE_ID:
                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(EmployeeContract.EmployeeEntry.TABLE_NAME, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEE:
                return EmployeeContract.EmployeeEntry.CONTENT_LIST_TYPE;
            case EMPLOYEE_ID:
                return EmployeeContract.EmployeeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    private Uri insertEmployee(Uri uri, ContentValues values) {
        String name = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("employee requires a name");
        }

        Integer gender = values.getAsInteger(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_GENDER);
        if (gender == null || !EmployeeContract.EmployeeEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Employee requires valid gender");
        }
        String email = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL);
        if (email == null) {
            throw new IllegalArgumentException("employee requires a mail id");
        }




        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        long id = database.insert(EmployeeContract.EmployeeEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }


        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


}