CREATE DATABASE course_selection;
USE course_selection;

CREATE TABLE courses(
	course_id BIGINT PRIMARY KEY,
	course_name VARCHAR(64) NOT NULL,
	teacher_id BIGINT NOT NULL,
	capacity INT NOT NULL,
	begin_week TINYINT NOT NULL,
	end_week TINYINT NOT NULL
);

CREATE TABLE teachers(
	teacher_id BIGINT PRIMARY KEY,
	teacher_name VARCHAR(32) NOT NULL,
	password VARCHAR(32) NOT NULL
);

CREATE TABLE students(
	student_id BIGINT PRIMARY KEY,
	student_name VARCHAR(32) NOT NULL,
	password VARCHAR(32) NOT NULL
);

CREATE TABLE lessons(
	course_id BIGINT NOT NULL,
	day_of_week TINYINT NOT NULL,
	lesson_of_day TINYINT NOT NULL
);

CREATE TABLE selections(
	course_id BIGINT NOT NULL,
	student_id BIGINT NOT NULL
);

INSERT INTO courses VALUES(1, 'Software Engineering', 1, 80, 5, 15);
INSERT INTO teachers VALUES(1, 'Chen Qing', 'cqpw');
INSERT INTO students VALUES(1, 'Tian Zichen', 'tzcpw');
INSERT INTO lessons VALUES(1, 1, 1);
INSERT INTO lessons VALUES(1, 3, 1);
INSERT INTO lessons VALUES(1, 5, 1);
INSERT INTO selections VALUES(1, 1);