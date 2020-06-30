package com.example.todolist;

public class TaskModel  {

    String taskinfo;
    String taskDesc;
    String taskAuthor;
    String taskid;
    String status;


    public TaskModel() {
    }

    public TaskModel(String status,String taskinfo, String taskDesc, String taskAuthor, String taskid) {
        this.taskinfo = taskinfo;
        this.status=status;
        this.taskDesc = taskDesc;
        this.taskAuthor = taskAuthor;
        this.taskid = taskid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskinfo() {
        return taskinfo;
    }

    public void setTaskinfo(String taskinfo) {
        this.taskinfo = taskinfo;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskAuthor() {
        return taskAuthor;
    }

    public void setTaskAuthor(String taskAuthor) {
        this.taskAuthor = taskAuthor;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}
