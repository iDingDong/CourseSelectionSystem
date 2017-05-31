/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 堃
 */
public class AdminEntry {
	public long m_id = 1;
	public String m_password = "";
	
	public static enum Function {
		exit_entry,
		teacher,
		student,
		unknown
	}
	
	public static enum Choice {
		back_to_list,
		add,
		delete,
		modify,
		unknown
	}
	
	public static class UserInfo {
		public long id;
		public String password;
	}
	
	public static class RegisterInfo {
		public long id;
		public String name;
		public String password;
	}
	
	public static class TAction {
		Choice choice;
		Teacher teacher;
	}
	
	public static class SAction {
		Choice choice;
		Student student;
	}
	
	public static interface LoginHandler {
		public abstract UserInfo handle();
	}
	
	public static interface FunctionChoiceHandler {
		public abstract Function handle();
	}
	
	public static interface ManageTeacherHandler {
		public abstract TAction handle(List<Teacher> teachers);
	}
	
	public static interface ManageStudentHandler {
		public abstract SAction handle(List<Student> Student);
	}
	
	public static interface AddTeacherHandler {
		public abstract RegisterInfo handle();
	}
	
	public static interface AddStudentHandler {
		public abstract RegisterInfo handle();
	}
	
	private static LoginHandler s_login_handler;
	private static FunctionChoiceHandler s_function_choice_handler;
	private static ManageTeacherHandler s_manage_teacher_handler;
	private static ManageStudentHandler s_manage_student_handler;
	private static AddTeacherHandler s_add_teacher_handler;
	private static AddStudentHandler s_add_student_handler;
	
	
	
	public static void register_login_handler(LoginHandler handler) {
		s_login_handler = handler;
	}
	
	public static void register_function_choice_handler(
		FunctionChoiceHandler handler
	) {
		s_function_choice_handler = handler;
	}
	
	public static void register_manage_teacher_handler(
		ManageTeacherHandler handler
	) {
		s_manage_teacher_handler = handler;
	}
	
	public static void register_manage_student_handler(
		ManageStudentHandler handler
	) {
		s_manage_student_handler = handler;
	}
	
	public static void register_add_teacher_handler(
		AddTeacherHandler handler
	) {
		s_add_teacher_handler = handler;
	}
	
	public static void register_add_student_handler(
		AddStudentHandler handler
	) {
		s_add_student_handler = handler;
	}
	
