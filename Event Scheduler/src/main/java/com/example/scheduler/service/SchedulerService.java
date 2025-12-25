package com.example.scheduler.service;

import com.example.scheduler.model.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerService {

    private List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<String> detectConflicts() {
        List<String> conflicts = new ArrayList<>();
        for (int i = 0; i < events.size() - 1; i++) {
            Event current = events.get(i);
            Event next = events.get(i + 1);
            if (current.getEnd() > next.getStart()) {
                conflicts.add("Conflict between: " + current.getName() + " and " + next.getName());
            }
        }
        return conflicts;
    }

    public List<Event> suggestResolutions() {
        List<Event> resolutions = new ArrayList<>();
        for (int i = 0; i < events.size() - 1; i++) {
            Event current = events.get(i);
            Event next = events.get(i + 1);
            if (current.getEnd() > next.getStart()) {
                int duration = next.getEnd() - next.getStart();
                int newStart = current.getEnd();
                int newEnd = newStart + duration;
                Event resolvedEvent = new Event(next.getName(), Event.toTime(newStart), Event.toTime(newEnd));
                resolutions.add(resolvedEvent);
            }
        }
        return resolutions;
    }
}