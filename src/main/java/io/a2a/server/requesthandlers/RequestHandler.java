package io.a2a.server.requesthandlers;

import java.util.concurrent.Flow;

import io.a2a.spec.CancelTaskResponse;
import io.a2a.spec.GetTaskPushNotificationRequest;
import io.a2a.spec.GetTaskPushNotificationResponse;
import io.a2a.spec.GetTaskRequest;
import io.a2a.spec.GetTaskResponse;
import io.a2a.spec.SendTaskRequest;
import io.a2a.spec.SendTaskResponse;
import io.a2a.spec.SendTaskStreamingRequest;
import io.a2a.spec.SendTaskStreamingResponse;
import io.a2a.spec.SetTaskPushNotificationRequest;
import io.a2a.spec.SetTaskPushNotificationResponse;
import io.a2a.spec.TaskResubscriptionRequest;

public interface RequestHandler {
    GetTaskResponse onGetTask(GetTaskRequest request);

    CancelTaskResponse onCancelTask(GetTaskRequest request);

    // TODO - These Response and Request classes don't exist yet
    // SendMessageResponse onMessageSend(SendMessageRequest request);
    // SendStreamingMessageResponse onMessageSendStream(SendStreamingMessageRequest request);

    // TODO - Move to new SendMessageXXX classes
    SendTaskResponse onMessageSend(SendTaskRequest request);

    // TODO - Move to new SendStreamingMessageXXX classes
    SendTaskStreamingResponse onMessageSendStream(SendTaskStreamingRequest request);

    SetTaskPushNotificationResponse onSetTaskPushNotificationConfig(SetTaskPushNotificationRequest request);

    GetTaskPushNotificationResponse onGetTaskPushNotificationConfig(GetTaskPushNotificationRequest request);

    // TODO - Move to new SendStreamingMessageResponse (there is also some async stuff for the return value)
    Flow.Publisher<SendTaskStreamingResponse> onResubscribeToTask(TaskResubscriptionRequest request);
}
