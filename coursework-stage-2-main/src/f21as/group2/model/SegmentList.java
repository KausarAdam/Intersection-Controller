package f21as.group2.model;

import java.util.ArrayList;

// Array List for segments
public class SegmentList {

	// Instance Variable
	private ArrayList<Segment> segmentList;

	// Constructor
	public SegmentList() {
		// Data Added to the Segment List
		this.segmentList = new ArrayList<Segment>();
		segmentList.add(new Segment("S1", "P1", "Right"));
		segmentList.add(new Segment("S1", "P1", "Straight"));
		segmentList.add(new Segment("S1", "P2", "Left"));
		segmentList.add(new Segment("S2", "P3", "Right"));
		segmentList.add(new Segment("S2", "P3", "Straight"));
		segmentList.add(new Segment("S2", "P4", "Left"));
		segmentList.add(new Segment("S3", "P5", "Right"));
		segmentList.add(new Segment("S3", "P5", "Straight"));
		segmentList.add(new Segment("S3", "P6", "Left"));
		segmentList.add(new Segment("S4", "P7", "Right"));
		segmentList.add(new Segment("S4", "P7", "Straight"));
		segmentList.add(new Segment("S4", "P8", "Left"));
	}

	// Getter & Setter
	public ArrayList<Segment> getSegmentList() {
		return segmentList;
	}

	public void setSegmentList(ArrayList<Segment> segmentList) {
		this.segmentList = segmentList;
	}

}
