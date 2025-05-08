package io.a2a.server;

import io.a2a.spec.CancelTaskRequest;
import io.a2a.spec.CancelTaskResponse;
import io.a2a.spec.GetTaskPushNotificationRequest;
import io.a2a.spec.GetTaskPushNotificationResponse;
import io.a2a.spec.GetTaskRequest;
import io.a2a.spec.GetTaskResponse;
import io.a2a.spec.SendTaskRequest;
import io.a2a.spec.SendTaskResponse;
import io.a2a.spec.SendTaskStreamingRequest;
import io.a2a.spec.SetTaskPushNotificationRequest;
import io.a2a.spec.SetTaskPushNotificationResponse;
import io.a2a.spec.TaskResubscriptionRequest;

public interface TaskManager {
    GetTaskResponse onGetTask(GetTaskRequest request);

    CancelTaskResponse onCancelTask(CancelTaskRequest request);

    SendTaskResponse onSendTask(SendTaskRequest request);

    // TODO Python return type is: AsyncIterable[SendTaskStreamingResponse] | JSONRPCResponse
    Object onSendTaskStreamingRequest(SendTaskStreamingRequest request);

    SetTaskPushNotificationResponse onSetTaskPushNotification(SetTaskPushNotificationRequest request);

    GetTaskPushNotificationResponse onGetTaskPushNotification(GetTaskPushNotificationRequest request);

    // TODO Python return type is: AsyncIterable[SendTaskResponse] | JSONRPCResponse
    Object onResubscribeToTask(TaskResubscriptionRequest request);
}
