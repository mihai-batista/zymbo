package org.umldiagram.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserGateway {
	private Connection dbCon = null;
        	
	public UserGateway() {
		dbCon = Database.getSingletonInstance().getConnection();
        }
	
	public ResultSet selecteazaDupaUserSiParola(String nume, String parola) throws SQLException {
	    System.out.println("nume = "+nume);
            System.out.println("pass="+parola);
            String createString = "SELECT * FROM user WHERE username='"+nume+"' AND password='"+parola+"'";
	    //String createString = "SELECT * FROM user";
            Statement stmt = null;
	    stmt = dbCon.createStatement();
	   
	    ResultSet rs = stmt.executeQuery(createString);
	    return rs;
	 }	
        
        public ResultSet selecteazaTotiUserii() throws SQLException {
	    String createString = "SELECT * FROM user";
            Statement stmt = null;
	    stmt = dbCon.createStatement();
	   
	    ResultSet rs = stmt.executeQuery(createString);
	    return rs;
	 }
        
        	 
	 public void adaugaUser(String username, String password, String name, String address, String birthdate, String role) throws SQLException {
            String createString = "INSERT INTO user (username, password, name, address, birthdate, role) VALUES('"+username+"','"+password+"','"+name+"','"+address+"','"+birthdate+"','"+role+"')";
	    Statement stmt = null;
	    stmt = dbCon.createStatement();
	    stmt.executeUpdate(createString);
	 }
         
         public boolean stergeUser(int id)  {
	    String createString = "DELETE FROM user WHERE id='"+id+"'";
	    Statement stmt = null;
	    try {
                stmt = dbCon.createStatement();
                stmt.executeUpdate(createString);
                return true;
            }catch (SQLException e) {
                return false;
            }    
            
	}
         
         public boolean updateUser(int id, String username, String password, String name, String address, String birthdate, String role)  {
	    String createString = "UPDATE user SET username='"+username+"', password='"+password+"', name='"+name+"', address='"+address+"', birthdate='"+birthdate+"', role='"+role+"' WHERE id='"+id+"'";
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
