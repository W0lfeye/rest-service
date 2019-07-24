package chargingsessionstore.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargingSession {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	
	private LocalDateTime startedAt;
	private LocalDateTime suspendedAt;
	
	@Enumerated(EnumType.STRING)
	private ChargingSessionStatus status;
	
	protected ChargingSession() {}
	
	public ChargingSession(LocalDateTime startedAt) {
		this.startedAt = startedAt;
		status = ChargingSessionStatus.STARTED;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDateTime getSuspendedAt() {
		return suspendedAt;
	}

	public void setSuspendedAt(LocalDateTime suspendedAt) {
		this.suspendedAt = suspendedAt;
	}

	public ChargingSessionStatus getStatus() {
		return status;
	}

	public void setStatus(ChargingSessionStatus status) {
		this.status = status;
	}

	public UUID getId() {
		return id;
	}
	
}
