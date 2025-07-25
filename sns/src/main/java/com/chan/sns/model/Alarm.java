package com.chan.sns.model;

import com.chan.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity alarmEntity) {
        return new Alarm(
                alarmEntity.getId(),
                alarmEntity.getAlarmType(),
                alarmEntity.getArgs(),
                alarmEntity.getRegisterAt(),
                alarmEntity.getUpdatedAt(),
                alarmEntity.getDeletedAt()
        );
    }
}
