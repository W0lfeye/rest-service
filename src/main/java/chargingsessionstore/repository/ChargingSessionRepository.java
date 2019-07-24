package chargingsessionstore.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import chargingsessionstore.entity.ChargingSession;

public interface ChargingSessionRepository extends CrudRepository<ChargingSession, UUID>{

	@Query(value = "select count(cs) from ChargingSession cs where cs.startedAt >= ?1 and cs.startedAt <= ?2 and cs.status = 'STARTED'")
	Integer countChargingSessionsStarted(LocalDateTime from, LocalDateTime to);
	
	@Query(value = "select count(cs) from ChargingSession cs where cs.suspendedAt >= ?1 and cs.suspendedAt <= ?2 and cs.status = 'SUSPENDED'")
	Integer countChargingSessionsSuspended(LocalDateTime from, LocalDateTime to);
}
