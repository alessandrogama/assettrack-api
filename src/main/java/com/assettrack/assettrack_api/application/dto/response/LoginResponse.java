package com.assettrack.assettrack_api.application.dto.response;

import org.hibernate.validator.internal.util.logging.Log;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        String username,
        String role
) {
    public  static LoginResponse of(String accessToken, String refreshToken,
                                    long expiresIn, String username, String role) {
        return new LoginResponse(accessToken, refreshToken,"Bearer", expiresIn, username, role);
    }
}
