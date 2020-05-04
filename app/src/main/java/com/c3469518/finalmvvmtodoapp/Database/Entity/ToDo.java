package com.c3469518.finalmvvmtodoapp.Database.Entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entity class for to-do
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
@Entity(tableName = "todos")
public class ToDo {

    /**
     * The ID of the task
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todo_id")
    private int todoId;

    /**
     * Name given to the task
     */
    private String name;

    /**
     * A short description of the task
     */
    private String description;

    /**
     * Date that the task must be completed by
     */
    @ColumnInfo(name = "due_date")
    private String dueDate;

    /**
     * State of completedness
     */
    @ColumnInfo(name = "is_completed")
    private boolean completed;

    /**
     * Path to the thumbnail
     */
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    public ToDo() {}

    @Ignore
    public ToDo(String name, String description, String dueDate, boolean completed, @Nullable String thumbnail) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;

        if (thumbnail != null){
            this.thumbnail = thumbnail;
        } else{
            this.thumbnail = "";
        }
    }

    /**
     * Returns the ID of the to-do
     * @return int value of ID
     */
    public int getTodoId() {
        return todoId;
    }

    /**
     * Sets ID of the To-do
     * Room requires this method.
     *
     * @param todoId the new ID of to-do
     */
    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    /**
     * Returns a String of the to-do's name.
     *
     * @return the name property of the to-do
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name property of the to-do to the String object that is passed in.
     *
     * @param name the new name of the to-do
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a String of the to-do's description.
     *
     * @return  the description property of the to-do
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description property of the to-do to the String object that is passed in.
     *
     * @param description the new description of the to-do
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the date for the due date of the to-do.
     *
     * @return  due date of the to-do
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Sets the DueDate property of the to-do.
     * Used to set the due date to the value that is passed in.
     * @param dueDate new due date of the to-do
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Returns the file path of the thumbnail of the to-do.
     *
     * @return  file path of the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the thumbnail property of the to-do.
     * Used to set the thumbnail to the value that is passed in.
     * @param thumbnail new thumbnail of the to-do
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Boolean value for the to-do's state of completion
     * @return true if the to-do is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Set the completed state of the to-do to the value passed in
     * @param completed boolean value for the completedness of the to-do
     */
    public void setCompleted(boolean completed){
        this.completed = completed;
    }


}
