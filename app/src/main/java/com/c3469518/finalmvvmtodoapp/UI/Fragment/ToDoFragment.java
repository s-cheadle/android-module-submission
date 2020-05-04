package com.c3469518.finalmvvmtodoapp.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.c3469518.finalmvvmtodoapp.Activity.AddEditToDoActivity;
import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.R;
import com.c3469518.finalmvvmtodoapp.UI.ListAdapter.ItemClickListener;
import com.c3469518.finalmvvmtodoapp.UI.ListAdapter.ToDoListAdapter;
import com.c3469518.finalmvvmtodoapp.UI.ViewModel.ToDoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.c3469518.finalmvvmtodoapp.GlobalConstants.ADD_TODO_REQ;

/**
 * Fragment class for the to-do list
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
public class ToDoFragment extends Fragment {

    private final String TAG = "ToDoFragment";
    private Context context;
    private ToDoListAdapter todoListAdapter;
    private ToDoViewModel todoViewModel;
    private RecyclerView recyclerView;

    public static ToDoFragment createNewInstance() {
        return new ToDoFragment();
    }

    private ToDoFragmentListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        todoListAdapter = new ToDoListAdapter();

        // Implement the interface
        todoListAdapter.setItemClickListener(new ItemClickListener<ToDo>() {
            @Override
            public void onItemClick(ToDo todo) {
                Log.d(TAG, "Editing todo: " + todo.getTodoId());
                listener.EditToDo(todo);
            }
        });

        recyclerView.setAdapter(todoListAdapter);

        // for swiping to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast toast;
                ToDo todo = todoListAdapter.getTodoAt(viewHolder.getAdapterPosition());
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        Log.d(TAG, "Left swipe: delete");
                        todoViewModel.delete(todo);
                        toast = Toast.makeText(getContext(), R.string.left_swipe, Toast.LENGTH_SHORT);
                        break;
                    case ItemTouchHelper.RIGHT:
                        Log.d(TAG, "Right swipe: change completed");
                        todo.setCompleted(!todo.isCompleted());
                        todoViewModel.update(todo);
                        toast = Toast.makeText(getContext(), R.string.right_swipe, Toast.LENGTH_SHORT);
                        break;
                    default:
                        Log.d(TAG, "Swipe error");
                        toast = Toast.makeText(getContext(), R.string.swipe_error, Toast.LENGTH_SHORT);
                }
                todoListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.add_todo_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Adding todo");
                Intent intent = new Intent(context, AddEditToDoActivity.class);
                startActivityForResult(intent, ADD_TODO_REQ);
            }
        });

        todoViewModel = new ViewModelProvider(requireActivity()).get(ToDoViewModel.class);
        todoViewModel.getTodoLiveData().observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                todoListAdapter.submitList(toDos);
                Log.d(TAG, "Todos changed!");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof ToDoFragmentListener){
            listener = (ToDoFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement ToDoFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        context = null;
    }

    public void InsertToDo(ToDo todo){
        todoViewModel.insert(todo);
    }

    public void UpdateToDo(ToDo todo){
        todoViewModel.update(todo);
    }

    public interface ToDoFragmentListener{
        void EditToDo(ToDo todo);
    }
}
