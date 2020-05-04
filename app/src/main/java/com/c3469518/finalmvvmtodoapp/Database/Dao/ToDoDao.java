package com.c3469518.finalmvvmtodoapp.Database.Dao;

import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Interface for the to-do DAO
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
@Dao
public interface ToDoDao {

   /**
    * Insert a to-do into the database.
    * @param todo the to-do to insert into the database
    */
   @Insert
   void insertToDo(ToDo todo);

   /**
    * Update the existing to-do in the database with the to-do that is passed in.
    * @param todo the to-do containing the update
    */
   @Update
   void updateToDo(ToDo todo);

   /**
    * Delete the to-do from the database, indicated by the ID that is passed in.
    * @param todo the to-do to delete
    */
   @Delete
   void deleteToDo(ToDo todo);

   /**
    * Return all of the to-dos in the database.
    */
   @Query("SELECT * FROM todos ORDER BY due_date DESC")
   LiveData<List<ToDo>> getAllToDos();

   /**
    * Completely wipe the database of to-dos.
    */
   @Query("DELETE FROM todos")
   void wipeDatabase();


}
