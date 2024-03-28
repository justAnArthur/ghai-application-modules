package fiit.vava.server;

import fiit.vava.server.config.Constants;
import fiit.vava.server.services.SkipAuthorization;
import fiit.vava.server.services.UserService;
import io.grpc.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.lang.reflect.Method;

public class AuthorizationServerInterceptor implements ServerInterceptor {

    private final JwtParser parser = Jwts.parser().setSigningKey(Constants.JWT_SIGNING_KEY);

    private final UserService userService;

    public AuthorizationServerInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        logRoute(serverCall.getMethodDescriptor().getFullMethodName());

        if (!checkAuthorization(serverCall.getMethodDescriptor().getFullMethodName()))
            return Contexts.interceptCall(Context.current(), serverCall, metadata, serverCallHandler);

        String value = metadata.get(Constants.AUTHORIZATION_METADATA_KEY);

        Status status;
        if (value == null)
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
        else if (!value.startsWith(Constants.BEARER_TYPE))
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
        else {
            try {
                String token = value.substring(Constants.BEARER_TYPE.length()).trim();
                Jws<Claims> claims = parser.parseClaimsJws(token);

                User user = userService.authorize(
                        claims.getBody().getSubject(),
                        (String) claims.getBody().get("password")
                );

                Context ctx = Context.current().withValue(Constants.USER_CONTEXT, user);
                return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
            } catch (Exception e) {
                status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
            }
        }

        serverCall.close(status, metadata);
        return new ServerCall.Listener<>() {
        };
    }

    /**
     * @return true - if authorization is required
     */
    private boolean checkAuthorization(String methodName) {
        try {
            Class<?> clazz = Class.forName(methodName.split("/")[0]);
            String methodNameWithoutPackage = methodName.split("/")[1];

            Method method = clazz.getMethod(methodNameWithoutPackage);
            return !method.isAnnotationPresent(SkipAuthorization.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return true;
        }
    }

    private void logRoute(String methodName) {
        System.out.println(methodName);
    }
}