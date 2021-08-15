package me.noahvdaa.dyescapetimer;

public enum SpeedrunCategory {
	ANY("Any%", "when you complete the quest Where Adventures Begin V", SpeedrunCategory.startPhrase, "^Quest completed: Where Adventures Begin V$"),
	DEATH("Death%", "when you die", SpeedrunCategory.startPhrase, "^You lost [0-9]+ exp points!$"),
	TUTORIAL("Tutorial%", "when you complete the quest Where Adventures Begin II", SpeedrunCategory.startPhrase, "^Quest completed: Where Adventures Begin II$");

	private static final String startPhrase = "^\\[TIP\\] Please enable sound for the best experience! \\[Right-Click to start your adventure\\.\\.\\.\\]$";

	private final String humanName;
	private final String humanEndTrigger;
	private final String startTrigger;
	private final String endTrigger;

	SpeedrunCategory(String humanName, String humanEndTrigger, String startTrigger, String endTrigger) {
		this.humanName = humanName;
		this.humanEndTrigger = humanEndTrigger;
		this.startTrigger = startTrigger;
		this.endTrigger = endTrigger;
	}

	public String getHumanName() {
		return this.humanName;
	}

	public String getHumanEndTrigger() {
		return this.humanEndTrigger;
	}

	public String getStartTrigger() {
		return this.startTrigger;
	}

	public String getEndTrigger() {
		return this.endTrigger;
	}
}
