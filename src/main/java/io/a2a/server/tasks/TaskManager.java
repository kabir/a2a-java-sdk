package io.a2a.server.tasks;

import static io.a2a.spec.TaskState.SUBMITTED;
import static io.a2a.util.Assert.checkNotNullParam;

import java.util.ArrayList;
import java.util.List;

import io.a2a.spec.Artifact;
import io.a2a.spec.Task;
import io.a2a.spec.TaskArtifactUpdateEvent;
import io.a2a.spec.TaskStatus;
import io.a2a.spec.TaskStatusUpdateEvent;

public class TaskManager {
    private volatile String taskId;
    private volatile String sessionId;
    private final TaskStore taskStore;

    public TaskManager(String taskId, String sessionId, TaskStore taskStore) {
        checkNotNullParam("taskStore", taskStore);
        this.taskId = taskId;
        this.sessionId = sessionId;
        this.taskStore = taskStore;
    }

    public Task getTask() {
        if (taskId == null) {
            return null;
        }
        return taskStore.get(taskId);
    }

    public void saveTaskEvent(Task task) {
        String taskId = task.id();
        saveTask(task);
    }

    public void saveTaskEvent(TaskStatusUpdateEvent event) {
        String taskId = event.id();
        Task task = ensureTask(event.id(), event.sessionId());

        // Update status
        task = new Task.Builder()
                .status(event.status())
                .build();
        saveTask(task);
    }

    public void saveTaskEvent(TaskArtifactUpdateEvent event) {
        String taskId = event.id();
        Task task = ensureTask(event.id(), event.sessionId());

        // Append artifacts
        List<Artifact> artifacts = task.artifacts() == null ? new ArrayList<>() : new ArrayList<>(task.artifacts());
        Artifact newArtifact = event.artifact();
        /* TODO Note that the reference implementation checks Artifact.id() and Artifact.append() to decide how to
         add the artifact. Until we add those fields, go with the simple path
         */
        artifacts.add(newArtifact);

        saveTask(task);
    }

    private Task ensureTask(String eventTaskId, String eventSessionId) {
        Task task = taskStore.get(taskId);
        if (task == null) {
            task = createTask(eventTaskId, eventSessionId);
            saveTask(task);
        }
        return task;
    }

    private Task createTask(String taskId, String sessionId) {
        return new Task.Builder()
                .id(taskId)
                .sessionId(sessionId)
                .status(new TaskStatus(SUBMITTED))
                .history(new ArrayList<>())
                .build();
    }

    private void saveTask(Task task) {
        taskStore.save(task);
        if (taskId == null) {
            taskId = task.id();
            sessionId = task.sessionId();
        }
    }
}
