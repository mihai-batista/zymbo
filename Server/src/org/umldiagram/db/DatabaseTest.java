package org.umldiagram.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class DatabaseTest {

	@Test
	public void databaseConnectionSuccessfullyEstablished() {
		assertNotNull(Database.getSingletonInstance().getConnection());
	}

}
