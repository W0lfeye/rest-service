package chargingsessionstore.controller.requestobject;

import java.time.LocalDateTime;

public class ChargingSessionLocalDateTimes {
	
	private LocalDateTime startedAt;
	private LocalDateTime suspendedAt;
	
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
}
