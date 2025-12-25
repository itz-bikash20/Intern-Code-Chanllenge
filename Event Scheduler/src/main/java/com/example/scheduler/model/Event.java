package com.example.scheduler.model;

public class Event {
    private String name;
    private int start;
    private int end;

    public Event(String name, String startTime, String endTime) {
        this.name = name;
        this.start = toMinutes(startTime);
        this.end = toMinutes(endTime);
    }

    public String getName() {
        return name;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public static int toMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public static String toTime(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        return String.format("%02d:%02d", h, m);
    }
}