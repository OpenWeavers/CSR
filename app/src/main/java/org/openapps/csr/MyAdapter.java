package org.openapps.csr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private String studentName;
    private String usn;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView subject_name, subject_code, grade;
        public ViewHolder(View v) {
            super(v);
            subject_name = v.findViewById(R.id.subject_name);
            subject_code = v.findViewById(R.id.subject_code);
            grade = v.findViewById(R.id.grade);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        this.mDataset = myDataset;
    }

    public MyAdapter(Student student)   {
        this.studentName = student.getName();
        this.usn = student.getUSN();
        List<String> myDataset = new ArrayList<>(Arrays.asList(student.getMarks().split("\n")));
        this.mDataset = myDataset.toArray(new String[0]);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_marks, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String[] s = mDataset[position].split("\\|");
        holder.subject_code.setText(s[0]);
        holder.subject_name.setText(s[1]);
        holder.grade.setText(s[2]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length - 1; // -1 because of usage of .split()
    }
}

