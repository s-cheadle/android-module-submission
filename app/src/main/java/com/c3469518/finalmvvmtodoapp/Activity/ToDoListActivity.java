package com.c3469518.finalmvvmtodoapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.R;
import com.c3469518.finalmvvmtodoapp.UI.Fragment.ToDoFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import static com.c3469518.finalmvvmtodoapp.GlobalConstants.ADD_TODO_REQ;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EDIT_TODO_REQ;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_COMPLETE;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_DATE;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_DESCRIPTION;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_ID;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_NAME;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_THUMBNAIL;

/**
 * Activity for the to-do list
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
public class ToDoListActivity extends AppCompatActivity implements ToDoFragment.ToDoFragmentListener {

    private final String TAG = "ToDoListActivity";
    private final String FRAGMENT_TAG = "ToDoListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            ToDoFragment toDoFragment = ToDoFragment.createNewInstance();
            toDoFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, toDoFragment, FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void EditToDo(ToDo todo) {
        Intent intent = new Intent(this, AddEditToDoActivity.class);

        intent.putExtra(EXTRA_NAME, todo.getName());
        intent.putExtra(EXTRA_DESCRIPTION, todo.getDescription());
        intent.putExtra(EXTRA_DATE, todo.getDueDate());
        intent.putExtra(EXTRA_COMPLETE, todo.isCompleted());
        intent.putExtra(EXTRA_THUMBNAIL, todo.getThumbnail());
        intent.putExtra(EXTRA_ID, todo.getTodoId());

        super.startActivityForResult(intent, EDIT_TODO_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        requestCode = requestCode & 0x0000ffff; //This is ridiculous. https://stackoverflow.com/questions/10564474/wrong-requestcode-in-onactivityresult

        FragmentManager fm = getSupportFragmentManager();
        ToDoFragment fragment = (ToDoFragment) fm.findFragmentByTag(FRAGMENT_TAG);

        if (requestCode == ADD_TODO_REQ && resultCode == RESULT_OK && data != null) {

            Log.d(TAG, "onActivityResult: ADD TODO");

            ToDo todo = new ToDo(
                    data.getStringExtra(EXTRA_NAME),
                    data.getStringExtra(EXTRA_DESCRIPTION),
                    data.getStringExtra(EXTRA_DATE),
                    data.getBooleanExtra(EXTRA_COMPLETE, false),
                    data.getStringExtra(EXTRA_THUMBNAIL)
            );

            fragment.InsertToDo(todo);

            Toast.makeText(getApplicationContext(), R.string.save, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TODO_REQ && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: EDIT TODO");

            int id = data.getIntExtra(EXTRA_ID, -1);

            if (id == -1) {
                Log.d(TAG, "onActivityResult: UPDATE ERROR");
                Toast.makeText(this, R.string.error_update, Toast.LENGTH_SHORT).show();
                return;
            }

            ToDo todo = new ToDo(
                    data.getStringExtra(EXTRA_NAME),
                    data.getStringExtra(EXTRA_DESCRIPTION),
                    data.getStringExtra(EXTRA_DATE),
                    data.getBooleanExtra(EXTRA_COMPLETE, false),
                    data.getStringExtra(EXTRA_THUMBNAIL)
            );

            todo.setTodoId(id);
            fragment.UpdateToDo(todo);
            Toast.makeText(getApplicationContext(), R.string.updated, Toast.LENGTH_SHORT).show();

        } else {
            Log.d(TAG, "onActivityResult: Didn't SAVE");
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.wipe_database_menu_item) {
            fragment.wipeDatabase();
            Toast.makeText(this, R.string.wipe_database, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }*/

}
