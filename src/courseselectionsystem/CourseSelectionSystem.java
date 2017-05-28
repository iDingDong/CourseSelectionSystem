/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 堃
 */
public class CourseSelectionSystem {
	public static interface MessageHandler {
		public abstract void handle(String message);
	}
	
	private static MessageHandler s_message_handler;
	private static java.sql.Connection s_connection;
	private static java.sql.Statement s_statement;
	

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			s_connection = java.sql.DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/course_selection?" +
				"user=root&password=&useUnicode=true&characterEncoding=UTF8"
			);
			s_statement = s_connection.createStatement();
		} catch (java.sql.SQLException ex) {
			send_message("Failed to reach database.");
		}
	}
	
	public static java.sql.Statement get_statement() {
		return s_statement;
	}
	
	public static void send_message(String message) {
		if (s_message_handler != null) {
			s_message_handler.handle(message);
		}
	}
	
}
