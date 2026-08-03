package com.example.bankapi;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class StreamLambdaHandler
        implements RequestHandler<HttpApiV2ProxyRequest, AwsProxyResponse> {

    private static final SpringBootLambdaContainerHandler<
            HttpApiV2ProxyRequest,
            AwsProxyResponse
            > HANDLER;

    static {
        try {
            HANDLER =
                    SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(
                            BankapiApplication.class
                    );
        } catch (ContainerInitializationException exception) {
            throw new RuntimeException(
                    "Could not initialize Spring Boot application",
                    exception
            );
        }
    }

    @Override
    public AwsProxyResponse handleRequest(
            HttpApiV2ProxyRequest event,
            Context context
    ) {
        return HANDLER.proxy(event, context);
    }
}