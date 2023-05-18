package net.quazar.apigateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import net.quazar.apigateway.entity.ApiError;

import java.io.IOException;
import java.io.InputStream;

public class ApiGatewayErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ApiError apiError;
        if (response.status() >= 400 && response.status() <= 499) {
            try (InputStream bodyIs = response.body()
                    .asInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                apiError = mapper.readValue(bodyIs, ApiError.class);
            } catch (IOException e) {
                return new Exception(e.getMessage());
            }
            return switch (response.status()) {
                case 400 -> new BadRequestException(apiError.getMessage() != null ? apiError.getMessage() : "Bad Request",
                        apiError.getTimestamp());
                case 401 -> new UnauthorizedException(apiError.getMessage() != null ? apiError.getMessage() : "Unauthorized",
                        apiError.getTimestamp());
                case 404 -> new NotFoundException(apiError.getMessage() != null ? apiError.getMessage() : "Not found",
                        apiError.getTimestamp());
                default -> errorDecoder.decode(methodKey, response);
            };
        }
        return errorDecoder.decode(methodKey, response);
    }
}
