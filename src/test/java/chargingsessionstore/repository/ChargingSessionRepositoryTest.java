package chargingsessionstore.repository;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import chargingsessionstore.entity.ChargingSession;
import chargingsessionstore.entity.ChargingSessionStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ChargingSessionRepositoryTest {
	
	@Autowired
    private ChargingSessionRepository repository;
	
	@After
	public void cleanUp() {
		repository.deleteAll();
	}
	
	@Test
	public void insertAndFindByIdTest() {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession session = new ChargingSession(startedAt);
		repository.save(session);
		
		ChargingSession found = repository.findById(session.getId()).get();	
		assertTrue(found.getId().equals(session.getId()));
		assertTrue(found.getStartedAt().equals(session.getStartedAt()));
		assertTrue(found.getStatus().equals(session.getStatus()));
	}
	
	@Test
	public void insertAndGetStartedCountTest() {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession session = new ChargingSession(startedAt);
		repository.save(session);
		
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minus(1, ChronoUnit.MINUTES);
		Integer startedCount = repository.countChargingSessionsStarted(from, to);
		
		assertTrue(startedCount.equals(1));
	}
	
	@Test
	public void insertAndGetSuspendedCountTest() {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession session = new ChargingSession(startedAt);
		LocalDateTime suspendedAt = LocalDateTime.now();
		session.setSuspendedAt(suspendedAt);
		session.setStatus(ChargingSessionStatus.SUSPENDED);
		repository.save(session);
		
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minus(1, ChronoUnit.MINUTES);
		Integer suspendedCount = repository.countChargingSessionsSuspended(from, to);
		
		assertTrue(suspendedCount.equals(1));
	}
}
