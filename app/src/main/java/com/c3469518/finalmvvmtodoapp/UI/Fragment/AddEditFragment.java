package com.c3469518.finalmvvmtodoapp.UI.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.R;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_COMPLETE;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_DATE;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_DESCRIPTION;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_NAME;
import static com.c3469518.finalmvvmtodoapp.GlobalConstants.EXTRA_THUMBNAIL;

/**
 * Fragment class for Add/Edit to-do
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
public class AddEditFragment extends Fragment {

    private final int RESULT_IMAGE = 0;
    private final String TAG = "ADD_EDIT";

    private AddEditFragmentListener listener;

    private Context context;

    private CheckBox checkBox;
    private EditText editTextName;
    private EditText editTextDescription;
    private ImageButton imageButton;
    private TextView textView;

    private LocalDate date;
    private String thumbnailPath = "";

    public AddEditFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Setup the views
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        editTextName = view.findViewById(R.id.name_edittext);
        editTextDescription = view.findViewById(R.id.description_edittext);

        checkBox = view.findViewById(R.id.complete_checkbox);

        DatePicker datePicker = view.findViewById(R.id.date_picker);
        date = LocalDate.now();
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("OnDateChanged", String.format("Date Changed: year-%d month-%d day-%d", year, monthOfYear, dayOfMonth));

                //yes, months need +1, but days don't... this is not an accident
                date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
            }
        });

        textView = view.findViewById(R.id.path);

        imageButton = view.findViewById(R.id.thumbnail);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            RESULT_IMAGE);
                } else {
                    // Camera stopped working in the Virtual Device...So Gallery it is!
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_IMAGE);
                }
            }
        });

        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Glide.with(v).clear(v);
                thumbnailPath = "";
                textView.setText(thumbnailPath);
                return true;
            }
        });

        Button saveButton = view.findViewById(R.id.save_todo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText().toString().trim().isEmpty() || editTextDescription.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, R.string.blank_todo_warning, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Blank name or description");
                } else {
                    ToDo todo = new ToDo(
                            editTextName.getText().toString().trim(),
                            editTextDescription.getText().toString().trim(),
                            date.toString(),
                            checkBox.isChecked(),
                            thumbnailPath
                    );
                    listener.SaveTodo(todo);
                    Log.d(TAG, "Saved: " + todo.getName());
                }
            }
        });

        Bundle bundle = getArguments();

        if (bundle != null) {
            Log.d(TAG, "Editing To-Do");
            listener.SetTitle(getString(R.string.edit_todo));
            // Name & Description
            editTextName.setText(bundle.getString(EXTRA_NAME));
            editTextDescription.setText(bundle.getString(EXTRA_DESCRIPTION));

            // Checkbox
            checkBox.setChecked(bundle.getBoolean(EXTRA_COMPLETE));

            // Date
            date = LocalDate.parse(bundle.getString(EXTRA_DATE));
            datePicker.updateDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

            //thumbnail
            thumbnailPath = bundle.getString(EXTRA_THUMBNAIL);
            setImageButton();
        } else {
            Log.d(TAG, "Add new To-Do");
            listener.SetTitle(getString(R.string.add_todo));
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;

        if (context instanceof AddEditFragmentListener) {
            listener = (AddEditFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement AddEditFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        context = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set the image button background
        if (resultCode == RESULT_OK && requestCode == RESULT_IMAGE && data != null) {

            thumbnailPath = data.getDataString();
            setImageButton();
        }
    }

    /**
     * Sets the background of the imageButton using the Glide library.
     */
    private void setImageButton() {
        if (!thumbnailPath.isEmpty()) {
            Glide.with(context).asBitmap().load(thumbnailPath).into(imageButton);
        }
        textView.setText(thumbnailPath);
    }

    /**
     * AddEditFragmentListener -- Listener interface
     * Used to send data back to the Activity
     */
    public interface AddEditFragmentListener {

        /**
         * Method used to save the to-do into the database
         *
         * @param todo to-do to be saved
         */
        void SaveTodo(ToDo todo);

        /**
         * Set the title in the Activity
         * @param title of the activity
         */
        void SetTitle(String title);
    }
}
