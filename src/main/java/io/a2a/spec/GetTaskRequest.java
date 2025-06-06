package io.a2a.spec;

import static io.a2a.spec.A2A.GET_TASK_METHOD;
import static io.a2a.spec.A2A.JSONRPC_VERSION;
import static io.a2a.util.Utils.defaultIfNull;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.a2a.util.Assert;

/**
 * A get task request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class GetTaskRequest extends JSONRPCRequest<TaskQueryParams> {

    @JsonCreator
    public GetTaskRequest(@JsonProperty("jsonrpc") String jsonrpc, @JsonProperty("id") Object id,
                          @JsonProperty("method") String method, @JsonProperty("params") TaskQueryParams params) {
        Assert.checkNotNullParam("method", method);
        Assert.checkNotNullParam("params", params);

        if (! method.equals(GET_TASK_METHOD)) {
            throw new IllegalArgumentException("Invalid GetTaskRequest method");
        }

        this.jsonrpc = defaultIfNull(jsonrpc, JSONRPC_VERSION);
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.method = method;
        this.params = params;
    }

    public GetTaskRequest(Object id, TaskQueryParams params) {
        this(null, id, GET_TASK_METHOD, params);
    }


    public static class Builder {
        private String jsonrpc;
        private Object id;
        private String method = "tasks/get";
        private TaskQueryParams params;

        public GetTaskRequest.Builder jsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
            return this;
        }

        public GetTaskRequest.Builder id(Object id) {
            this.id = id;
            return this;
        }

        public GetTaskRequest.Builder method(String method) {
            this.method = method;
            return this;
        }

        public GetTaskRequest.Builder params(TaskQueryParams params) {
            this.params = params;
            return this;
        }

        public GetTaskRequest build() {
            return new GetTaskRequest(jsonrpc, id, method, params);
        }
    }
}
