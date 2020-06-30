package com.example.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TaskDialogFragment extends AppCompatDialogFragment {
    private EditText taskInfo;
    private EditText taskdesc;
    private EditText taskauthor;
    private DatabaseReference ref;
    private String status = "pending";
    private ProgressBar taskProgress;
    private Button addTaskBtn;
    String taskidfromadapter;
    private Button updateTaskBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.single_task, null);
        ref = FirebaseDatabase.getInstance().getReference();
        taskInfo = view.findViewById(R.id.task_info);
        taskdesc = view.findViewById(R.id.task_desc);
        taskauthor = view.findViewById(R.id.task_author);
        taskProgress = view.findViewById(R.id.task_progress);
        addTaskBtn = view.findViewById(R.id.add_task_btn);
        updateTaskBtn = view.findViewById(R.id.update_task_btn);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            taskidfromadapter = bundle.getString("taskid");

            addTaskBtn.setVisibility(View.INVISIBLE);
            addTaskBtn.setEnabled(false);
            ref.child("Tasks").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    if (snapshot.exists()) {
                        TaskModel taskModel = snapshot.getValue(TaskModel.class);
                        String taskidftechfromfirebase = taskModel.getTaskid();
                        if (taskidftechfromfirebase.equals(taskidfromadapter)) {
                            String taskInfos = taskModel.getTaskinfo();
                            String taskDesc = taskModel.getTaskDesc();
                            String taskAuthor = taskModel.getTaskAuthor();
                            taskauthor.setText(taskAuthor);
                            taskdesc.setText(taskDesc);
                            taskInfo.setText(taskInfos);
                            addTaskBtn.setVisibility(View.INVISIBLE);
                            addTaskBtn.setEnabled(false);
                            updateTaskBtn.setVisibility(View.VISIBLE);
                            updateTaskBtn.setEnabled(true);
                        }

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            updateTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskProgress.setVisibility(View.VISIBLE);
                    String taskInfos = taskInfo.getText().toString();
                    String taskdescs = taskdesc.getText().toString();
                    String taskAuthor = taskauthor.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("taskinfo", taskInfos);
                    map.put("taskDesc", taskdescs);
                    map.put("taskAuthor", taskAuthor);
                    ref.child("Tasks").child(taskidfromadapter).updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   taskProgress.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getActivity(), "Task upated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            taskProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }

               updateTaskBtn.setVisibility(View.INVISIBLE);
               updateTaskBtn.setEnabled(false);
            addTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskProgress.setVisibility(View.VISIBLE);
                    String taskInfos = taskInfo.getText().toString();
                    String taskdescs = taskdesc.getText().toString();
                    String taskAuthor = taskauthor.getText().toString();
                    String taskid = ref.child("Tasks").push().getKey();
                    TaskModel taskModel = new TaskModel(status, taskInfos, taskdescs, taskAuthor, taskid);
                    ref.child("Tasks").child(taskid).setValue(taskModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            taskProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Task Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            taskProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            builder.setView(view).setTitle("Add your Task")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });



        return builder.create();
    }
}

