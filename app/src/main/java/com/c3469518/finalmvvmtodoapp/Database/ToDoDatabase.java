package com.c3469518.finalmvvmtodoapp.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.c3469518.finalmvvmtodoapp.Database.Dao.ToDoDao;
import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Abstract class of the database
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
@Database(entities = {ToDo.class}, version = 1, exportSchema = false)
public abstract class ToDoDatabase extends RoomDatabase {

    /**
     * Singleton instance of the database.
     */
    private static volatile ToDoDatabase dbInstance;

    /**
     * @return Returns the Data Access Object for the To-Do database
     */
    public abstract ToDoDao toDoDao();

    /**
     * Singleton method for accessing the database instance.
     *
     * @param context Application context, used for the databaseBuilder in Room.
     * @return the initialised instance of the database
     */
    public static synchronized ToDoDatabase getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.
                    databaseBuilder(
                            context.getApplicationContext(),
                            ToDoDatabase.class,
                            "todo_db"
                    ).addCallback(callback)
                    .build();
        }
        return dbInstance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new SeedDataAsyncTask(dbInstance).execute();
        }
    };

    private static class SeedDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private ToDoDao todoDao;

        /**
         * Seed Data for the database on init
         *
         * @param db ToDoDatabase used to acquire the dao
         */
        private SeedDataAsyncTask(ToDoDatabase db) {
            todoDao = db.toDoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 1; i <= 20; i++) {
                todoDao.insertToDo(new ToDo("todo-" + i, "Delete me plz ", "16/01/2020", false, ""));
            }

            return null;
        }
    }
}

