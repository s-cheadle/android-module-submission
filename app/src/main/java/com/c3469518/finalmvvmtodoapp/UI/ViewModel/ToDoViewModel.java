package com.c3469518.finalmvvmtodoapp.UI.ViewModel;

import android.app.Application;

import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.Database.Repository.ToDoRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * ToDoViewModel -- View model class for the To-Do app
 *
 * @author Scott cheadle
 */
public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;
    private LiveData<List<ToDo>> todoLiveData;

    public ToDoViewModel(@NonNull Application app) {
        super(app);
        toDoRepository = new ToDoRepository(app);
        todoLiveData = toDoRepository.getAllToDos();
    }

    /**
     * Calls the insert method from the {@link ToDoRepository}
     * Creates a new entry in the database
     *
     * @param todo to be inserted into the database
     * @see ToDoRepository
     */
    public void insert(ToDo todo){
        toDoRepository.insert(todo);
    }

    /**
     * Calls the update method from the {@link ToDoRepository}
     * Updates the existing to-do with the values in the to-do passed in
     * @param todo updated to-do
     * @see ToDoRepository
     */
    public void update(ToDo todo){
        toDoRepository.update(todo);
    }

    /**
     * Calls the delete method from the {@link ToDoRepository}
     * Removes the indicated to-do from the database
     * @param todo to be removed from the database
     * @see ToDoRepository
     */
    public void delete(ToDo todo){
        toDoRepository.delete(todo);
    }

    /**
     * Calls the wipeDatabase method from the {@link ToDoRepository}
     * Removes all to-do's from the database
     * @see ToDoRepository
     */
    public void wipeDatabase(){
        toDoRepository.wipeDatabase();
    }

    /**
     * Returns the LiveData List of all to-do's
     * @return todoLiveData LiveData of all To-Do's in the database
     */
    public LiveData<List<ToDo>> getTodoLiveData() {
        return todoLiveData;
    }


}
