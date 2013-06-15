package org.umldiagram.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class SessionHelperTest {

	@Test
	public void testupdateLastSessionId() {
		SessionHelper.updateLastSessionId(3826);
		assertEquals(3826, SessionHelper.getLastSessionId());
	}
	
	@Test
	public void testGetLastSessionId() {
		assertEquals(3826, SessionHelper.getLastSessionId());
	}
	
	@Test
	public void testCheckSessionById() {
		assertEquals(true, SessionHelper.checkSessionById(7));
		assertEquals(false, SessionHelper.checkSessionById(6));
	}
	
	@Test
	public void testGetLastElementIdOfSession() {
		assertEquals(1, SessionHelper.getLastElementIdOfSession(7));
	}
	
	@Test
	public void testGetUserIdByUsername() {
		assertEquals(1, SessionHelper.getIdOfUser("smsapp@smsfeedback.com"));
	}
	
	@Test
	public void testCreateUser() {
		assertEquals(true, SessionHelper.createUser("mihai.batista@gmail.com"));
		assertEquals(true, SessionHelper.checkUser("mihai.batista@gmail.com"));
		assertEquals(false, SessionHelper.checkUser("mihai.batista1@gmail.com"));
	}
	
	@Test
	public void testCheckUserInSession() {
		assertEquals(true, SessionHelper.checkUserInSession(SessionHelper.getIdOfUser("smsapp@smsfeedback.com"), 3829));
	}
	
	@Test
	public void testAddUserInSession() {
		assertEquals(true, SessionHelper.addUserInSession(SessionHelper.getIdOfUser("sucursala1@smsfeedback.com"), 3829));
		
	}
	
	
	

}
