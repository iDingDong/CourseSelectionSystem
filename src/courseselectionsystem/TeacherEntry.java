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
		public List<Course.Lesson> lessons;
	}
	
	public static enum Function {
		exit_entry,
		add_course,
		delete_course,
		modify_course,
		unknown
	}
	
	public static class Action {
		Function function_choice;
		Course course;
	}

	public static interface LoginHandler {
		public abstract UserInfo handle();
	}
	
	public static interface ActionHandler {
		public abstract Action handle(Teacher user);
	}
	
	public static interface AddCourseHandler {
		public abstract CourseInfo handle();
	}
	
	public static interface ModifyCourseHandler {
		public abstract void handle(CourseInfo info);
	}
	
	private Teacher m_user;
	private static LoginHandler s_login_handler;
	private static ActionHandler s_action_handler;
	private static AddCourseHandler s_add_course_handler;
	private static ModifyCourseHandler s_modify_course_handler;
	
	public static void register_login_handler(LoginHandler handler) {
		s_login_handler = handler;
	}
	
	public static void register_function_choice_handler(
		ActionHandler handler
	) {
		s_action_handler = handler;
	}
	
	public static void register_add_course_handler(
		AddCourseHandler handler
	) {
		s_add_course_handler = handler;
	}
	
	public static void register_modify_course_handler(
		ModifyCourseHandler handler
	) {
		s_modify_course_handler = handler;
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
		
		Action action;
		for (; ; ) {
			if (s_action_handler != null) {
				action = s_action_handler.handle(get_user());
			} else {
				action = new Action();
				Course.display_info_header();
				CourseSelectionSystem.send_cmd_message(
					Course.display_info_header() + "\n"
				);
				for (Course course : get_user().get_courses()) {
					CourseSelectionSystem.send_cmd_message(
						course.display_info_on_cmd() + "\n"
					);
				}
				CourseSelectionSystem.send_cmd_message(
					"Please select function: "
				);
				String choice = CourseSelectionSystem.get_cmd_input_string();
				switch (choice) {
					case "back":
					action.function_choice = Function.exit_entry;
					break;
					
					case "add":
					action.function_choice = Function.add_course;
					break;
					
					case "delete":
					action.function_choice = Function.delete_course;
					CourseSelectionSystem.send_cmd_message("Course ID: ");
					action.course = new Course(Long.valueOf(
						CourseSelectionSystem.get_cmd_input_string()
					));
					break;
					
					case "modify":
					action.function_choice = Function.modify_course;
					CourseSelectionSystem.send_cmd_message("Course ID: ");
					action.course = new Course(Long.valueOf(
						CourseSelectionSystem.get_cmd_input_string()
					));
					break;
					
					default:
					action.function_choice = Function.unknown;
					break;
					
				}
			}
			switch (action.function_choice) {
				case exit_entry:
				return;
				
				case add_course:
				add_course();
				break;
				
				case delete_course:
				delete_course(action.course);
				break;
				
				case modify_course:
				modify_course(action.course);
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
			info.lessons = new ArrayList<Course.Lesson>();
			for (; ; ) {
				String input;
				Course.Lesson lesson = new Course.Lesson();
				CourseSelectionSystem.send_cmd_message(
					"Day of week: "
				);
				input = CourseSelectionSystem.get_cmd_input_string();
				if (input.equals("cancel")) {
					break;
				}
				lesson.day_of_week = Integer.valueOf(input);
				CourseSelectionSystem.send_cmd_message(
					"Lesson of day: "
				);
				input = CourseSelectionSystem.get_cmd_input_string();
				if (input.equals("cancel")) {
					break;
				}
				lesson.lesson_of_day = Integer.valueOf(input);
				info.lessons.add(lesson);
			}
		}
		add_course_impl(info);
	}
	
	private void add_course_impl(CourseInfo info) {
		if (Course.exist_id(info.id)) {
			CourseSelectionSystem.send_message("Course id already exist!");
			return;
		}
		if (info.begin_week > info.end_week) {
			CourseSelectionSystem.send_message(
				"Begin week should be no later than end week."
			);
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
		new Course(info.id).set_lessons(info.lessons);
	}
	
	private void delete_course(Course course) {
		if (course.get_teacher().get_id() != get_user().get_id()) {
			CourseSelectionSystem.send_message("This is not your course.");
			return;
		}
		course.delete_course();
	}
	
	private void modify_course(Course course) {
		if (course.get_teacher().get_id() != get_user().get_id()) {
			CourseSelectionSystem.send_message("This is not your course.");
			return;
		}
		CourseInfo info = new CourseInfo();
		info.id = course.get_id();
		info.name = course.get_name();
		info.capacity = course.get_capacity();
		info.begin_week = course.get_begin_week();
		info.end_week = course.get_end_week();
		info.lessons = course.get_lessons();
		if (s_modify_course_handler != null) {
			s_modify_course_handler.handle(info);
		} else {
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
			if (info.begin_week > info.end_week) {
				CourseSelectionSystem.send_message(
					"Begin week should be no later than end week."
				);
				return;
			}
			for (; ; ) {
				String input;
				Course.Lesson lesson = new Course.Lesson();
				CourseSelectionSystem.send_cmd_message(
					"Day of week: "
				);
				input = CourseSelectionSystem.get_cmd_input_string();
				if (input.equals("cancel")) {
					break;
				}
				lesson.day_of_week = Integer.valueOf(input);
				CourseSelectionSystem.send_cmd_message(
					"Lesson of day: "
				);
				input = CourseSelectionSystem.get_cmd_input_string();
				if (input.equals("cancel")) {
					break;
				}
				lesson.lesson_of_day = Integer.valueOf(input);
				info.lessons.add(lesson);
			}
		}
		modify_course_impl(course, info);
	}
	
	private void modify_course_impl(
		Course course, CourseInfo info
	) {
		/*
		if (
			course.check_new_id(info.id) &&
			course.check_new_name(info.name) &&
			course.check_new_capacity(info.capacity) &&
			course.check_new_schedule(
				info.begin_week, info.end_week
			)
		) {
			course.change_id(info.id);
			course.change_name(info.name);
			course.change_capacity(info.capacity);
			course.change_schedule(
				info.begin_week, info.end_week
			);
			course.set_lessons(info.lessons);
		}
		*/
		course.delete_course();
		add_course_impl(info);
	}
	
}
