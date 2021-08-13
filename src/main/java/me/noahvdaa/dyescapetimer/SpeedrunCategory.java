package me.noahvdaa.dyescapetimer;

public enum SpeedrunCategory {
	TUTORIAL("Tutorial%", "[TIP] Please enable sound for the best experience! [Right-Click to start your adventure...]", "Quest completed: Where Adventures Begin II");

	private final String humanName;
	private final String startTrigger;
	private final String endTrigger;

	SpeedrunCategory(String humanName, String startTrigger, String endTrigger){
		this.humanName = humanName;
		this.startTrigger = startTrigger;
		this.endTrigger = endTrigger;
	}

	public String getHumanName() {
		return this.humanName;
	}

	public String getStartTrigger() {
		return this.startTrigger;
	}

	public String getEndTrigger() {
		return this.endTrigger;
	}
}
