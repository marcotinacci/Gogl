package ibm650.gogl;

import static org.junit.Assert.*;
import ibm650.gogl.datastructure.Color;
import ibm650.gogl.exception.BadMoveException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGoban {

	private Goban goban;
	
	@Before
	public void setUp(){
		goban = new Goban(9);
	}
	
	@After
	public void print(){
		System.out.println(goban);
	}

	@Test
	public void testFDSingle() {
		try {
			goban.move(Color.BLACK, 3, 2);
			assertTrue(goban.getCross(3, 2).getFreedom() == 4);
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}

	@Test
	public void testFDCorner() {
		try {
			goban.move(Color.BLACK, 0, 0);
			assertTrue(goban.getCross(0, 0).getFreedom() == 2);
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	@Test
	public void testFDGroup() {
		try {
			goban.move(Color.BLACK, 1, 1);
			goban.move(Color.BLACK, 1, 2);
			goban.move(Color.BLACK, 2, 2);
			assertTrue(goban.getCross(1, 2).getFreedom() == 7);
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	@Test
	public void testMergeTwoGroups() {
		try {
			goban.move(Color.BLACK, 1, 1);
			goban.move(Color.BLACK, 1, 2);
			goban.move(Color.BLACK, 1, 4);
			goban.move(Color.BLACK, 1, 5);
			goban.move(Color.BLACK, 2, 5);
			goban.move(Color.BLACK, 1, 3);
			assertTrue(goban.getCross(1, 1).getFreedom() == 13);
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	@Test
	public void testStoneCaptured() {
		try {
			goban.move(Color.BLACK, 2, 2);
			goban.move(Color.BLACK, 3, 1);
			goban.move(Color.BLACK, 4, 2);
			goban.move(Color.WHITE, 3, 2);
			goban.move(Color.BLACK, 3, 3);
			assertTrue(goban.getCross(3, 2).getColor().equals(Color.EMPTY));
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	@Test
	public void testGroupCaptured() {
		try {
			goban.move(Color.BLACK, 2, 2);
			goban.move(Color.BLACK, 3, 1);
			goban.move(Color.WHITE, 4, 2);
			goban.move(Color.WHITE, 3, 2);
			goban.move(Color.BLACK, 4, 1);
			goban.move(Color.BLACK, 4, 3);
			goban.move(Color.BLACK, 5, 2);
			goban.move(Color.BLACK, 3, 3);
			assertTrue(goban.getCross(3, 2).getColor().equals(Color.EMPTY)
					&& goban.getCross(4, 2).getColor().equals(Color.EMPTY));
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	@Test
	public void testSuicide() {
		try {
			goban.move(Color.BLACK, 1, 2);
			goban.move(Color.BLACK, 2, 1);
			goban.move(Color.BLACK, 2, 3);
			goban.move(Color.BLACK, 3, 2);
			goban.move(Color.WHITE, 2, 2);
			fail("Suicide!");
		} catch (BadMoveException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testCaptureOverSuicide() {
		try {
			goban.move(Color.WHITE, 4, 2);
			goban.move(Color.BLACK, 1, 2);
			goban.move(Color.WHITE, 3, 1);
			goban.move(Color.BLACK, 2, 3);
			goban.move(Color.WHITE, 3, 3);
			goban.move(Color.BLACK, 2, 1);
			goban.move(Color.WHITE, 2, 2);
			goban.move(Color.BLACK, 3, 2);
			assertTrue(goban.getCross(3, 2).getColor().equals(Color.BLACK)
					&& goban.getCross(2, 2).getColor().equals(Color.EMPTY));
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	@Test
	public void testKO() {
		try {
			goban.move(Color.WHITE, 4, 2);
			goban.move(Color.BLACK, 1, 2);
			goban.move(Color.WHITE, 3, 1);
			goban.move(Color.BLACK, 2, 3);
			goban.move(Color.WHITE, 3, 3);
			goban.move(Color.BLACK, 2, 1);
			goban.move(Color.BLACK, 3, 2);
			goban.move(Color.WHITE, 2, 2);
			goban.move(Color.BLACK, 3, 2);
			fail("KO!");
		} catch (BadMoveException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testNotKO() {
		try {
			goban.move(Color.WHITE, 4, 2);
			goban.move(Color.BLACK, 1, 2);
			goban.move(Color.WHITE, 3, 1);
			goban.move(Color.BLACK, 2, 3);
			goban.move(Color.WHITE, 3, 3);
			goban.move(Color.BLACK, 2, 1);
			goban.move(Color.BLACK, 3, 2);
			goban.move(Color.WHITE, 2, 2);
			goban.move(Color.BLACK, 8, 8);
			goban.move(Color.WHITE, 7, 7);
			
			goban.move(Color.BLACK, 3, 2);
			assertTrue(goban.getCross(2, 2).getColor().equals(Color.EMPTY)
					&& goban.getCross(3, 2).getColor().equals(Color.BLACK));
		} catch (BadMoveException e) {
			fail("Bad move!");
		}
	}
	
	
	
}
