package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    List<TaskModel> taskModelList;
     DatabaseReference ref;
     String taskid;
    public TaskAdapter(List<TaskModel> taskModelList) {
        this.taskModelList = taskModelList;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task_for_adapter,null);
        ref= FirebaseDatabase.getInstance().getReference();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.ViewHolder holder, int position) {
              TaskModel taskModel=taskModelList.get(position);
              holder.taskauthor.setText(taskModel.getTaskAuthor());
              holder.taskinfo.setText(taskModel.getTaskinfo());
              holder.taskdesc.setText(taskModel.getTaskDesc());
              holder.taskStatus.setText(taskModel.getStatus());
                 taskid=taskModel.getTaskid();
                     if(taskModel.getStatus().equals("complete"))
                     {
                         holder.okBtn.setVisibility(View.INVISIBLE);
                         holder.okBtn.setEnabled(false);
                         holder.checkbox.setVisibility(View.INVISIBLE);


                     }
        final Map<String,Object> map= new HashMap<>();

               holder.okBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(final View view) {
                    map.put("status","complete");
                       if(holder.checkbox.isChecked())
                       {

                           ref.child("Tasks").child(taskid).updateChildren(map)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                         holder.okBtn.setEnabled(false);
                                         holder.checkbox.setVisibility(View.INVISIBLE);
                                         holder.okBtn.setVisibility(View.INVISIBLE);
                                           Toast.makeText(view.getContext(),"status updated",Toast.LENGTH_SHORT).show();
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {

                                   Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                               }
                           });

                       }
                       else {
                           Toast.makeText(view.getContext(),"please select checkbox",Toast.LENGTH_SHORT).show();
                       }
                   }
               });
                 holder.taskDeleteBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(final View view) {

                      AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                      builder.setMessage("Are you sure want to delete");
                      builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                              ref.child("Tasks").child(taskid)
                                      .removeValue()
                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void aVoid) {
                                              Toast.makeText(view.getContext(),"Task deleted",Toast.LENGTH_SHORT).show();
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }
                      });
                      builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                          }
                      });
                      builder.show();
                  }
              });

    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView taskinfo;
        private TextView taskdesc;
        private TextView taskauthor;
        private Button taskDeleteBtn;
        private  Button taskEditBtn;
        private TextView taskStatus;
        private CheckBox checkbox;
        private Button okBtn;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            taskinfo=itemView.findViewById(R.id.task_info_view);
            taskdesc=itemView.findViewById(R.id.task_desc_view);
            taskauthor=itemView.findViewById(R.id.task_author_view);
            taskDeleteBtn=itemView.findViewById(R.id.delete_task_btn);
            taskEditBtn=itemView.findViewById(R.id.edit_task_btn);
            taskStatus=itemView.findViewById(R.id.task_status_view);
            checkbox=itemView.findViewById(R.id.complete_status_checkbox);
            okBtn=itemView.findViewById(R.id.ok_btn);
            taskEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskDialogFragment taskDialogFragment=new TaskDialogFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("taskid",taskid);
                    taskDialogFragment.setArguments(bundle);
                    FragmentManager manager=((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
                    taskDialogFragment.show(manager,"open dialog");
                }
            });


        }
    }
}
