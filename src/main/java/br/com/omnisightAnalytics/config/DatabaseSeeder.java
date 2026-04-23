package br.com.omnisightAnalytics.config;

import br.com.omnisightAnalytics.domain.Coordinates;
import br.com.omnisightAnalytics.domain.MatchEvent;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final MongoTemplate mongoTemplate;
    private final Faker faker;
    private final Random random;

    public DatabaseSeeder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.faker = new Faker();
        this.random = new Random();
    }

    @Override
    public void run(String... args) {
        long count = mongoTemplate.getCollection("match_events").countDocuments();
        if (count > 0) {
            log.info("O banco já possui {} registros. Seeder ignorado.", count);
            return;
        }

        log.info("Iniciando a geração de dados. Por favor, aguarde...");

        int totalRecords = 200_000; // Reduzido para poupar armazenamento
        int batchSize = 10_000;
        int batches = totalRecords / batchSize;

        // Gerando um pool menor de IDs para termos dados mais "repetidos" e densos
        List<String> matchIds = generateIds(2_000);
        List<String> playerIds = generateIds(5_000);
        String[] eventTypes = {"kill", "death", "assist", "item_buy", "objective_capture"};

        for (int i = 0; i < batches; i++) {
            List<MatchEvent> batch = new ArrayList<>(batchSize);

            for (int j = 0; j < batchSize; j++) {
                String matchId = matchIds.get(random.nextInt(matchIds.size()));
                String playerId = playerIds.get(random.nextInt(playerIds.size()));
                String eventType = eventTypes[random.nextInt(eventTypes.length)];

                Instant timestamp = Instant.now().minus(random.nextInt(30 * 24), ChronoUnit.HOURS);

                Coordinates coords = new Coordinates(
                        Math.round(random.nextDouble() * 1000.0 * 100.0) / 100.0,
                        Math.round(random.nextDouble() * 1000.0 * 100.0) / 100.0
                );

                MatchEvent event = new MatchEvent(matchId, playerId, eventType, timestamp, coords);
                batch.add(event);
            }

            mongoTemplate.insertAll(batch);
            log.info("Lote {}/{} inserido com sucesso ({} registros totais).", (i + 1), batches, (i + 1) * batchSize);
        }

        log.info("✅ Seeder finalizado! 200.000 registros inseridos no MongoDB.");
    }

    private List<String> generateIds(int amount) {
        List<String> ids = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            ids.add(UUID.randomUUID().toString());
        }
        return ids;
    }
}