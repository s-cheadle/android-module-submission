package com.c3469518.finalmvvmtodoapp.UI.ListAdapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.c3469518.finalmvvmtodoapp.Database.Entity.ToDo;
import com.c3469518.finalmvvmtodoapp.R;

/**
 * List adapter for the to-do list
 * @author Scott Cheadle <s.cheadle8424@student.leedsbeckett.ac.uk>
 */
public class ToDoListAdapter extends ListAdapter<ToDo, ToDoListAdapter.ToDoViewHolder> {

    private ItemClickListener listener;

    public ToDoListAdapter() {
        super(diffCallback);
    }

    /**
     * DiffUtil Item callback used to check the contents of the ToDoList
     */
    private static final DiffUtil.ItemCallback<ToDo> diffCallback = new DiffUtil.ItemCallback<ToDo>() {

        @Override
        public boolean areItemsTheSame(@NonNull ToDo oldItem, @NonNull ToDo newItem) {
            return oldItem.getTodoId() == newItem.getTodoId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDo oldItem, @NonNull ToDo newItem) {
            return oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getName().equals(newItem.getName())
                    && oldItem.getDueDate().equals(newItem.getDueDate())
                    && oldItem.getThumbnail().equals(newItem.getThumbnail());
        }
    };

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ToDoViewHolder holder, int position) {
        ToDo current = getItem(position);

        holder.nameTextView.setText(current.getName());
        holder.dateTextView.setText(current.getDueDate());
        holder.doneCheckbox.setChecked(current.isCompleted());
    }

    /**
     * Returns the To-Do object at the given index
     */
    public ToDo getTodoAt(int position) {
        return getItem(position);
    }

    /**
     * ViewHolder class for the adapter.
     */
    class ToDoViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private CheckBox doneCheckbox;

        ToDoViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_textview);
            dateTextView = itemView.findViewById(R.id.date_textview);
            doneCheckbox = itemView.findViewById(R.id.done_checkbox);

            doneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getTodoAt(getAdapterPosition()).setCompleted(isChecked);

                    if (isChecked) {
                        nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    //Check for invalid clicks
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(pos));
                    }
                }
            });
        }
    }

    /**
     *  setItemClickListener -- custom ItemClickListener
     *  Instantiates the listener inside {@link ToDoListAdapter} to the ItemClickListener that is passed in.
     */
    public void setItemClickListener(ItemClickListener<ToDo> listener) {
        this.listener = listener;
    }
}