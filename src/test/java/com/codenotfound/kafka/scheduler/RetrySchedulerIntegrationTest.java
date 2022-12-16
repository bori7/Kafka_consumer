package com.codenotfound.kafka.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.codenotfound.kafka.config.LibraryEventsConsumerConfig;
import com.codenotfound.kafka.entity.FailureRecord;
import com.codenotfound.kafka.jpa.FailureRecordRepository;
import com.codenotfound.kafka.service.LibraryEventsService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import lombok.experimental.var;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class RetrySchedulerIntegrationTest {

    @SpyBean
    LibraryEventsService libraryEventsServiceSpy;

    @Autowired
    RetryScheduler retryScheduler;

    @Autowired
    FailureRecordRepository failureRecordRepository;

    @BeforeEach
    public void setUp(){

        failureRecordRepository.deleteAll();

        var record = "{\"libraryEventId\":1,\"book\":{\"bookId\":456,\"bookName\":\"Kafka Using Spring Boot 2.X\",\"bookAuthor\":\"Dilip\"}}";

        var failureRecord = new FailureRecord(null,"library-events", 123, record,1,0L, "exception occurred", LibraryEventsConsumerConfig.RETRY);
        var failureRecord1= new FailureRecord(null,"library-events", 123, record,1,1L, "exception occurred",LibraryEventsConsumerConfig.DEAD);

        failureRecordRepository.saveAll(new ArrayList<>(Arrays.asList(new FailureRecord[]{failureRecord, failureRecord1})));
    }



    @Test
    @Disabled
    public void retryFailedRecords() throws JsonProcessingException {

        retryScheduler.retryFailedRecords();

        Mockito.verify(libraryEventsServiceSpy, times(1) ).processLibraryEvent(Mockito.isA(ConsumerRecord.class));
    }
}
