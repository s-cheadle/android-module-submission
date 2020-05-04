package com.c3469518.finalmvvmtodoapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.R;
import com.c3469518.finalmvvmtodoapp.UI.Fragment.AddEditFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_COMPLETE;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_DATE;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_DESCRIPTION;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_ID;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_NAME;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_THUMBNAIL;

/**
 * Activity for Add/Edit to-do
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
public class AddEditToDoActivity extends AppCompatActivity implements AddEditFragment.AddEditFragmentListener {

    private final String FRAGMENT_TAG = "AddEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        if (findViewById(R.id.add_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            AddEditFragment fragment = new AddEditFragment();
            Bundle bundle = getIntent().getExtras();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.add_container,fragment,FRAGMENT_TAG)
                    .commitNow();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //test
    }

    @Override
    public void SaveTodo(ToDo todo) {

        Intent data = new Intent();

        data.putExtra(EXTRA_NAME, todo.getName());
        data.putExtra(EXTRA_DESCRIPTION, todo.getDescription());
        data.putExtra(EXTRA_COMPLETE, todo.isCompleted());
        data.putExtra(EXTRA_DATE, todo.getDueDate());
        data.putExtra(EXTRA_THUMBNAIL, todo.getThumbnail());

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void SetTitle(String title) {
        setTitle(title);
    }


}
