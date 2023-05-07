# Java Spring Boot Project

![spring boot logo](src/main/resources/assets/spring-boot.png "spring boot logo")

## Table of Contents

- [About the Project](#about-the-project)
- [Built With](#built-with)
- [Explanation](#explanation)
- [Teacher's Accounts](#teachers-accounts)
- [Run the Project](#run-the-project)
- [Routes Get](#routes-get)
  - [Login to Get Token](#login-to-get-token)
  - [Get All Users](#get-all-users)
  - [Get All Teachers](#get-all-teachers)
  - [Get All Students](#get-all-students)
  - [Get User by Email](#get-user-by-email)
  - [Get All Courses](#get-all-courses)
  - [Get Course by ID](#get-course-by-id)
- [Routes Post](#routes-post)
  - [Save New Student](#save-new-student)
  - [Save New Course](#save-new-course)
  - [Subscribe Student to a Course](#subscribe-student-to-a-course)
  - [Unsubscribe Student from a Course](#unsubscribe-student-from-a-course)
- [Routes Put](#routes-put)
  - [Edit Student](#edit-student)
  - [Edit Course](#edit-course)
- [Routes Delete](#routes-delete)
  - [Delete Student](#delete-student)
  - [Delete Course](#delete-course)
- [Clone the Repo](#clone-the-repo)
- [Contact](#contact)


## About-the-project

This is a spring boot project building an API connected to a database mysql.

## 🛠 Built with

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
* ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
* ![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
* <img src="https://www.docker.com/wp-content/uploads/2022/03/horizontal-logo-monochromatic-white.png" alt="Docker" style="width: 130px; height: auto;">

## Explanation
This project is a structure of 3 entities (Teachers, Students and Courses) with relationship between them.

There are by default 2 existing teachers who can:
* show users and courses
* save students and courses
* edit students and courses
* delete students and courses
* subscribe students to courses
* unsubscribe students from courses

There are students added by a teacher who can:
* show users and courses
* edit his profile

## Teacher's accounts
* teacher 1: "email":"profA@gmail.com", "password":"profA"
* teacher 2: "email":"profB@gmail.com", "password":"profB"

## Run the project
Atfer cloning the repository, you just have to type the following 2 commands (in the root of the project):

    docker-compose build
    
    docker-compose up -d
    
:warning: : you must have dcoker installed in your machine, if not:
* [Install docker for windows](https://docs.docker.com/desktop/install/windows-install/)
* [Install docker for mac](https://docs.docker.com/desktop/install/mac-install/)
* [Install docker for linux](https://docs.docker.com/desktop/install/linux-install/)

:warning: : don't forget to add the environment path for docker after installing:
* Windows
    Powershell:
    ```gitbash
    set Path=%Path%;C:\Program Files\Docker\Docker\resources\bin
    ```
* Linux
    bash:
    ```gitbash
    echo 'export PATH="/usr/local/bin:$PATH"' >> ~/.bashrc && source ~/.bashrc
    ```
* Mac
    bash:
    ```gitbash
    echo 'export PATH="/usr/local/bin:$PATH"' >> ~/.bash_profile && source ~/.bash_profile
    ```
Restart the machine and verify with: docker --version

## Routes Get
### Login to get token
* Method: GET
* Request url: /auth/login
* PathVariable: not required
* RequestBody (Body raw JSON): {
    "email":"", 
    "password":""
}
* Authorization (Bearer token): not required

### Get all users
* Method: GET
* Request url: /users/allUsers
* PathVariable: not required
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

### Get all teachers
* Method: GET
* Request url: /users/allTeachers
* PathVariable: not required
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

### Get all students
* Method: GET
* Request url: /users/allStudents
* PathVariable: not required
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

### Get user by email
* Method: GET
* Request url: /users/user/{email}
* PathVariable: {email} (example: /users/user/example@gmail.com)
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

### Get all courses
* Method: GET
* Request url: /courses/allCourses
* PathVariable: not required
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

### Get course by id
* Method: GET
* Request url: /courses/course/{idCourse}
* PathVariable: {idCourse} (example: /courses/course/23)
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

## Routes Post
### Save new student
* Method: POST
* Request url: /users/saveStudent
* PathVariable: not required
* RequestBody (Body raw JSON): {
    "email":"",
    "password":"",
    "lastname":"",
    "firstname":""
}
* Authorization (Bearer token): required (token getting from /auth/login)

### Save new course
* Method: POST
* Request url: /courses/saveCourse
* PathVariable: not required
* RequestBody (Body raw JSON): {
    "title":"Java",
    "studentEmails":["ex1@gmail.com", "ex2@gmail.com"] (studentEmails = not mandatory, can be null, is array of existing students's emails)
}
* Authorization (Bearer token): required (token getting from /auth/login)

### Subscribe student to a course
* Method: POST
* Request url: /courses/subscribeStudent/{idCourse}
* PathVariable: {idCourse} (example: /courses/subscribeStudent/42)
* RequestBody (Body raw JSON): {
    "studentEmail": ""
}
* Authorization (Bearer token): required (token getting from /auth/login)

### Unsubscribe student from a course
* Method: POST
* Request url: /courses/unsubscribeStudent/{idCourse}
* PathVariable: {idCourse} (example: /courses/unsubscribeStudent/42)
* RequestBody (Body raw JSON): {
    "studentEmail": ""
}
* Authorization (Bearer token): required (token getting from /auth/login)

## Routes Put
### Edit student
* Method: PUT
* Request url: /users/editStudent/{email}
* PathVariable: {email} (example: /users/editStudent/example@gmail.com)
* RequestBody (Body raw JSON): {
    "email":"",
    "password":"",
    "lastname":"",
    "firstname":""
} (you do not have to pass these 4 bodies, at least 1 is enough)
* Authorization (Bearer token): required (token getting from /auth/login)

### Edit course
* Method: PUT
* Request url: /courses/editCourse/{idCourse}
* PathVariable: {idCourse} (example: /courses/editCourse/42)
* RequestBody (Body raw JSON): {
    "title":"JS"
}
* Authorization (Bearer token): required (token getting from /auth/login)

## Routes Delete
### Delete student
* Method: DELETE
* Request url: /users/deleteStudent/{email}
* PathVariable: {email} (example: /users/deleteStudent/example@gmail.com)
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

### Delete course
* Method: DELETE
* Request url: /courses/deleteCourse/{idCourse}
* PathVariable: {idCourse} (example: /courses/deleteCourse/25)
* RequestBody (Body raw JSON): not required
* Authorization (Bearer token): required (token getting from /auth/login)

## Clone the repo

* Clone the repository:
    ```gitbash
    git clone git@github.com:ElmiriYounes/JavaSpringBoot.git
    ```

## Contact

El miri younes - elmiri.younes@hotmail.com