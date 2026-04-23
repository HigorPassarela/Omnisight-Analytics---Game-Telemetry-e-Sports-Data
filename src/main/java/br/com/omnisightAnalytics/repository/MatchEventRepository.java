package br.com.omnisightAnalytics.repository;

import br.com.omnisightAnalytics.domain.MatchEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchEventRepository extends MongoRepository<MatchEvent, String>, MatchEventRepositoryCustom {
}
