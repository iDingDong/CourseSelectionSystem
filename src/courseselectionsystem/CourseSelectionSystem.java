/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 堃
 */
public class CourseSelectionSystem {
	public enum Entry {
		exit_system,
		teacher,
		student,
		admin,
		unknown
	}
	
	public static interface MessageHandler {
		public abstract void handle(String message);
	}
	
	public static interface EntrySelectionHandler {
		public abstract Entry handle();
	}
	
	private static Scanner s_scanner = new Scanner(System.in);
	private static MessageHandler s_message_handler;
	private static EntrySelectionHandler s_entry_selection_handler;
	private static java.sql.Connection s_connection;
	private static java.sql.Statement s_statement;
	
	public static java.sql.Statement get_statement() {
		return s_statement;
	}
	
	public static void send_message(String message) {
		if (s_message_handler != null) {
			s_message_handler.handle(message);
		}
		send_cmd_message("System Message: " + message + "\n");
	}
	
	public static void send_cmd_message(String message) {
		System.out.print(message);
	}
	
	public static String get_cmd_input_string() {
		return s_scanner.nextLine();
	}
	

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("begin");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			s_connection = java.sql.DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/course_selection?" +
				"user=root&password=&useUnicode=true&characterEncoding=UTF8"
			);
			try {
				s_statement = s_connection.createStatement();
				try {
					run_system();
				} finally {
					s_statement.close();
				}
			} finally {
				s_connection.close();
			}
		} catch(ClassNotFoundException ex) {
			send_message("Failed to reach database.");
		} catch (java.sql.SQLException ex) {
			send_message("Failed to reach database.");
		}
	}
	
	public static void run_system() {
		for (; ; ) {
			Entry entry;
			if (s_entry_selection_handler != null) {
				entry = s_entry_selection_handler.handle();
			} else {
				send_cmd_message("Please select an entry: ");
				String choice = get_cmd_input_string();
				if (choice.equals("exit")) {
					entry = Entry.exit_system;
				} else if (choice.equals("teacher")) {
					entry = Entry.teacher;
				} else if (choice.equals("student")) {
					entry = Entry.student;
				} else if (choice.equals("admin")) {
					entry = Entry.admin;
				} else {
					entry = Entry.unknown;
				}
			}
			switch (entry) {
				case exit_system:
				return;
				
				case teacher:
				TeacherEntry teacher_entry = new TeacherEntry();
				teacher_entry.run();
				break;
				
				case student:
				break;
				
				case admin:
				break;
				
				default:
				send_message("Unknown entry, please re-select");
				break;

			}
		}
	}
	
}
