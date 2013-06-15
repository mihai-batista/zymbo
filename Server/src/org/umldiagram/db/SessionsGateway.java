package org.umldiagram.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SessionsGateway {
	private Connection dbCon = null;
	
	public SessionsGateway() {
		dbCon = Database.getSingletonInstance().getConnection();
    }
	
	public ResultSet selectById(int id) throws SQLException {
	    String selectQuery = "SELECT * FROM sessions WHERE id='"+id+"'";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	   
	    ResultSet rs = stmt.executeQuery(selectQuery);
	    return rs;
	 }	
        
     public Boolean updateLastElementById(int id, int lastElementId) throws SQLException {
        String updateQuery = "UPDATE sessions SET last_element_id='" + lastElementId + "' WHERE id='" + id + "'";
    	Statement stmt = null;
    	try {
    		stmt = dbCon.createStatement();
            stmt.executeUpdate(updateQuery);
            return true;
    	}catch (SQLException e) {
            System.out.println("Update sql failed.");
            return false;
        }    
	 }
     
     public Boolean updateContentById(int id, String content) throws SQLException {
        String updateQuery = "UPDATE sessions SET content='" + content + "' WHERE id='" + id + "'";
     	Statement stmt = null;
     	try {
     		stmt = dbCon.createStatement();
             stmt.executeUpdate(updateQuery);
             return true;
     	}catch (SQLException e) {
             System.out.println("Update sql failed.");
             return false;
         }    
 	 }
     
     public void insertSession(int sessionId) throws SQLException {
        String insertQuery = "INSERT INTO sessions (id, last_element_id, content) VALUES('"+ sessionId + "','0', '<whiteboard></whiteboard>')";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	    stmt.executeUpdate(insertQuery);
	 }
}
