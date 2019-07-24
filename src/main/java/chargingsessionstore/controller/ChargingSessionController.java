package chargingsessionstore.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import chargingsessionstore.controller.requestobject.ChargingSessionLocalDateTimes;
import chargingsessionstore.controller.responseobject.ChargingSummary;
import chargingsessionstore.entity.ChargingSession;
import chargingsessionstore.entity.ChargingSessionStatus;
import chargingsessionstore.repository.ChargingSessionRepository;

@RestController
public class ChargingSessionController {

	@Autowired
	private ChargingSessionRepository repository;
	
	@RequestMapping(value = "/chargingSessions", method = RequestMethod.POST)
	public ChargingSession chargingSessions(@RequestBody ChargingSessionLocalDateTimes requestObject) {
		ChargingSession newSession = new ChargingSession(requestObject.getStartedAt());
		repository.save(newSession);
		return newSession;
	}
	
	@RequestMapping(value = "/chargingSessions/{id}", method = RequestMethod.PUT)
	public ChargingSession chargingSessions(@PathVariable UUID id, @RequestBody ChargingSessionLocalDateTimes requestObject) {
		ChargingSession session = repository.findById(id).get();
		session.setSuspendedAt(requestObject.getSuspendedAt());
		session.setStatus(ChargingSessionStatus.SUSPENDED);
		repository.save(session);
		return session;
	}
	
	@RequestMapping(value = "/chargingSessions/{id}", method = RequestMethod.GET)
	public ChargingSession chargingSessions(@PathVariable UUID id) {
		return repository.findById(id).get();
	}
	
	@RequestMapping(value = "/chargingSummary", method = RequestMethod.GET)
	public ChargingSummary chargingSummary() {
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minus(1, ChronoUnit.MINUTES);
		Integer startedCount = repository.countChargingSessionsStarted(from, to);
		Integer suspendedCount = repository.countChargingSessionsSuspended(from, to);
		
		ChargingSummary result = new ChargingSummary();
		result.setStartedCount(startedCount);
		result.setSuspendedCount(suspendedCount);
		return result;
	}
}
