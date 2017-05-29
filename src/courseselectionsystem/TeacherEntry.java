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
public class TeacherEntry {
	public static class UserInfo {
		public int id;
		public String password;
	}
	
	public static class CourseInfo {
		public int id;
		public String name;
		public int capacity;
		public int begin_week;
		public int end_week;
	}
	
	public enum Function {
		exit_entry,
		add_course,
		delete_course,
		modify_course,
		unknown
	}
	
	public static interface LoginHandler {
		public abstract UserInfo handle();
	}
	
	public static interface AddCourseHandler {
		public abstract CourseInfo handle();
	}
	
	public static interface FunctionChoiceHandler {
		public abstract Function handle(ArrayList<Courses> courses);
	}
	
	private Teacher m_user;
	private static LoginHandler s_login_handler;
	private static FunctionChoiceHandler s_function_choice_handler;
	
	public Teacher get_user() {
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
				info.id = Integer.valueOf(
					CourseSelectionSystem.get_cmd_input_string()
				);
				CourseSelectionSystem.send_cmd_message("Password: ");
				info.password = CourseSelectionSystem.get_cmd_input_string();
			}
			if (login(info)) {
				break;
			}
			CourseSelectionSystem.send_message("Failed to login.");
		}
		CourseSelectionSystem.send_cmd_message(
			"Welcome," + m_user.get_name() + "\n"
		);
		
		Function function_choice = Function.unknown;
		for (; ; ) {
			if (s_function_choice_handler != null) {
				function_choice = s_function_choice_handler.handle(
					get_user().get_courses()
				);
			} else {
				CourseSelectionSystem.send_cmd_message(
					"Please select function: "
				);
				String choice = CourseSelectionSystem.get_cmd_input_string();
				if (choice.equals("back")) {
					function_choice = Function.exit_entry;
				} else if (choice.equals("add")) {
					function_choice = Function.add_course;
				} else {
					function_choice = Function.unknown;
				}
			}
			switch (function_choice) {
				case exit_entry:
				return;
				
				case add_course:
				add_course();
				break;
				
				default:
				CourseSelectionSystem.send_message(
					"Unknown function, please re-select"
				);
				break;
				
			}
		}
	}
	
	private void add_course() {
		
	}
	
	private boolean login(UserInfo info) {
		String sql =
			"SELECT password FROM teachers WHERE teacher_id = " + info.id
		;
		try {
			java.sql.ResultSet sql_result =
				CourseSelectionSystem.get_statement().executeQuery(sql)
			;
			try {
				if (sql_result.next()) {
					if (info.password.equals(sql_result.getString(1))) {
						m_user = new Teacher(info.id);
						return true;
					}
				}
			} finally {
				sql_result.close();
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return false;
	}
	
	private void add_course(Course.Info info) {
		if (Course.exist_id(info.id)) {
			CourseSelectionSystem.send_message("Course id already exist!");
			return;
		}
		String sql =
			"INSERT INTO courses values(" +
			info.id +
			", " +
			info.name +
			", " +
			get_user().get_id() +
			", " +
			info.capacity +
			", " +
			info.begin_week +
			", " +
			info.end_week +
			");"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Failed to add course.");
		}
	}
	
}
