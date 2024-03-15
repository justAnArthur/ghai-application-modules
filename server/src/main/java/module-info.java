module server {
    requires io.grpc;
    requires io.grpc.stub;
    requires io.grpc.protobuf;

    requires annotations.api;

    requires protobuf.java;
    requires com.google.common;
    requires jjwt;

    exports fiit.vava.server;
    exports fiit.vava.server.config;
}