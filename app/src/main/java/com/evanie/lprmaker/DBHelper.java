package com.evanie.lprmaker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    //table values for student marks
    public static final String STUDENT_MARKS = "STUDENT_MARKS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SEX = "SEX";

    public DBHelper(Context context) {
        super(context, Utils.databaseName, null, 1);
    }

    //this is called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + STUDENT_MARKS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_SEX + " TEXT, TOTAL INTEGER, GRADE INTEGER, TOTAL_GRADE INTEGER)";
        db.execSQL(createTableStatement);
    }

    //this is called when a database version number is changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists learners");
    }

    //a function to add students
    @SuppressLint("Recycle")
    public void addOne(int id, String name, String sex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        name = name.replaceAll("[^a-zA-Z0-9 ]", "");
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_SEX, sex);
        cv.put("GRADE", 1);
        cv.put("TOTAL_GRADE", allSubjects().size());

        Cursor cursor = getData();
        if (cursor.getColumnCount() > 6) {
            for (int i = 3; i < cursor.getColumnCount() - 2; i++) {
                cv.put(cursor.getColumnName(i), 0);
            }
        }
        db.insert(STUDENT_MARKS, null, cv);
        /*Log.d("TAG", "Added new student: " + name + " to the MAIN table");

        //add in all assessment tables
        cv.clear();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_SEX, sex);

        if (allSubjects().size() != 0) {
            for (int i = 0; i < allSubjects().size(); i++) {
                cursor = db.rawQuery("Select * from " + allSubjects().get(i) + "", null);
                for (int j = 3; j < cursor.getColumnCount(); j++) {
                    cv.put(cursor.getColumnName(j), 0);
                }
                db.insert(allSubjects().get(i), null, cv);
                Log.e("TAG", "Added new student: " + name + " to the " + allSubjects().get(i) + " table");
            }
        }*/
    }

    //get all the subjects
    public ArrayList<String> allSubjects(){
        ArrayList<String> subjects = new ArrayList<>();
        Cursor cursor = getData();
        for (int i = 3; i < cursor.getColumnCount()-3; i++){
            subjects.add(cursor.getColumnName(i));
        }
        return subjects;
    }

    //a function to fetch students by their ID
    public String getStudentName(String id){
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM "+ STUDENT_MARKS + " WHERE " + COLUMN_ID + " = " + id;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
        }
        return name;
    }

    //a function to fetch students by their ID
    public ArrayList<Integer> getStudent(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ STUDENT_MARKS + " WHERE " + COLUMN_ID + " = " + id;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            for (int i = 3; i < cursor.getColumnCount()-3; i++){
                arrayList.add(cursor.getInt(i));
            }
        }
        return arrayList;
    }

    //a function that updates students' scores
    public boolean updateStudentMarks(String id, int marks, String subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(subject, marks);
        int update = db.update(STUDENT_MARKS, cv, COLUMN_ID + " = ? ", new String[]{id});
        return update == 1;
    }

    //get all learners' data
    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor;
        cursor = DB.rawQuery("Select * from STUDENT_MARKS", null);
        return cursor;
    }

    public Cursor getRankedData(String rankBy){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (rankBy.equals("grades")){
            cursor = db.rawQuery("Select * from STUDENT_MARKS order by TOTAL_GRADE desc, TOTAL desc", null);
        }else {
            cursor = db.rawQuery("Select * from STUDENT_MARKS order by TOTAL desc", null);
        }
        return cursor;
    }

    public Cursor getAssessmentData(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("Select * from " + tableName + " order by TOTAL desc", null);
        return cursor;
    }

    public void refresh(String id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        int totalMarks = 0;
        int totalGrades = 0;
        int grade;
        ArrayList<Integer> scores;
        scores = getStudent(id);
        for (int i = 0; i < scores.size(); i++){
            totalMarks += scores.get(i);
            totalGrades += getGrade(scores.get(i));
        }
        grade = Math.round((float) totalGrades / (float) (allSubjects().size()));

        cv.put("TOTAL", totalMarks);
        cv.put("TOTAL_GRADE", totalGrades);
        cv.put("GRADE", grade);
        db.update(STUDENT_MARKS, cv, COLUMN_ID + " = ? ", new String[]{id});
    }

    @SuppressLint({"Recycle", "Range"})
    public int gradingData(int grade, String column){
        SQLiteDatabase DB = this.getWritableDatabase();
        String queryString;
        Cursor cursor;
        if (column.equals("GRADE")) {
            if (subjectExist(column)){
                if (grade > 1) {
                    queryString = "SELECT * FROM "+ STUDENT_MARKS + " WHERE " + column + " = " + grade;
                } else {
                    queryString = "SELECT * FROM " + STUDENT_MARKS + " WHERE " + column + " <= " + grade;
                }
                cursor = DB.rawQuery(queryString, null);
                return cursor.getCount();
            } else {
                return 0;
            }

        } else {
            cursor = getData();
            int grade4 = 0, grade3 = 0, grade2 = 0, grade1 = 0;
            if (cursor.moveToFirst()){
                do {
                    if (getGrade(cursor.getInt(cursor.getColumnIndex(column))) == 4){
                        grade4++;
                    } else if (getGrade(cursor.getInt(cursor.getColumnIndex(column))) == 3){
                        grade3++;
                    } else if (getGrade(cursor.getInt(cursor.getColumnIndex(column))) == 2){
                        grade2++;
                    } else {
                        grade1++;
                    }
                }while (cursor.moveToNext());
                if (grade == 4){
                    return grade4;
                } else if (grade == 3){
                    return grade3;
                } else if (grade == 2){
                    return grade2;
                } else {
                    return grade1;
                }
            } else {
                return 0;
            }
        }
    }

    //get grade for each subject
    public int getGrade(int marks){
        if (marks > 79){
            return  4;
        }
        else if (marks > 59){
            return 3;
        }
        else if (marks > 39){
            return 2;
        }
        else {
            return 1;
        }
    }

    //delete a student from the database using their ID
    public void removeStudent(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete student from the main table
        db.delete(STUDENT_MARKS, COLUMN_ID + " = ? ", new String[]{ id });
        Log.d("TAG", "Removed student with the ID: " + id + " from the MAIN table");

        //delete student from the assessment tables
        /*for (int i = 0; i < allSubjects().size(); i++) {
            db.delete(allSubjects().get(i), COLUMN_ID + " = ? ", new String[]{ id });
            Log.e("TAG", "Removed student with the ID: " + id + " from the " + allSubjects().get(i) + " table");
        }*/
        db.close();
    }

    //clear all data in the database
    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STUDENT_MARKS,null,null);
        db.execSQL("drop Table if exists _STUDENT_MARKS_old");
        db.close();
    }

    //clear all learners marks in the database
    public void clearMarks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS _STUDENT_MARKS_old");
        db.execSQL("ALTER TABLE STUDENT_MARKS RENAME TO _STUDENT_MARKS_old");
        String createTableStatement = "CREATE TABLE " + STUDENT_MARKS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_SEX + " TEXT)";
        db.execSQL(createTableStatement);
        db.execSQL("INSERT INTO STUDENT_MARKS (" + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + ") SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + " FROM _STUDENT_MARKS_old");
        db.execSQL("ALTER TABLE STUDENT_MARKS ADD TOTAL INTEGER");
        db.execSQL("ALTER TABLE STUDENT_MARKS ADD GRADE INTEGER");
        db.execSQL("ALTER TABLE STUDENT_MARKS ADD TOTAL_GRADE INTEGER");

        Cursor cursor = getData();
        if (cursor.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                String id = cursor.getString(0);
                cv.put("TOTAL", 0);
                cv.put("GRADE", 0);
                cv.put("TOTAL_GRADE", 0);
                db.update(STUDENT_MARKS, cv, COLUMN_ID + " = ? ", new String[]{id});
                cv.clear();
            } while (cursor.moveToNext());
        }
        db.close();
    }

    //add more subjects
    @SuppressLint("Recycle")
    public void addSubjects(String subject){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getData();
        if (cursor.getColumnName(cursor.getColumnCount()-1).equals("TOTAL_GRADE")) {
            alterTable();
        }

        subject = subject.replaceAll("[^a-zA-Z0-9]", "_");
        if (!subjectExist(subject)){
            db.execSQL("ALTER TABLE STUDENT_MARKS ADD " + subject + " INTEGER");
            if (cursor.moveToFirst()){
                do {
                    ContentValues cv = new ContentValues();
                    String id = cursor.getString(0);
                    cv.put(subject, 0);
                    db.update(STUDENT_MARKS, cv, COLUMN_ID + " = ? ", new String[]{id});
                    cv.clear();
                }while (cursor.moveToNext());
            }
            Log.i("TAG", "Added new subject: " + subject);
        }

        //create assessment table for the subject
        /*if (!subject.equals("TOTAL") && !subject.equals("GRADE") && !subject.equals("TOTAL_GRADE")) {
            String queryString = "CREATE TABLE " + subject + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_SEX + " TEXT, TOTAL INTEGER)";
            db.execSQL(queryString);
            db.execSQL("INSERT INTO " + subject + " (" + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + ") SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + " FROM STUDENT_MARKS");

            cursor = db.rawQuery("Select * from " + subject + "", null);
            if (cursor.moveToFirst()) {
                do {
                    ContentValues cv = new ContentValues();
                    String id = cursor.getString(0);
                    for (int j = 3; j < cursor.getColumnCount(); j++) {
                        cv.put(cursor.getColumnName(j), 0);
                    }
                    db.update(subject, cv, COLUMN_ID + " = ? ", new String[]{id});
                    cv.clear();
                }while (cursor.moveToNext());
            }
            Log.e("TAG", "Created new assessment table: " + subject);
        }*/
        db.close();
    }

    //check if subject exists
    @SuppressLint("Recycle")
    private boolean subjectExist(String subject){
        boolean exist;
        SQLiteDatabase DB = getReadableDatabase();
        Cursor cursor;
        cursor = DB.rawQuery("Select * from STUDENT_MARKS", null);
        int index = cursor.getColumnIndex(subject);
        exist = index != -1;
        return exist;
    }

    //alter table
    private void alterTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS _STUDENT_MARKS_old");
        db.execSQL("ALTER TABLE STUDENT_MARKS RENAME TO _STUDENT_MARKS_old");
        String createTableStatement = "CREATE TABLE " + STUDENT_MARKS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_SEX + " TEXT)";
        db.execSQL(createTableStatement);
        db.execSQL("INSERT INTO STUDENT_MARKS (" + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + ") SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + " FROM _STUDENT_MARKS_old");

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from _STUDENT_MARKS_old", null);
        for (int i = 3; i < cursor.getColumnCount()-3; i++){
            db.execSQL("ALTER TABLE STUDENT_MARKS ADD " + cursor.getColumnName(i) + " INTEGER");
        }
        while (cursor.moveToNext()){
            for (int i = 3; i < cursor.getColumnCount()-3; i++){
                String id = cursor.getString(0);
                ContentValues cv = new ContentValues();
                cv.put(cursor.getColumnName(i), cursor.getString(i));
                db.update(STUDENT_MARKS, cv, COLUMN_ID + " = ? ", new String[]{id});
            }
        }
        db.execSQL("DROP TABLE _STUDENT_MARKS_old");
    }

    //remove subjects
    public void removeSubjects(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS _STUDENT_MARKS_old");
        db.execSQL("ALTER TABLE STUDENT_MARKS RENAME TO _STUDENT_MARKS_old");
        String createTableStatement = "CREATE TABLE " + STUDENT_MARKS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_SEX + " TEXT)";
        db.execSQL(createTableStatement);
        db.execSQL("INSERT INTO STUDENT_MARKS (" + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + ") SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SEX + " FROM _STUDENT_MARKS_old");

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from _STUDENT_MARKS_old", null);
        for (int i = 3; i < cursor.getColumnCount()-3; i++){
            if (!cursor.getColumnName(i).equals(subject)) {
                db.execSQL("ALTER TABLE STUDENT_MARKS ADD " + cursor.getColumnName(i) + " INTEGER");
                Log.d("TAG", cursor.getColumnName(i));
            }
        }
        if (cursor.moveToFirst()){
            do {
                for (int i = 3; i < cursor.getColumnCount()-3; i++){
                    if (!cursor.getColumnName(i).equals(subject)){
                        String id = cursor.getString(0);
                        ContentValues cv = new ContentValues();
                        cv.put(cursor.getColumnName(i), cursor.getString(i));
                        db.update(STUDENT_MARKS, cv, COLUMN_ID + " = ? ", new String[]{id});
                    }
                }
            }while (cursor.moveToNext());
        }
        db.execSQL("DROP TABLE _STUDENT_MARKS_old");
        Log.i("TAG", "Removed a subject: " + subject);

        //delete assessment table
        /*db.execSQL("DROP TABLE IF EXISTS " + subject + "");
        Log.e("TAG", "Deleted assessment table: " +subject);*/
        db.close();
    }

    //export learners' data to in CSV
    public void exportDB(String rankBy){
        String root = Environment.getExternalStorageDirectory().toString();
        File exportDir = new File(root + "/Documents");
        if (!exportDir.exists()){
            if (exportDir.mkdirs());
        }
        File file = new File(exportDir, "Performance Record.csv");
        try {
            if (file.createNewFile());
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
            String s = "";
            String total;
            int pos = 0;
            Cursor cursor = getRankedData(rankBy);

            //get column names
            String[] columnNames = new String[cursor.getColumnCount()-1];
            columnNames[0] = "POS";
            for (int i = 1; i < cursor.getColumnCount()-1; i++){
                columnNames[i] = cursor.getColumnName(i);
            }

            //write column names
            csvWriter.writeNext(columnNames);

            //get data
            if (cursor.moveToFirst()){
                do {
                    total = cursor.getString(cursor.getColumnIndexOrThrow("TOTAL"));
                    if (!total.equals(s)){
                        pos++;
                    }
                    String[] columnData = new String[columnNames.length];
                    columnData[0] = String.valueOf(pos);
                    for (int i = 1; i < columnNames.length; i++){
                        columnData[i] = cursor.getString(cursor.getColumnIndexOrThrow(columnNames[i]));
                    }

                    //write data
                    csvWriter.writeNext(columnData);
                    s = total;
                }while (cursor.moveToNext());
            }
            csvWriter.close();
            cursor.close();
        }
        catch (Exception sqlEx){
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public void copyDatabase(){
        String root = Environment.getExternalStorageDirectory().toString();
        String databaseDir = root+"/Android/data/com.evanie.lprmaker/files/databases/learners.db";
        SQLiteDatabase db = this.getWritableDatabase();
        if (!db.isDbLockedByCurrentThread()){
            db.execSQL("ATTACH "+databaseDir+" AS SUB");
            Log.v("TAG", "Database attached");
            db.execSQL("INSERT INTO STUDENT_TABLE SELECT * FROM SUB.STUDENT_TABLE");
            Log.v("TAG", "Database copied");
            db.execSQL("DETACH SUB");
            Log.v("TAG", "Database detached");
        } else {
            Log.v("TAG", "Database is locked");
        }
    }
}
