package com.assettrack.assettrack_api.application.usecase.alert;

import com.assettrack.assettrack_api.domain.entity.Alert;

public interface AlertNotificationPort {
    void notify(Alert alert);
}
