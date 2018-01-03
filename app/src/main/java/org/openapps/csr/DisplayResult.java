package org.openapps.csr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.openapps.csr.MainActivity.EXTRA_MESSAGE;

public class DisplayResult extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    String usn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                final Intent intent = getIntent();
                usn = intent.getStringExtra(EXTRA_MESSAGE);
                new Result().execute();
            }
        });
    }


    @Override
    public void onRefresh() {
        recreate();
    }

    private class Result extends AsyncTask<Void, Void, Void> {
        String res = "";
        Student student = null;

        @Override
        protected void onPreExecute()   {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                student = ResultFetcher.fetchResult(usn);
                //res = student.toString();
            } catch (IOException e) {
                res = "Check your Internet connectivity";
            }
            catch (Exception e) {
                res = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //List<String> myDataset = new ArrayList<>(Arrays.asList(res.split("\n")));
            //String[] data = myDataset.toArray(new String[0]);
            //String studentName = student.getName();
            //String usn = student.getUSN();
            String[] data = {""};
            try {
                String marks = student.getMarks();
                List<String> myDataset = new ArrayList<>(Arrays.asList(marks.split("\n")));
                data = myDataset.toArray(new String[0]);
                //mAdapter = new MyAdapter(data);
                TextView studentNameView = findViewById(R.id.student_name);
                studentNameView.setText(student.getName());
                TextView studentUsnView = findViewById(R.id.student_usn);
                studentUsnView.setText(student.getUSN());
                TextView studentSgpaView = findViewById(R.id.sgpa);
                studentSgpaView.setText("SGPA : "+String.valueOf(student.getSgpa()));
                mAdapter = new MyAdapter(data);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(DisplayResult.this, LinearLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);
            }
            catch (NullPointerException e)  {
                RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
                recyclerView.setVisibility(View.GONE);
                TextView textView = findViewById(R.id.error_text_view);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Error! Swipe Down to refresh.");
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
