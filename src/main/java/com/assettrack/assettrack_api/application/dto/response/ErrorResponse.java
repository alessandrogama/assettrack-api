package com.assettrack.assettrack_api.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    public static ErrorResponse of(int status, String error, String message,
                                   String path, String details) {
        return new ErrorResponse(LocalDateTime.now(), status,error,message,path,List.of(details));
    }
    public static ErrorResponse of(int status, String error, String message,
                                   String path, List<String> details) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, details);
    }
}
