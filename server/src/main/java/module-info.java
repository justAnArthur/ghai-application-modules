module server {
  requires java.sql;
  requires io.grpc;
  requires io.grpc.stub;
  requires io.grpc.protobuf;

  requires annotations.api;
  requires protobuf.java;
  requires com.google.common;
  requires jjwt;
  requires org.slf4j;
  requires java.dotenv;

  exports fiit.vava.server;
  exports fiit.vava.server.config;
}
