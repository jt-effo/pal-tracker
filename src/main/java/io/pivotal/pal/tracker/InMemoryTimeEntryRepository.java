package io.pivotal.pal.tracker;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> repository = new HashMap<>();
    private AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(atomicLong.getAndIncrement());
        repository.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return repository.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        if (find(id) == null) {
            return null;
        }

        timeEntry.setId(id);
        repository.put(id, timeEntry);
        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        repository.remove(id);
    }
}
