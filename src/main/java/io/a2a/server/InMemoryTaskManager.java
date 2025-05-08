package io.a2a.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.a2a.spec.CancelTaskRequest;
import io.a2a.spec.CancelTaskResponse;
import io.a2a.spec.GetTaskPushNotificationRequest;
import io.a2a.spec.GetTaskPushNotificationResponse;
import io.a2a.spec.GetTaskRequest;
import io.a2a.spec.GetTaskResponse;
import io.a2a.spec.Message;
import io.a2a.spec.PushNotificationConfig;
import io.a2a.spec.SendTaskRequest;
import io.a2a.spec.SendTaskResponse;
import io.a2a.spec.SendTaskStreamingRequest;
import io.a2a.spec.SetTaskPushNotificationRequest;
import io.a2a.spec.SetTaskPushNotificationResponse;
import io.a2a.spec.Task;
import io.a2a.spec.TaskNotFoundError;
import io.a2a.spec.TaskQueryParams;
import io.a2a.spec.TaskResubscriptionRequest;

public class InMemoryTaskManager implements TaskManager {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, PushNotificationConfig> pushNotificationInfos = new ConcurrentHashMap<>();

    @Override
    public GetTaskResponse onGetTask(GetTaskRequest request) {
        TaskQueryParams params = request.getParams();

        Task task = tasks.get(params.id());
        if (task == null) {
            return new GetTaskResponse(request.getId(), new TaskNotFoundError());
        }

        Task result = appendTaskHistory(task, params.historyLength());
        return new GetTaskResponse(request.getId(), result);
    }

    @Override
    public CancelTaskResponse onCancelTask(CancelTaskRequest request) {
        return null;
    }

    @Override
    public SendTaskResponse onSendTask(SendTaskRequest request) {
        return null;
    }

    @Override
    public Object onSendTaskStreamingRequest(SendTaskStreamingRequest request) {
        return null;
    }

    @Override
    public SetTaskPushNotificationResponse onSetTaskPushNotification(SetTaskPushNotificationRequest request) {
        return null;
    }

    @Override
    public GetTaskPushNotificationResponse onGetTaskPushNotification(GetTaskPushNotificationRequest request) {
        return null;
    }

    @Override
    public Object onResubscribeToTask(TaskResubscriptionRequest request) {
        return null;
    }

    protected Task appendTaskHistory(Task task, int historyLength) {
        List<Message> history = new ArrayList<>();
        if (historyLength > 0) {
            int from = task.history().size() - 1 - historyLength;
            if (from < 0) {
                from = 0;
            }
            history = task.history().subList(from, task.history().size());
        }
        Task newTask = new Task(
                task.id(),
                task.sessionId(),
                task.status(),
                task.artifacts(),
                history,
                task.metadata());
        return newTask;
    }
}
