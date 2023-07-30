package f21as.group2.model;

// This class stores the attributes and methods. It connects segments, phases and directions.
public class Segment {
	// Instance Variables
	private String segNum;
	private String phaseNum;
	private String direction;

	// Constructor
	public Segment(String segNum, String phaseNum, String direction) {
		this.segNum = segNum;
		this.phaseNum = phaseNum;
		this.direction = direction;
	}

	// Getters and setters
	public String getSegNum() {
		return segNum;
	}

	public void setSegNum(String segNum) {
		this.segNum = segNum;
	}

	public String getPhaseNumFromSeg() {
		return phaseNum;
	}

	public void setPhaseNum(String phaseNum) {
		this.phaseNum = phaseNum;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
