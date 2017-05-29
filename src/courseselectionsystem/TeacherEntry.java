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
		public long id;
		public String password;
	}
	
	public static class CourseInfo {
		public long id;
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
	
	public static interface FunctionChoiceHandler {
		public abstract Function handle(ArrayList<Course> courses);
	}
	
	public static interface AddCourseHandler {
		public abstract CourseInfo handle();
	}
	
	private Teacher m_user;
	private static LoginHandler s_login_handler;
	private static FunctionChoiceHandler s_function_choice_handler;
	private static AddCourseHandler s_add_course_handler;
	
	public static void register_login_handler(LoginHandler handler) {
		s_login_handler = handler;
	}
	
	public static void register_function_choice_handler(
		FunctionChoiceHandler handler
	) {
		s_function_choice_handler = handler;
	}
	
	public static void register_add_course_handler(
		AddCourseHandler handler
	) {
		s_add_course_handler = handler;
	}
	
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
		CourseInfo info;
		if (s_add_course_handler != null) {
			info = s_add_course_handler.handle();
		} else {
			info = new CourseInfo();
			CourseSelectionSystem.send_cmd_message("ID: ");
			info.id = Long.valueOf(
				CourseSelectionSystem.get_cmd_input_string()
			);
			CourseSelectionSystem.send_cmd_message("Name: ");
			info.name = CourseSelectionSystem.get_cmd_input_string();
			CourseSelectionSystem.send_cmd_message("Capacity: ");
			info.capacity = Integer.valueOf(
				CourseSelectionSystem.get_cmd_input_string()
			);
			CourseSelectionSystem.send_cmd_message("Begin week: ");
			info.begin_week = Integer.valueOf(
				CourseSelectionSystem.get_cmd_input_string()
			);
			CourseSelectionSystem.send_cmd_message("End week: ");
			info.end_week = Integer.valueOf(
				CourseSelectionSystem.get_cmd_input_string()
			);
		}
		add_course_impl(info);
	}
	
	private boolean login(UserInfo info) {
		String sql =
			"SELECT password FROM teachers WHERE teacher_id = " + info.id
		;
		try {
			try (
				java.sql.ResultSet sql_result =
					CourseSelectionSystem.get_statement().executeQuery(sql)
			) {
				if (sql_result.next()) {
					if (info.password.equals(sql_result.getString(1))) {
						m_user = new Teacher(info.id);
						return true;
					}
				}
			}
		} catch (SQLException ex) {
			CourseSelectionSystem.send_message("Unable to inquire.");
		}
		return false;
	}
	
	private void add_course_impl(CourseInfo info) {
		if (Course.exist_id(info.id)) {
			CourseSelectionSystem.send_message("Course id already exist!");
			return;
		}
		String sql =
			"INSERT INTO courses values(" +
			info.id +
			", '" +
			info.name +
			"', " +
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
	
	private void delete_course_impl(Course course) {
		if (course.get_teacher().get_id() == get_user().get_id()) {
			CourseSelectionSystem.send_message("This is not your course.");
		} else {
			course.delete_course();
		}
	}
	
}
