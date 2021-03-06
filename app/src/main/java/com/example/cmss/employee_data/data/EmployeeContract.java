/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cmss.employee_data.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class EmployeeContract {


    public EmployeeContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.employee";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EMPLOYEE = "employee";



    public static final class EmployeeEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EMPLOYEE);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEE;


        public final static String TABLE_NAME = "employees";


        public final static String _ID = BaseColumns._ID;

//        public final static  String COLUMN_EMPLOYEE_PHOTO="photo";

        public final static String COLUMN_EMPLOYEE_NAME = "name";

        public final static String COLUMN_EMPLOYEE_SIRNAME = "sirname";

        public final static String COLUMN_EMPLOYEE_DATE ="date";

        public final static String COLUMN_EMPLOYEE_GENDER = "gender";

        public final static String COLUMN_EMPLOYEE_EMAIL = "email";


        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;


        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }


}
