package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import models.Bills;
import models.Category;
import models.Settings;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class DBcatch extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ETracker.db";
    //---------------------------------------------------------------------------------------
    // table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";
    //---------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------
    // table name
    private static final String TABLE_SETTINGS = "settings";

    // Settings Table Columns names
    private static final String COLUMN_SETTINGS_ID = "settings_id";
    private static final String COLUMN_SETTINGS_USER_ID = "user_id";
    private static final String COLUMN_USER_PIE_CHART = "pie_chart";
    private static final String COLUMN_USER_BAR_CHART = "bar_chart";
    private static final String COLUMN_USER_RADAR_CHART = "radar_chart";

    // create table sql query
    private String CREATE_TABLE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
            + COLUMN_SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SETTINGS_USER_ID + " INTEGER,"
            + COLUMN_USER_PIE_CHART + " BOOLEAN,"
            + COLUMN_USER_RADAR_CHART + " BOOLEAN,"
            + COLUMN_USER_BAR_CHART + " BOOLEAN,"
            + " FOREIGN KEY ("+COLUMN_SETTINGS_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    //---------------------------------------------------------------------------------------
    // table name
    private static final String TABLE_CATEGORIES = "categories";

    // Bills Table Columns names
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_USER_ID = "user_ID";
    private static final String COLUMN_CATEGORY_NAME = "name";

    // create table sql query
    private String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_USER_ID + " TEXT,"
            + COLUMN_CATEGORY_NAME + " TEXT ,"
            + " FOREIGN KEY ("+COLUMN_CATEGORY_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    //---------------------------------------------------------------------------------------
    private static final String TABLE_BILLS = "bills";

    // Bills Table Columns names
    private static final String COLUMN_BILL_ID = "bill_id";
    private static final String COLUMN_BILL_USER_ID = "user_id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE_STRING = "dateString";
    private static final String COLUMN_COMPANY_NAME = "company_name";
    private static final String COLUMN_CATEGORY = "category";

    private String CREATE_BILLS_TABLE = "CREATE TABLE " + TABLE_BILLS + "("
            + COLUMN_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BILL_USER_ID + " INTEGER,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_AMOUNT + " FLOAT,"
            + COLUMN_DATE_STRING + " TEXT,"
            + COLUMN_COMPANY_NAME + " TEXT,"
            + COLUMN_CATEGORY + " TEXT,"
            + " FOREIGN KEY ("+COLUMN_BILL_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    //---------------------------------------------------------------------------------------
    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_BILLS_TABLE = "DROP TABLE IF EXISTS " + TABLE_BILLS;
    private String DROP_CATEGORIES_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
    private String DROP_TABLE_SETTINGS_TABLE = "DROP TABLE IF EXISTS " + TABLE_SETTINGS;
    /**
     * Constructor
     *
     * @param context
     */
    public DBcatch(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BILLS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_TABLE_SETTINGS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_BILLS_TABLE);
        db.execSQL(DROP_CATEGORIES_TABLE);
        db.execSQL(DROP_TABLE_SETTINGS_TABLE);
        // Create tables again
        onCreate(db);

    }
    //---------------------------------------------------------------------------------------
    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public User getUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_USER_ID,
                        COLUMN_USER_NAME, COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD }, COLUMN_USER_EMAIL + "=?",
                new String[] { String.valueOf(email) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
        user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        return user;
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public void updateUserPassword(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public ArrayList<User> getUserByID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // sorting orders
        ArrayList<User> categoryUser = new ArrayList<User>();

        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_USER_ID,
                        COLUMN_USER_NAME, COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD }, COLUMN_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding record to list
                categoryUser.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return categoryUser;
    }
    //---------------------------------------------------------------------------------------
    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_USER_ID, category.getUserID());
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        // Inserting Row
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public List<Category> getCategoriesByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // sorting orders
        String sortOrder =
                COLUMN_CATEGORY_ID;
        List<Category> categoryList = new ArrayList<Category>();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_CATEGORY_ID,
                        COLUMN_CATEGORY_USER_ID, COLUMN_CATEGORY_NAME }, COLUMN_CATEGORY_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, sortOrder, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_USER_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // Adding record to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return categoryList;
    }
    public List<Category> getCategsadoriesByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // sorting orders
        String sortOrder =
                COLUMN_CATEGORY_ID;
        List<Category> categoryList = new ArrayList<Category>();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_CATEGORY_ID,
                        COLUMN_CATEGORY_USER_ID, COLUMN_CATEGORY_NAME }, COLUMN_CATEGORY_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, sortOrder, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_USER_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // Adding record to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return categoryList;
    }
    public List<Category> getAllCategories() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_USER_ID,
                COLUMN_CATEGORY_NAME,
        };
        // sorting orders
        String sortOrder =
                COLUMN_CATEGORY_ID;
        List<Category> categoryList = new ArrayList<Category>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT columns FROM category ORDER BY id;
         */
        Cursor cursor = db.query(TABLE_CATEGORIES, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_USER_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // Adding record to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return categoryList;
    }

    public void deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete category record by id
        db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + "=\"" + categoryId+"\"", null);
        db.close();
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());

        // updating row
        db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public boolean checkCategory(String category, String user_id) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_CATEGORY_NAME + " = ?" + " AND " + COLUMN_CATEGORY_USER_ID + " = ?";
        // selection argument
        String[] selectionArgs = {category,user_id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT id FROM category WHERE name ="String category";
         */
        Cursor cursor = db.query(TABLE_CATEGORIES, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    //---------------------------------------------------------------------------------------
    public void addBill(Bills bill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_USER_ID, bill.getUserID());
        values.put(COLUMN_DESCRIPTION, bill.getDescription());
        values.put(COLUMN_AMOUNT, bill.getAmount());
        values.put(COLUMN_DATE_STRING, bill.getDateString());
        values.put(COLUMN_COMPANY_NAME, bill.getCompany_name());
        values.put(COLUMN_CATEGORY, bill.getCategory());

        // Inserting Row
        db.insert(TABLE_BILLS, null, values);
        db.close();
    }
    public List<Bills> getBillsByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // sorting orders
        List<Bills> billsList = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, new String[] { COLUMN_BILL_ID,
                        COLUMN_BILL_USER_ID, COLUMN_DESCRIPTION, COLUMN_AMOUNT, COLUMN_DATE_STRING, COLUMN_COMPANY_NAME, COLUMN_CATEGORY}, COLUMN_CATEGORY_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, COLUMN_BILL_ID, null);

        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_ID))));
                bills.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_USER_ID))));
                bills.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                bills.setAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_STRING)));
                bills.setCompany_name(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));
                bills.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                // Adding record to list
                billsList.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return billsList;
    }
    public ArrayList<Bills> getMonthDateByUserID(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tmpcol_monthly_total = "Monthly_Total";
        String tmpcol_month_year = "Month_and_Year";
        String tmpcol_year_total = "Year Total";
        String[] columns = new String[]{
                "sum(" + COLUMN_AMOUNT + ") AS " + tmpcol_monthly_total,
                "substr(" + COLUMN_DATE_STRING + ",4) AS " + tmpcol_month_year
        };
        String whereclause = COLUMN_BILL_USER_ID + "=?";
        String[] whereargs = new String[]{String.valueOf(userID)};
        String groupbyclause = "substr(" + COLUMN_DATE_STRING + ",4)";
        String orderbyclause = "substr(" + COLUMN_DATE_STRING + ",7,2)||substr(" + COLUMN_DATE_STRING + ",4,2)";
        ArrayList<Bills> listBillsDates = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, columns, whereclause,
                whereargs, groupbyclause, null, orderbyclause, null);
        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setAmount(cursor.getInt(cursor.getColumnIndex(tmpcol_monthly_total)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(tmpcol_month_year))); //<<<<<<<<<< NOTE data is MM/YY (otherwise which date to use? considering result will be arbrirtaryy)
                // Adding record to list
                listBillsDates.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return listBillsDates;
    }
    public ArrayList<Bills> getBillByID(int bill_id){
        SQLiteDatabase db = this.getReadableDatabase();
        // sorting orders
        ArrayList<Bills> billsList = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, new String[] { COLUMN_BILL_ID,
                        COLUMN_BILL_USER_ID, COLUMN_DESCRIPTION, COLUMN_AMOUNT, COLUMN_DATE_STRING, COLUMN_COMPANY_NAME, COLUMN_CATEGORY}, COLUMN_BILL_ID + "=?",
                new String[] { String.valueOf(bill_id) }, null, null, COLUMN_BILL_ID, null);

        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_ID))));
                bills.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_USER_ID))));
                bills.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                bills.setAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_STRING)));
                bills.setCompany_name(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));
                bills.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                // Adding record to list
                billsList.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return billsList;
    }
    public ArrayList<Bills> getYearDateByUserID(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tmpcol_year_total = "Year_Total";
        String tmpcol_month_year = "Month_and_Year";
        String[] columns = new String[]{
                "sum(" + COLUMN_AMOUNT + ") AS " + tmpcol_year_total,
                "substr(" + COLUMN_DATE_STRING + ",7) AS " + tmpcol_month_year
        };
        String whereclause = COLUMN_BILL_USER_ID + "=?";
        String[] whereargs = new String[]{String.valueOf(userID)};
        /**
         substr(X,Y,Z);  X beginning with the Y-th. The left-most character of X is number 1.
         If Y is negative then the first character of the substring is found by counting
         from the right rather than the left. If Z is negative then the abs(Z) characters
         preceding the Y-th character are returned.*/
        String groupbyclause = "substr(" + COLUMN_DATE_STRING + ",7)";
        String orderbyclause = "substr(" + COLUMN_DATE_STRING + ",7,2)";

        ArrayList<Bills> listBillsDates = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, columns, whereclause,
                whereargs, groupbyclause, null, orderbyclause, null);
        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setAmount(cursor.getInt(cursor.getColumnIndex(tmpcol_year_total)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(tmpcol_month_year))); //<<<<<<<<<< NOTE data is MM/YY (considering result will be arbrirtaryy)
                // Adding record to list
                listBillsDates.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return listBillsDates;
    }
    public List<Bills> getAllBills() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_BILL_ID,
                COLUMN_BILL_USER_ID,
                COLUMN_DESCRIPTION,
                COLUMN_AMOUNT,
                COLUMN_DATE_STRING,
                COLUMN_COMPANY_NAME,
                COLUMN_CATEGORY
        };
        // sorting orders
        String sortOrder =
                COLUMN_BILL_ID;
        List<Bills> billsList = new ArrayList<Bills>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT columns FROM bills ORDER BY bill_id;
         */
        Cursor cursor = db.query(TABLE_BILLS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bills bill = new Bills();
                bill.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_ID))));
                bill.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_USER_ID))));
                bill.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                bill.setAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)));
                bill.setDateString(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_STRING)));
                bill.setCompany_name(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));
                bill.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                // Adding record to list
                billsList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return bills list
        return billsList;
    }

    public void deleteBill(int billId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILLS, COLUMN_BILL_ID + "=\"" + billId+"\"", null);
        db.close();
    }

    public void updateBill(Bills bills) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_USER_ID, String.valueOf(bills.getUserID()));
        values.put(COLUMN_DESCRIPTION, bills.getDescription());
        values.put(COLUMN_AMOUNT, String.valueOf((bills.getAmount())));
        values.put(COLUMN_DATE_STRING, bills.getDateString());
        values.put(COLUMN_COMPANY_NAME, bills.getCompany_name());
        values.put(COLUMN_CATEGORY, bills.getCategory());

        // updating row
        db.update(TABLE_BILLS, values, COLUMN_BILL_ID + " = ?", new String[]{String.valueOf(bills.getId())});
        db.close();
    }
    //---------------------------------------------------------------------------------------
    /**
     * This method is to create user record
     *
     * @param settings
     */
    public void createSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTINGS_USER_ID, settings.getUser_ID());
        values.put(COLUMN_USER_PIE_CHART, settings.isPieChart());
        values.put(COLUMN_USER_BAR_CHART, settings.isBarChart());
        values.put(COLUMN_USER_RADAR_CHART, settings.isRadarChart());

        // Inserting Row
        db.insert(TABLE_SETTINGS, null, values);
        db.close();
    }
    /**
     * This method to update user record
     *
     * @param settings
     */
    public void updateSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTINGS_USER_ID, String.valueOf(settings.getUser_ID()));
        values.put(COLUMN_USER_PIE_CHART, settings.isPieChart());
        values.put(COLUMN_USER_BAR_CHART, settings.isBarChart());
        values.put(COLUMN_USER_RADAR_CHART, settings.isRadarChart());

        // updating row
        db.update(TABLE_SETTINGS, values, COLUMN_SETTINGS_ID + " = ?",
                new String[]{String.valueOf(settings.getId())});
        db.close();
    }

    public ArrayList<Settings> getSettingsByID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // sorting orders
        ArrayList<Settings> settingsArrayList = new ArrayList<Settings>();

        Cursor cursor = db.query(TABLE_SETTINGS, new String[] { COLUMN_SETTINGS_ID,
                        COLUMN_SETTINGS_USER_ID, COLUMN_USER_PIE_CHART, COLUMN_USER_RADAR_CHART, COLUMN_USER_BAR_CHART }, COLUMN_SETTINGS_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Settings settings = new Settings();
                settings.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SETTINGS_ID))));
                settings.setUser_ID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SETTINGS_USER_ID))));
                settings.setPieChart(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_PIE_CHART))> 0);
                settings.setRadarChart(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_RADAR_CHART))> 0);
                settings.setBarChart(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_BAR_CHART)) > 0);
                // Adding record to list
                settingsArrayList.add(settings);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return category list
        return settingsArrayList;
    }
}
