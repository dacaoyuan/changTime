package com.everyoo.changtime;

import java.io.Serializable;
import java.util.Calendar;

@SuppressWarnings("serial")
public class TimerEntity implements Serializable {

    public int year, month, day, hour, minute, second;
    private static TimerEntity entity;

    private TimerEntity() {

    }

    public static TimerEntity instance(Calendar calendar) {
        if (entity == null) {
            entity = new TimerEntity();
        }
        entity.year = calendar.get(Calendar.YEAR);
        entity.month = calendar.get(Calendar.MONTH) + 1;
        entity.day = calendar.get(Calendar.DAY_OF_MONTH);
        entity.hour = calendar.get(Calendar.HOUR_OF_DAY);
        entity.minute = calendar.get(Calendar.MINUTE);
        entity.second = calendar.get(Calendar.SECOND);
        return entity;
    }

}
