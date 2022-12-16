package com.codenotfound.kafka.jpa;

import com.codenotfound.kafka.entity.LibraryEvent;
import org.springframework.data.repository.CrudRepository;


public interface LibraryEventsRepository extends CrudRepository<LibraryEvent,Integer> {
}
