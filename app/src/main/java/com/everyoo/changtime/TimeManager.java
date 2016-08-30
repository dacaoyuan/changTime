package com.everyoo.changtime;

public class TimeManager{
	private static TimeManager manager;
	private TimeManager() {

	}
	public static TimeManager instance() {
		if (manager == null) {
			manager = new TimeManager();
		}
		return manager;
	}
	
	/**
	 *  由年、月确定天的最大数
	 * @param year
	 * @param month
	 * @return
	 */
	private int getMaxDay(int year, int month) {
		int day = 0;
		boolean isRen = true;
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			isRen = true;
		} else {
			isRen = false;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			day = 30;
			break;
		default:
			if (isRen == true) {
				day = 29;
			} else {
				day = 28;
			}
			break;
		}
		return day;
	}

	public TimerEntity running(TimerEntity entity) {
		if (entity == null)
			return null;
		if (entity.second < 59) {
			entity.second++;
		} else {
			entity.second = 0;
			addMinute(entity);
		}
		return entity;
	}

	private void addMinute(TimerEntity entity) {
		if (entity.minute < 59) {
			entity.minute++;
		} else {
			entity.minute = 0;
			addHour(entity);
		}
	}

	private void addHour(TimerEntity entity) {
		if (entity.hour < 23) {
			entity.hour++;
		} else {
			entity.hour = 0;
			addDay(entity);
		}
	}

	private void addDay(TimerEntity entity) {
		if (entity.day < getMaxDay(entity.year, entity.month)) {
			entity.day++;
		} else {
			entity.day = 1;
			addMonthAndYear(entity);
		}
	}
	
	private void addMonthAndYear(TimerEntity entity) {
		if (entity.month < 12) {
			entity.month++;
		} else {
			entity.month = 1;
			entity.year++;
		}
	}
}
