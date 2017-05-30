/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		courses,
		unknown
	}

	public static interface LoginHandler {
		public abstract UserInfo handle();
	}
	
	public static interface FunctionChoiceHandler {
		public abstract Function handle(Student user);
	}
	
	public static interface ViewTimetableHandler {
		public abstract Course handle(List<Course> all_courses);
	}
	
	public static interface ViewCoursesHandler {
		public abstract Course handle(List<Course> all_courses);
	}
	
	private Student m_user;
	private static LoginHandler s_login_handler;
	private static FunctionChoiceHandler s_function_choice_handler;
	private static ViewTimetableHandler s_view_timetable_handler;
	private static ViewCoursesHandler s_view_courses_handler;
	
	public static void register_login_handler(LoginHandler handler) {
		s_login_handler = handler;
	}
	
	public static void register_function_choice_handler(
		FunctionChoiceHandler handler
	) {
		s_function_choice_handler = handler;
	}
	
	public static void register_view_timetable_handler(
		ViewTimetableHandler handler
	) {
		s_view_timetable_handler = handler;
	}
	
	public static void register_view_courses_handler(
		ViewCoursesHandler handler
	) {
		s_view_courses_handler = handler;
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
					function_choice = Function.courses;
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
				view_timetable();
				break;
				
				case courses:
				view_courses();
				break;
				
				default:
				CourseSelectionSystem.send_message(
					"Unknown function, please re-select"
				);
				break;
				
			}
		}
	}
	
	private void view_timetable() {
		for (; ; ) {
			Course selected;
			if (s_view_timetable_handler != null) {
				selected = s_view_timetable_handler.handle(
					Course.get_all_courses()
				);
			} else {
				CourseSelectionSystem.send_cmd_message(
					Course.display_info_header() + "\n"
				);
				for (Course course : get_user().get_courses()) {
					CourseSelectionSystem.send_cmd_message(
						course.display_info_on_cmd() + "\n"
					);
				}
				CourseSelectionSystem.send_cmd_message("De-select a course: ");
				String selection = CourseSelectionSystem.get_cmd_input_string();
				if (selection.equals("back")) {
					selected = null;
				} else {
					selected = new Course(Long.valueOf(selection));
				}
			}
			if (selected == null) {
				return;
			}
			get_user().deselect_course(selected);
		}
	}
	
	private void view_courses() {
		for (; ; ) {
			Course selected;
			if (s_view_courses_handler != null) {
				selected = s_view_courses_handler.handle(
					Course.get_all_courses()
				);
			} else {
				CourseSelectionSystem.send_cmd_message(
					Course.display_info_header() + "\n"
				);
				for (Course course : Course.get_all_courses()) {
					CourseSelectionSystem.send_cmd_message(
						course.display_info_on_cmd() + "\n"
					);
				}
				CourseSelectionSystem.send_cmd_message("Select a course: ");
				String selection = CourseSelectionSystem.get_cmd_input_string();
				if (selection.equals("back")) {
					selected = null;
				} else {
					selected = new Course(Long.valueOf(selection));
				}
			}
			if (selected == null) {
				return;
			}
			get_user().select_course(selected);
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
