package com.example.scheduler.controller;

import com.example.scheduler.model.Event;
import com.example.scheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("/events")
    public void addEvent(@RequestBody Event event) {
        schedulerService.addEvent(event);
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        return schedulerService.getEvents();
    }

    @PutMapping("/events/{id}")
    public void updateEvent(@PathVariable String id, @RequestBody Event event) {
        schedulerService.updateEvent(id, event);
    }

    @DeleteMapping("/events/{id}")
    public void deleteEvent(@PathVariable String id) {
        schedulerService.deleteEvent(id);
    }

    @GetMapping("/conflicts")
    public List<String> getConflicts() {
        return schedulerService.detectConflicts();
    }

    @PostMapping("/suggestions")
    public List<Event> suggestAlternativeSlots(@RequestBody String workingHours) {
        return schedulerService.suggestAlternativeSlots(workingHours);
    }
}