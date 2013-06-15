package org.umldiagram.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersGateway {
	private Connection dbCon = null;
	
	public UsersGateway() {
		dbCon = Database.getSingletonInstance().getConnection();
    }
	
	public ResultSet selectByUsername(String username) throws SQLException {
	    String createString = "SELECT * FROM users WHERE username='" + username + "'";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	   
	    ResultSet rs = stmt.executeQuery(createString);
	    return rs;
	 }
	
	public void insertUser(String username) throws SQLException {
		String insertString = "INSERT INTO users (username) VALUES('" + username + "')";
		Statement stmt = null;
		stmt = dbCon.createStatement();
		stmt.executeUpdate(insertString);
	}
        
}
