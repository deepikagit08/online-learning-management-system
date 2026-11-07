# Online Learning Management System (LMS)

A role-based web application built with **Core Java, JDBC, Servlets and JSP** on **MySQL**, deployed on Apache Tomcat 9.

## Problem Statement
Colleges need a simple platform where instructors publish courses and lessons, students enrol and track progress,
and assignments are submitted and graded online, all managed by an administrator.

## Features
| Role | Capabilities |
|------|--------------|
| Student | Register/login, browse & enrol in courses, read lessons, mark lessons complete, see progress %, submit/re-submit assignments, view grades |
| Instructor | Create courses, add lessons, create assignments, view submissions, grade them |
| Admin | View all users and courses, delete users/courses |

## Architecture (MVC + DAO)
```
Browser -> AuthFilter -> Servlet (Controller) -> DAO (JDBC) -> MySQL
                              |
                              +-> JSP + JSTL (View)
```
| Package | Purpose |
|---------|---------|
| `model` | Entities: abstract `User` with `Admin`/`Instructor`/`Student`, `Course`, `Lesson`, `Assignment`, `Submission` |
| `dao` | JDBC data access, generic `Dao<T>` interface |
| `servlet` | `AuthServlet`, `DashboardServlet`, `CourseServlet`, `AssignmentServlet`, `AdminServlet` |
| `filter` | `AuthFilter` for authentication and role-based authorization |
| `util` / `exception` | `DBConnection`, `PasswordUtil`, `LmsException` |

## Mapping to the GUVI marking rubric
- **Problem Understanding & Solution Design** - three roles, normalised 7-table schema, MVC/DAO layering (this README + `sql/schema.sql`).
- **Core Java Concepts** - abstract class + inheritance + polymorphism (`User` hierarchy), interface + generics (`Dao<T>`), collections (`List`, `Set`, `LinkedHashMap`), custom exception (`LmsException`), try-with-resources, switch expressions, encapsulation, hashing (`MessageDigest`).
- **Database Integration (JDBC)** - `DriverManager`, `PreparedStatement` (SQL-injection safe), `ResultSet`, JOINs, sub-queries, generated keys, `ON DUPLICATE KEY UPDATE`, foreign keys with cascade.
- **Servlets & Web Integration** - `@WebServlet` controllers with `doGet`/`doPost`, `HttpSession`, `RequestDispatcher` forward, redirect (Post/Redirect/Get), `@WebFilter`, `web.xml` error pages, JSP + JSTL views.

## Setup
**Prerequisites:** JDK 17, Maven, MySQL 8, Apache Tomcat 9 (javax.servlet; not Tomcat 10).

1. Create the database: `mysql -u root -p < sql/schema.sql`
2. Edit `src/main/resources/db.properties` with your MySQL username/password.
3. Build: `mvn clean package` -> produces `target/lms.war`
4. Copy `lms.war` to `<tomcat>/webapps/` and start Tomcat (or use the Smart Tomcat plugin in IntelliJ / Servers view in Eclipse).
5. Open http://localhost:8080/lms/

**Default admin:** `admin@lms.com` / `admin123`. Register new accounts as Student or Instructor from the Register page.

## Suggested demo flow
1. Register an instructor -> create a course -> add lessons -> create an assignment.
2. Register a student -> enrol -> mark lessons complete -> submit the assignment.
3. Log back in as instructor -> View submissions -> grade.
4. Student dashboard shows progress % and marks. Admin panel lists/deletes users and courses.

## Security notes
Passwords are salted-SHA-256 hashed, all SQL uses prepared statements, output is escaped with `<c:out>`, and `AuthFilter` enforces roles server-side. For production use BCrypt and CSRF tokens.
