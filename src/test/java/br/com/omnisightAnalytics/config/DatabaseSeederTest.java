package br.com.omnisightAnalytics.config;

import br.com.omnisightAnalytics.config.DatabaseSeeder;
import br.com.omnisightAnalytics.domain.MatchEvent;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseSeederTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @InjectMocks
    private DatabaseSeeder databaseSeeder;

    @Captor
    private ArgumentCaptor<Collection<MatchEvent>> batchCaptor;

    @BeforeEach
    void setUp() {
        lenient().when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
    }

    @Test
    void shouldNotSeedDataWhenDatabaseIsNotEmpty() {
        when(mongoCollection.countDocuments()).thenReturn(150L);

        databaseSeeder.run();

        verify(mongoTemplate, never()).insertAll(any());
    }

    @Test
    void shouldSeedDataWhenDatabaseIsEmpty() {
        when(mongoCollection.countDocuments()).thenReturn(0L);

        databaseSeeder.run();

        verify(mongoTemplate, times(20)).insertAll(batchCaptor.capture());

        List<Collection<MatchEvent>> capturedBatches = batchCaptor.getAllValues();
        assertEquals(20, capturedBatches.size());
        assertEquals(10000, capturedBatches.get(0).size());
    }
}