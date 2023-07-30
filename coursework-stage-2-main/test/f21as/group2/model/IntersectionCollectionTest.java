package f21as.group2.model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntersectionCollectionTest {

	// To create test variables
	private static IntersectionCollection v = new IntersectionCollection();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// To read the file with test data
		v.readFile("TestVehicles.csv");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	// Links to waitingToCrossPerSeg(String)
	@Test
	final void testWaitingToCrossPerSeg() {
		assertEquals("Number of vehicles waiting to cross in S1 must be 2.", 2, v.waitingToCrossPerSeg("S1"));
	}

	// Links to totalLengthPerSeg(String)
	@Test
	final void testTotalLengthPerSeg() {
		assertEquals("Total Length of S2 must be 12.", 12, v.totalLengthPerSeg("S2"));
	}

	// Links to avgCrossTimePerSeg(String)
	@Test
	final void testAvgCrossTimePerSeg() {
		// (10+8)/2 = 9
		assertEquals("Average crossing time for S3 must be 9.", "9.0", String.valueOf(v.avgCrossTimePerSeg("S3")));
	}

	// Links to emissionPerSeg(String)
	@Test
	final void testEmissionPerSeg() {
		// 1 car with emission 5 g/min and 1 bus with emission 10 g/min
		// total wait per segment = 15+20+15+20+15=20 = 105
		// final in grams/sec -> (((1*5) + (1*10))/60)*105 = 26.25
		assertEquals("Total Emissions for S1 must be 26.25.", "26.25", String.valueOf(v.emissionPerSeg("S1")));
	}

	// Links to totalEmissions()
	@Test
	final void testTotalEmissions() {
		// 26.25 + 35 + 35 + 26.25 = 122.5
		assertEquals("Total Emissions for all segments must be 122.5.", "122.5", String.valueOf(v.totalEmissions()));
	}

	// Links to totalWaitPerSeg(String)
	@Test
	final void testTotalWaitPerSeg() {
		// 15+20+15+20+15+20 = 105
		assertEquals("Total waiting time for S2 must be 105.", "105.0", String.valueOf(v.totalWaitPerSeg("S2")));
	}

	// Links to avgWaitingTime()
	@Test
	final void testAvgWaitingTime() {
		// (105+105+105+105)/4 = 105
		assertEquals("Average waiting time for all segments must be 105.", "105.0",
				String.valueOf((v.avgWaitingTime())));
	}

}
