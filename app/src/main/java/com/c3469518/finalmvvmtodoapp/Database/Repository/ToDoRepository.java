package com.c3469518.finalmvvmtodoapp.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.c3469518.finalmvvmtodoapp.Database.Dao.ToDoDao;
import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.Database.ToDoDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Repository class for the to-do app
 *
 * @author Scott Cheadle
 */
public class ToDoRepository {

    private ToDoDao toDoDao;
    private LiveData<List<ToDo>> todoList;

    public ToDoRepository(Application app){
        ToDoDatabase db = ToDoDatabase.getInstance(app);
        toDoDao = db.toDoDao();
        todoList = toDoDao.getAllToDos();
    }

    /**
     * Inserts a to-do object into the database
     *
     * @param todo to be inserted
     */
    public void insert(ToDo todo){
        new InsertToDoAsyncTask(toDoDao).execute(todo);
    }

    /**
     * Updates an existing to-do with the new to-do
     * @param todo the new to-do
     */
    public void update(ToDo todo){
        new UpdateToDoAsyncTask(toDoDao).execute(todo);
    }

    /**
     * Removes an existing to-do from the database
     * @param todo to be removed
     */
    public void delete(ToDo todo){
        new DeleteToDoAsyncTask(toDoDao).execute(todo);
    }

    /**
     * Removes all data from the database
     */
    public void wipeDatabase(){
        new WipeDatabaseAsyncTask(toDoDao).execute();
    }

    /**
     * Returns a list of all existing to-dos
     * @return list of to-dos
     */
    public LiveData<List<ToDo>> getAllToDos(){
        return todoList;
    }

    /**
     * AsyncTask class that inserts a given number of to-dos into the database
     */
    private static class InsertToDoAsyncTask extends AsyncTask<ToDo, Void, Void> {
        private ToDoDao todoDao;

        private InsertToDoAsyncTask(ToDoDao dao){
            todoDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            todoDao.insertToDo(toDos[0]);
            return null;
        }
    }

    /**
     * AsyncTask class that is used to update a given number of to-dos
     */
    private static class UpdateToDoAsyncTask extends AsyncTask<ToDo, Void, Void> {
        private ToDoDao todoDao;

        private UpdateToDoAsyncTask(ToDoDao dao){
            todoDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            todoDao.updateToDo(toDos[0]);
            return null;
        }
    }

    /**
     * AsyncTask class used for removing to-dos from the database
     */
    private static class DeleteToDoAsyncTask extends AsyncTask<ToDo, Void, Void> {
        private ToDoDao todoDao;

        private DeleteToDoAsyncTask(ToDoDao dao){
            todoDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            todoDao.deleteToDo(toDos[0]);
            return null;
        }
    }

    /**
     * AsyncTask class that will remove all entries from the database
     */
    private static class WipeDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private ToDoDao todoDao;

        private WipeDatabaseAsyncTask(ToDoDao dao){
            todoDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todoDao.wipeDatabase();
            return null;
        }
    }
}