	private boolean login(UserInfo info) {
		return info.id == m_id && info.password.equals(m_password);
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
			"Welcome, admin\n"
		);
		Function function_choice;
		for (; ; ) {
			if (s_function_choice_handler != null) {
				function_choice = s_function_choice_handler.handle();
			} else {
				CourseSelectionSystem.send_cmd_message(
					"Please select function: "
				);
				String choice = CourseSelectionSystem.get_cmd_input_string();
				switch (choice) {
					case "back":
					function_choice = Function.exit_entry;
					break;
					
					case "teacher":
					function_choice = Function.teacher;
					break;
					
					case "student":
					function_choice = Function.student;
					break;
					
					default:
					function_choice = Function.unknown;
					break;
					
				}
			}
			switch (function_choice) {
				case exit_entry:
				return;
				
				case teacher:
				view_teachers();
				break;
				
				case student:
				view_students();
				break;
				
				default:
				CourseSelectionSystem.send_message(
					"Unknown function, please re-select"
				);
				break;
				
			}
		}
	}
	
	private void view_teachers() {
		for (; ; ) {
			List<Teacher> teachers = new ArrayList<Teacher>();
			String sql = "SELECT teacher_id FROM teachers;";
			try {
				try (
					java.sql.ResultSet sql_result =
						CourseSelectionSystem.get_statement().executeQuery(sql)
				) {
					while (sql_result.next()) {
						teachers.add(new Teacher(sql_result.getLong(1)));
					}
				}
			} catch (java.sql.SQLException ex) {
				CourseSelectionSystem.send_message("Unable to inquire.");
			}
			TAction action;
			if (s_manage_teacher_handler != null) {
				action = s_manage_teacher_handler.handle(teachers);
			} else {
				action = new TAction();
				CourseSelectionSystem.send_cmd_message(
					Teacher.display_info_header(true) + "\n"
				);
				for (Teacher teacher : teachers) {
					CourseSelectionSystem.send_cmd_message(
						teacher.display_info_on_cmd(true) + "\n"
					);
				}
				CourseSelectionSystem.send_cmd_message("Please select an action: ");
				switch (CourseSelectionSystem.get_cmd_input_string()) {
					case "back":
					action.choice = Choice.back_to_list;
					break;

					case "add":
					action.choice = Choice.add;
					break;

					case "delete":
					action.choice = Choice.delete;
					CourseSelectionSystem.send_cmd_message("Teacher ID: ");
					action.teacher = new Teacher(Long.valueOf(
						CourseSelectionSystem.get_cmd_input_string()
					));
					break;

					case "default":
					action.choice = Choice.unknown;
					break;

				}
			}
			switch (action.choice) {
				case back_to_list:
				return;

				case add:
				add_teacher();
				break;

				case delete:
				delete_teacher(action.teacher);
				break;

				default:
				CourseSelectionSystem.send_message(
					"Unknown action, please re-select"
				);
				break;

			}
		}
	}
	
	private void view_students() {
		for (; ; ) {
			List<Student> students = new ArrayList<Student>();
			String sql = "SELECT student_id FROM students;";
			try {
				try (
					java.sql.ResultSet sql_result =
						CourseSelectionSystem.get_statement().executeQuery(sql)
				) {
					while (sql_result.next()) {
						students.add(new Student(sql_result.getLong(1)));
					}
				}
			} catch (java.sql.SQLException ex) {
				CourseSelectionSystem.send_message("Unable to inquire.");
			}
			SAction action;
			if (s_manage_student_handler != null) {
				action = s_manage_student_handler.handle(students);
			} else {
				action = new SAction();
				CourseSelectionSystem.send_cmd_message(
					Teacher.display_info_header(true) + "\n"
				);
				for (Student student : students) {
					CourseSelectionSystem.send_cmd_message(
						student.display_info_on_cmd(true) + "\n"
					);
				}
				CourseSelectionSystem.send_cmd_message("Please select an action: ");
				switch (CourseSelectionSystem.get_cmd_input_string()) {
					case "back":
					action.choice = Choice.back_to_list;
					break;

					case "add":
					action.choice = Choice.add;
					break;

					case "delete":
					action.choice = Choice.delete;
					CourseSelectionSystem.send_cmd_message("Student ID: ");
					action.student = new Student(Long.valueOf(
						CourseSelectionSystem.get_cmd_input_string()
					));
					break;

					case "default":
					action.choice = Choice.unknown;
					break;

				}
			}
			switch (action.choice) {
				case back_to_list:
				return;

				case add:
				add_student();
				break;

				case delete:
				delete_student(action.student);
				break;

				default:
				CourseSelectionSystem.send_message(
					"Unknown action, please re-select"
				);
				break;

			}
		}
	}
	
	private void add_teacher() {
		RegisterInfo info;
		if (s_add_teacher_handler != null) {
			info = s_add_teacher_handler.handle();
		} else {
			info = new RegisterInfo();
			CourseSelectionSystem.send_cmd_message("ID: ");
			info.id = Long.valueOf(
				CourseSelectionSystem.get_cmd_input_string()
			);
			CourseSelectionSystem.send_cmd_message("Name: ");
			info.name = CourseSelectionSystem.get_cmd_input_string();
			CourseSelectionSystem.send_cmd_message("Password: ");
			info.password = CourseSelectionSystem.get_cmd_input_string();
		}
		add_teacher_impl(info);
	}
	
	private void add_teacher_impl(RegisterInfo info) {
		if (Teacher.exist_id(info.id)) {
			CourseSelectionSystem.send_message("ID already occupied.");
			return;
		}
		String sql =
			"INSERT INTO teachers VALUES(" +
			info.id +
			", '" +
			info.name +
			"', '" +
			info.password +
			"');"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (java.sql.SQLException ex) {
			CourseSelectionSystem.send_message("Failed to add teacher.");
		}
	}
	
	private void delete_teacher(Teacher teacher) {
		teacher.delete_teacher();
	}
	
	private void add_student() {
		RegisterInfo info;
		if (s_add_student_handler != null) {
			info = s_add_student_handler.handle();
		} else {
			info = new RegisterInfo();
			CourseSelectionSystem.send_cmd_message("ID: ");
			info.id = Long.valueOf(
				CourseSelectionSystem.get_cmd_input_string()
			);
			CourseSelectionSystem.send_cmd_message("Name: ");
			info.name = CourseSelectionSystem.get_cmd_input_string();
			CourseSelectionSystem.send_cmd_message("Password: ");
			info.password = CourseSelectionSystem.get_cmd_input_string();
		}
		add_student_impl(info);
	}
	
	private void add_student_impl(RegisterInfo info) {
		if (Student.exist_id(info.id)) {
			CourseSelectionSystem.send_message("ID already occupied.");
			return;
		}
		String sql =
			"INSERT INTO students VALUES(" +
			info.id +
			", '" +
			info.name +
			"', '" +
			info.password +
			"');"
		;
		try {
			CourseSelectionSystem.get_statement().execute(sql);
		} catch (java.sql.SQLException ex) {
			CourseSelectionSystem.send_message("Failed to add student.");
		}
	}
	
	private void delete_student(Student student) {
		student.delete_student();
	}
	
}
