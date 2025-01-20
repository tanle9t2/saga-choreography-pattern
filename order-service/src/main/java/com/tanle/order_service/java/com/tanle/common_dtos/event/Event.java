package com.tanle.order_service.java.com.tanle.common_dtos.event;

import java.util.Date;
import java.util.UUID;

public interface Event {

    UUID getEventId();

    Date getDate();

}
