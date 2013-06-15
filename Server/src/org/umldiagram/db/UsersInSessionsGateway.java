package org.umldiagram.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersInSessionsGateway {
	private Connection dbCon = null;
	
	public UsersInSessionsGateway() {
		dbCon = Database.getSingletonInstance().getConnection();
    }
	
	public ResultSet selectByUserId(int userId) throws SQLException {
	    String selectQuery = "SELECT * FROM usersinsessions WHERE user_id='" + userId + "'";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	   
	    ResultSet rs = stmt.executeQuery(selectQuery);
	    return rs;
	 }	
        
     public void insertUserInSession(int sessionId, int userId) throws SQLException {
        String insertQuery = "INSERT INTO usersinsessions (session_id, user_id) VALUES('"+ sessionId + "','" + userId + "')";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	    stmt.executeUpdate(insertQuery);
	 }
     
     public ResultSet selectByUserIdAndSessionId(int userId, int sessionId) throws SQLException {
 	    String selectQuery = "SELECT * FROM usersinsessions WHERE user_id='" + userId + "' AND session_id='" + sessionId + "'";
 	    Statement stmt = null;
 	    stmt = dbCon.createStatement();
 	   
 	    ResultSet rs = stmt.executeQuery(selectQuery);
 	    return rs;
 	 }	
}
