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
import io.a2a.spec.InternalError;
import io.a2a.spec.JSONRPCResponse;
import io.a2a.spec.Message;
import io.a2a.spec.PushNotificationConfig;
import io.a2a.spec.SendTaskRequest;
import io.a2a.spec.SendTaskResponse;
import io.a2a.spec.SendTaskStreamingRequest;
import io.a2a.spec.SetTaskPushNotificationRequest;
import io.a2a.spec.SetTaskPushNotificationResponse;
import io.a2a.spec.Task;
import io.a2a.spec.TaskIdParams;
import io.a2a.spec.TaskNotCancelableError;
import io.a2a.spec.TaskNotFoundError;
import io.a2a.spec.TaskPushNotificationConfig;
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

        synchronized (task) {
            Task result = appendTaskHistory(task, params.historyLength());
            return new GetTaskResponse(request.getId(), result);
        }
    }

    @Override
    public CancelTaskResponse onCancelTask(CancelTaskRequest request) {
        TaskIdParams params = request.getParams();
        Task task = tasks.get(params.id());
        if (task == null) {
            return new CancelTaskResponse(request.getId(), new TaskNotFoundError());
        }



        return new CancelTaskResponse(request.getId(), new TaskNotCancelableError());

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
        TaskPushNotificationConfig params = request.getParams();
        try {
            setPushNotificationInfo(params.id(), params.pushNotificationConfig());
        } catch (Exception e) {
            return new SetTaskPushNotificationResponse(
                    request.getId(),
                    new InternalError("An error occurred while setting push notification info"));
        }
        return new SetTaskPushNotificationResponse(request.getId(), params);
    }

    @Override
    public GetTaskPushNotificationResponse onGetTaskPushNotification(GetTaskPushNotificationRequest request) {
        TaskIdParams params = request.getParams();
        PushNotificationConfig config;
        try {
            config = getPushNotificationInfo(params.id());
        } catch (Exception e) {
            return new GetTaskPushNotificationResponse(
                    params.id(),
                    new InternalError("An error occurred while getting push notification info"));
        }

        return new GetTaskPushNotificationResponse(params.id(), new TaskPushNotificationConfig(params.id(), config));
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
        Task newTask = new Task.Builder(task)
                .history(history)
                .build();
        return newTask;
    }

    protected PushNotificationConfig setPushNotificationInfo(String taskId, PushNotificationConfig notificationConfig) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalStateException("No task found for " + taskId);
        }
        return pushNotificationInfos.put(taskId, notificationConfig);
    }

    protected PushNotificationConfig getPushNotificationInfo(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalStateException("Task not found for " + taskId);
        }
        return pushNotificationInfos.get(taskId);
    }


}
