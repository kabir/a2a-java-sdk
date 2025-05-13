package io.a2a.server;

import java.util.List;

import io.a2a.spec.GetTaskRequest;
import io.a2a.spec.Message;
import io.a2a.spec.Part;
import io.a2a.spec.Task;
import io.a2a.spec.TaskQueryParams;
import io.a2a.spec.TaskState;
import io.a2a.spec.TaskStatus;
import io.a2a.spec.TextPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskManagerTest {

    private static TaskManager manager;

    @BeforeEach
    public static void setupTaskManager() {
        manager = new InMemoryTaskManager();
//
//        String id = "testing";
//        List<Part<?>> parts = List.of(new TextPart("Hello world"));
//        Message message = new Message.Builder()
//                .role(Message.Role.USER)
//                .parts(List.of(new TextPart("Hello world")))
//                .build();
//        TaskSendParams params = new TaskSendParams.Builder()
//                .id(id)
//                .message(message)
//                .build();
//        SendTaskResponse response = manager.onSendTask(
//                new SendTaskRequest.Builder()
//                        .id(id)
//                        .params(params)
//                        .build());
    }

    @Test
    public void testGetTask() {
        //TaskQueryParams taskQueryParams = new TaskQueryParams.

        manager.onGetTask(
                new GetTaskRequest.Builder()
                        .params()
                        .build());
    }


    private static class TestTaskManager extends InMemoryTaskManager {
        void addTask(Task task) {
            super.addTask(task);
        }

        void addTask(Object id, String text) {
            List<Part<?>> parts = List.of(new TextPart(text));
            Message message = new Message.Builder()
                    .role(Message.Role.USER)
                    .parts(parts)
                    .build();
            Task task = new Task.Builder()
                    .id(id.toString())
                    .history(List.of(message))
                    .status(new TaskStatus(TaskState.SUBMITTED))
                    .build();

            addTask(task);
        }
    }
}
