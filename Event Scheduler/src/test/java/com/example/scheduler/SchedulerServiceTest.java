import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.scheduler.model.Event;
import com.example.scheduler.service.SchedulerService;

import java.util.ArrayList;
import java.util.List;

public class SchedulerServiceTest {

    private SchedulerService schedulerService;

    @BeforeEach
    public void setUp() {
        schedulerService = new SchedulerService();
    }

    @Test
    public void testAddEvent() {
        Event event = new Event("Meeting", "09:00", "10:00");
        schedulerService.addEvent(event);
        List<Event> events = schedulerService.getEvents();
        assertEquals(1, events.size());
        assertEquals("Meeting", events.get(0).getName());
    }

    @Test
    public void testConflictDetection() {
        Event event1 = new Event("Meeting", "09:00", "10:00");
        Event event2 = new Event("Workshop", "09:30", "11:00");
        schedulerService.addEvent(event1);
        schedulerService.addEvent(event2);
        
        List<Event> conflicts = schedulerService.detectConflicts();
        assertEquals(1, conflicts.size());
        assertEquals("Meeting", conflicts.get(0).getName());
        assertEquals("Workshop", conflicts.get(1).getName());
    }

    @Test
    public void testResolveConflict() {
        Event event1 = new Event("Meeting", "09:00", "10:00");
        Event event2 = new Event("Workshop", "09:30", "11:00");
        schedulerService.addEvent(event1);
        schedulerService.addEvent(event2);
        
        List<Event> resolvedEvents = schedulerService.resolveConflicts();
        assertEquals(2, resolvedEvents.size());
        assertNotEquals(event2.getStart(), resolvedEvents.get(1).getStart());
    }

    @Test
    public void testGetEventsWithinWorkingHours() {
        Event event1 = new Event("Meeting", "09:00", "10:00");
        Event event2 = new Event("Lunch", "12:00", "13:00");
        schedulerService.addEvent(event1);
        schedulerService.addEvent(event2);
        
        List<Event> workingHoursEvents = schedulerService.getEventsWithinWorkingHours("08:00", "17:00");
        assertEquals(2, workingHoursEvents.size());
    }
}