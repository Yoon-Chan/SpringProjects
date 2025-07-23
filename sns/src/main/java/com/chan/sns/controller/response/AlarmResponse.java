package com.chan.sns.controller.response;

import com.chan.sns.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class AlarmResponse {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarmEntity) {
        return new AlarmResponse(
                alarmEntity.getId(),
                alarmEntity.getAlarmType(),
                alarmEntity.getArgs(),
                alarmEntity.getAlarmType().getAlarmText(),
                alarmEntity.getRegisteredAt(),
                alarmEntity.getUpdatedAt(),
                alarmEntity.getDeletedAt()
        );
    }
}
