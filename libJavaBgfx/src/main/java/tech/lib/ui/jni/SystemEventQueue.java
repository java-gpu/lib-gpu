package tech.lib.ui.jni;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.lib.ui.event.AppEvent;

import java.util.Deque;
import java.util.LinkedList;

@Data
@Slf4j
public class SystemEventQueue {
    private static final int DEFAULT_MAX_EVENT_TO_KEEP = 50;
    private int maxEventToBeKept;
    private final Deque<AppEvent> eventList = new LinkedList<>();

    public SystemEventQueue() {
        this(DEFAULT_MAX_EVENT_TO_KEEP);
    }

    public SystemEventQueue(int maxEventToBeKept) {
        this.maxEventToBeKept = maxEventToBeKept;
    }

    public void addLast(AppEvent event) {
        eventList.addLast(event);
        if (eventList.size() > maxEventToBeKept) {
            var dropped = pollEvent();
            if (dropped != null) {
                log.debug("Dropped a very old event: {}", dropped);
            }
        }
    }

    public AppEvent pollEvent() {
        return eventList.poll();
    }

    public void resetQueue() {
        eventList.clear();
    }
}
