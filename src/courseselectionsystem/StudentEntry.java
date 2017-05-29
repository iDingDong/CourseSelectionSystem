/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author 堃
 */
public class StudentEntry {
	public static class UserInfo {
		public long id;
		public String password;
	}
	
	public static enum Function {
		exit_entry,
		timetable,
		view_courses,
		unknown
	}

	public static interface LoginHandler {
		public abstract UserInfo handle();
	}
	
	public static interface FunctionChoiceHandler {
		public abstract Function handle(Student user);
	}
	
	private Student m_user;
	private static LoginHandler s_login_handler;
	private static FunctionChoiceHandler s_function_choice_handler;
	
	public static void register_login_handler(LoginHandler handler) {
		s_login_handler = handler;
	}
	
	public static void register_function_choice_handler(
		FunctionChoiceHandler handler
	) {
		s_function_choice_handler = handler;
	}
	
	public Student get_user() {
		return m_user;
	}
	
	public void run() {
		UserInfo info;
		for (int count = 0; ; ++count) {
			if (count >= 3) {
				CourseSelectionSystem.send_message(
					"3 attempt failed. Returning."
				);
				return;
			}
			if (s_login_handler != null) {
				info = s_login_handler.handle();
			} else {
				info = new UserInfo();
				CourseSelectionSystem.send_cmd_message("ID: ");
				info.id = Long.valueOf(
					CourseSelectionSystem.get_cmd_input_string()
				);
				CourseSelectionSystem.send_cmd_message("Password: ");
				info.password = CourseSelectionSystem.get_cmd_input_string();
			}
			if (info == null) {
				return;
			}
			if (login(info)) {
				break;
			}
			CourseSelectionSystem.send_message("Failed to login.");
		}
		CourseSelectionSystem.send_cmd_message(
			"Welcome," + m_user.get_name() + "\n"
		);
		
		Function function_choice;
		for (; ; ) {
			if (s_function_choice_handler != null) {
				function_choice = s_function_choice_handler.handle(get_user());
			} else {
				CourseSelectionSystem.send_cmd_message(
					"Please select function: "
				);
				String choice = CourseSelectionSystem.get_cmd_input_string();
				switch (choice) {
					case "back":
					function_choice = Function.exit_entry;
					break;
					
					case "timetable":
					function_choice = Function.timetable;
					break;
					
					case "courses":
					function_choice = Function.view_courses;
					break;
					
					default:
					function_choice = Function.unknown;
					break;
					
				}
			}
			switch (function_choice) {
				case exit_entry:
				return;
				
				case timetable:
				break;
				
				case view_courses:
				break;
				
				default:
				CourseSelectionSystem.send_message(
					"Unknown function, please re-select"
				);
				break;
				
			}
		}
	}
	
	private boolean login(UserInfo info) {
		String sql =
			"SELECT password FROM students WHERE student_id = " + info.id
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					if (info.password.equals(sql_result.getString(1))) {
						m_user = new Student(info.id);
						return true;
					}
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return false;
	}
	
}
