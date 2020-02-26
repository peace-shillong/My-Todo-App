package com.peace.mytodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.peace.mytodo.recyclerview.TaskAdapter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonAddTask = findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        getTasks();
        getSingleTask();
    }

    private void getSingleTask() {
        class GetSingleTask extends AsyncTask<Void,Void,Task>{

            @Override
            protected Task doInBackground(Void... voids) {
                Task singleTask=DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getSingle(1);
                return singleTask;
            }
            @Override
            protected void onPostExecute(Task task) {
                super.onPostExecute(task);
                //TaskAdapter adapter = new TaskAdapter(MainActivity.this, task);
                //recyclerView.setAdapter(adapter);
                Log.d("Single Task data",task.getTask()+" - "+task.getDesc());
            }
        }
        GetSingleTask gt2=new GetSingleTask();
        gt2.execute();
    }

    private void getTasks() {
        //method that creates a thread to do work in the background for accessing object from RoomDb
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }
            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                TaskAdapter adapter = new TaskAdapter(MainActivity.this, tasks);
                recyclerView.setAdapter(adapter);
            }
        }
        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }
}
