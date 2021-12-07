create table course
(
    course_id  int auto_increment,
    name       varchar(255) not null,
    start_date datetime     not null,
    constraint course_id
        unique (course_id)
);

alter table course
    add primary key (course_id);

create table exam
(
    exam_id   int auto_increment,
    name      varchar(255) not null,
    exam_date datetime     not null,
    course_id int          not null,
    constraint exam_id
        unique (exam_id),
    constraint fk_course_to_exam
        foreign key (course_id) references course (course_id)
);

alter table exam
    add primary key (exam_id);

create table student
(
    student_id int auto_increment,
    name       varchar(255) not null,
    age        int          null,
    constraint student_id
        unique (student_id)
);

alter table student
    add primary key (student_id);

create table student_exams
(
    student_id int           not null,
    exam_id    int           not null,
    score      int default 0 null,
    feedback   text          null,
    constraint fk_exam_to_student
        foreign key (exam_id) references exam (exam_id),
    constraint fk_student_to_exam
        foreign key (student_id) references student (student_id)
);



SELECT *
FROM student_exams
         INNER JOIN student ON student_exams.student_id = student.student_id
         INNER JOIN exam ON student_exams.exam_id = exam.exam_id;

