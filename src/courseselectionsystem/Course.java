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
public class Course {
	private int m_id;
	
	public static class Lesson {
		public byte dayOfWeek;
		public byte lessonOfDay;
	}
	
	public int getId() {
		return m_id;
	}
	
	public void setId(int id) {
		m_id = id;
	}
	
}
