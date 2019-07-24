package chargingsessionstore.controller.responseobject;

public class ChargingSummary {
	
	private Integer startedCount;
	private Integer suspendedCount;
	
	public Integer getStartedCount() {
		return startedCount;
	}
	public void setStartedCount(Integer startedCount) {
		this.startedCount = startedCount;
	}
	public Integer getSuspendedCount() {
		return suspendedCount;
	}
	public void setSuspendedCount(Integer suspendedCount) {
		this.suspendedCount = suspendedCount;
	}
}
