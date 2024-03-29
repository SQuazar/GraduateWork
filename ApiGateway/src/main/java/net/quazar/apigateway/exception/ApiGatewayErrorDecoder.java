package net.quazar.apigateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import net.quazar.apigateway.entity.ApiError;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

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
                case 403 -> new BadUserCredentialsException(apiError.getMessage() != null ? apiError.getMessage() : "Bad Credentials",
                        apiError.getTimestamp());
                case 404 -> new NotFoundException(apiError.getMessage() != null ? apiError.getMessage() : "Not found",
                        apiError.getTimestamp());
                case 423 -> new LockedException(apiError.getMessage() != null ? apiError.getMessage() : "Locked",
                        apiError.getTimestamp());
                default -> errorDecoder.decode(methodKey, response);
            };
        }
        if (response.status() == 503)
            return new ServiceUnavailableException("Service Unavailable", LocalDateTime.now().toString());
        return errorDecoder.decode(methodKey, response);
    }
}
