package org.umldiagram.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SessionHelper {
	private static final int ID = 1;
	
	public static int getLastSessionId() {
		MetadataGateway mGtw = new MetadataGateway();
		
		try {
			ResultSet rs = mGtw.selectById(ID);
			rs.first();
			return rs.getInt("last_session_id");
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public static void updateLastSessionId(int lastSessionId) {
		MetadataGateway mGtw = new MetadataGateway();
		
		try {
			mGtw.updateById(ID, lastSessionId);
		} catch (SQLException e) {
			// Fail
		}
	}
	
	public static boolean checkSessionById(int sessionId) {
		SessionsGateway sGtw = new SessionsGateway();
		try {
			ResultSet rs = sGtw.selectById(sessionId);
			return rs.first();
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean createSession(int sessionId) {
		SessionsGateway sGtw = new SessionsGateway();
		try {
			sGtw.insertSession(sessionId);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean updateLastElementIdOfSession(int sessionId, int elementId) {
		SessionsGateway sGtw = new SessionsGateway();
		try {
			sGtw.updateLastElementById(sessionId, elementId);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean updateContentOfSession(int sessionId, String content) {
		SessionsGateway sGtw = new SessionsGateway();
		try {
			sGtw.updateContentById(sessionId, content);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static int getLastElementIdOfSession(int sessionId) {
		SessionsGateway sGtw = new SessionsGateway();
		try {
			ResultSet rs = sGtw.selectById(sessionId);
			rs.first();
			return rs.getInt("last_element_id");
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public static String getSessionContent(int sessionId) {
		SessionsGateway sGtw = new SessionsGateway();
		try {
			ResultSet rs = sGtw.selectById(sessionId);
			rs.first();
			return rs.getString("content");
		} catch (SQLException e) {
			return "<whiteboard></whiteboard>";
		}
	}
	
	public static int getIdOfUser(String username) {
		UsersGateway uGtw = new UsersGateway();
		try {
			ResultSet rs = uGtw.selectByUsername(username);
			rs.first();
			return rs.getInt("id");
		} catch (SQLException e) {
			return -1;
		}		
	}
	
	public static boolean createUser(String username) {
		UsersGateway uGtw = new UsersGateway();
		try {
			uGtw.insertUser(username);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean checkUser(String username) {
		UsersGateway uGtw = new UsersGateway();
		try {
			ResultSet rs = uGtw.selectByUsername(username);
			return rs.first();			
		} catch (SQLException e) {
			return false;
		}		
	}
	
	public static ArrayList<Integer> getUserSessions(int userId) {
		UsersInSessionsGateway usGtw = new UsersInSessionsGateway();
		ArrayList<Integer> sessions = new ArrayList<Integer>();
		try {
			ResultSet rs = usGtw.selectByUserId(userId);
			while (rs.next()) {
				sessions.add(rs.getInt("session_id"));
			}
		} catch (SQLException e) {
			
		}
		return sessions;
	}
	
	public static boolean addUserInSession(int userId, int sessionId) {
		UsersInSessionsGateway usGtw = new UsersInSessionsGateway();
		try {
			usGtw.insertUserInSession(sessionId, userId);
			return true;
		} catch (SQLException e) {
			return false;
		}		
	}
	
	public static boolean checkUserInSession(int userId, int sessionId) {
		UsersInSessionsGateway usGtw = new UsersInSessionsGateway();
		try {
			ResultSet rs = usGtw.selectByUserIdAndSessionId(userId, sessionId);
			return rs.first();			
		} catch (SQLException e) {
			return false;
		}	
	}
}
