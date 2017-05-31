/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionsystem;

/**
 *
 * @author 堃
 */
public class AdminEntry {
	public long m_id = 1;
	public String m_password = "";
	
	public static class UserInfo {
		public long id;
		public String password;
	}
	
	public static interface LoginHandler {
		public abstract UserInfo handle();
	}
	
	public static interface FunctionChoiceHandler {
		public abstract Function handle();
	}
	
	public static enum Function {
		exit_entry,
		teacher,
		student,
		unknown
	}
	
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
	}
	
	private void view_students() {
	}
	
}
