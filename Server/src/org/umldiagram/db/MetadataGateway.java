package org.umldiagram.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MetadataGateway {
	private Connection dbCon = null;
        	
	public MetadataGateway() {
		dbCon = Database.getSingletonInstance().getConnection();
    }
	
	public ResultSet selectById(int id) throws SQLException {
	    String createString = "SELECT * FROM metadata WHERE id='"+id+"'";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	   
	    ResultSet rs = stmt.executeQuery(createString);
	    return rs;
	 }	
        
     public Boolean updateById(int id, int lastSessionId) throws SQLException {
        String createString = "UPDATE metadata SET last_session_id='" + lastSessionId + "' WHERE id='" + id + "'";
    	Statement stmt = null;
    	try {
    		stmt = dbCon.createStatement();
            stmt.executeUpdate(createString);
            return true;
    	}catch (SQLException e) {
            System.out.println("Update sql failed.");
            return false;
        }    
	 }           
}
