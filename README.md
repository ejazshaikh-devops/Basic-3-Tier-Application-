# 3-Tier-web-application-Student-Attendance-System-
Building a 3-tier web application from scratch with complete backend and frontend 

1. Architchture :

                  
                    USER
                     │
                     ▼
                  React Ui 
                     │
                     ▼
                 Spring Boot REST API   
                     │
                     ▼
                  Hibernate / JPA 
                     │
                     ▼
                 MariaDB (AWS RDS) 



2. Full Infrastructure Architecture :


                    USER
                     │
                     ▼
               Web Browser
                     │
                     │ HTTP
                     ▼
             Public EC2 IP
            (51.xx.xx.xx)
                     │
        ┌────────────┴────────────┐
        │                         │
        ▼                         ▼ 
       React Frontend             Spring Boot API
       (Attendance Dashboard)       Port 8080
       Student UI
       Teacher UI
        │
        │ REST API
        ▼
        Spring Controllers
        │
        ▼
        Service Layer
        │
        ▼
        JPA Repository
        │
        ▼
        Hibernate ORM
        │
        ▼
        MariaDB (AWS RDS)
        │
        ▼
        Attendance Tables
        Student Tables 

3. Backend Internel Architecture :

       backend/
       |
       |-controller/
       StudentController
       AttendanceController
       ClassStatsController
       |
       |-model/
       Student.java
       Attendance.java
       |
       |-repository/
       StudentRepository
       AttendanceRepository
       |
       └──resources/
        application.properties

4. Frontend Internal Architecture :

   
       frontend/
        │
        ├── src/
        │     main.jsx
        │     index.css
        │
        ├── components (future)
        │     StudentTable
        │     Dashboard
        │     Login
        │
        └── build/
              dist/

 5. Security Layer (current) :
NOTE : This feature and more other features are in production and will update soon
<br>
<h6>Update :This feature is now added to see the latest version click : https://github.com/ejazshaikh-devops/Student-Attendance-v2<h6></h6>

Teacher access → full control
 <br>
Student access → read only

6. Deployment Architecture : 

       AWS Cloud
       │
       ├── EC2
       │    ├── React Frontend
       │    └── Spring Boot Backend
       │
       └── RDS
          └── MariaDB Database



7. Process Of Creation :
<br>
   Overview :
<br>
  Project: Batch 5 Attendance System
  <br>
  Architecture: 3-Tier Web Application

  Frontend: React (UI) 
  <br>
  Backend: Spring Boot (Java REST API)
  <br>
  Database: AWS RDS MariaDB
  <br>
  Infrastructure: AWS EC2 (Ubuntu)

1. Necessary Process :
   <br>
    1) Database Creation :
      <br>
       mysql -h RDS-ENDPOINT -u admin -p
      <br>
       CREATE DATABASE attendance;
      <br>
       spring.jpa.hibernate.ddl-auto=update
      <br>
       student
       attendance
      
   3) Backend Structure :
      <br>
      AttendanceApp/backend
      <br>
      Created Maven project structure:

           backend
            ├ pom.xml
            └ src
               └ main
                  ├ java
                  │   └ com
                  │       └ attendance
                  │            └ app
                  │               ├ controller
                  │               ├ model
                  │               ├ repository
                  │               └ service
                  └ resources
                  └ application.properties      

   pom.xml : Backend build file defines project dependencies contaions : spring boot, JPA, MariaDB Driver it will used by mvn clean packege. Which will give the output of : target/attendance-backend.jar


   3) Frontend Structure :
    <br>
     AttendanceApp/frontend

          frontend
          └ attendance-frontend
          ├ src
          │  ├ main.jsx
          │  └ index.css
          ├ package.json
          └ vite.config.js
      
main.jsx (UI LOGIC)
<br>
Responsibilities:
<br>
1) React app entry
2) API calls
3) UI rendering
   <br>
   Main Logic:
   <br>
   fetch(API/students)
   <br>
   POST(API/students)
   <br>
   DELETE(API/students/{id})

Implemented Features :
<br>
add student
<br>
delete student
<br>
attendance toggle
<br>
weekly attendance
<br>
percentage calculation
<br>
performance score
<br>

 Ui Features : 
 <br>
 Teacher Panel : Name, Batch, Course.
 <br>
 Students Control : Add Student, Remove Student.
 <br>
 Attendance Table : Weekes.
 <br>
 Analytics : Attendence %, Performance Score.


 8. After Deployment :

        UI action
        ↓
        React API call
        ↓
        Spring Controller
        ↓
        Repository
        ↓
        Hibernate ORM
        ↓
        MariaDB database

Example: Mark attendance

<br>
UI → POST /attendance
<br>
Controller → save()
<br>
Repository → SQL insert
<br>
Database → attendance table  
<br>

9. Debugging :

    1) Port 8080 already in use
       <br>
       Problem : Previous Java process still running in background
       <br>
       Debugg : lsof -i :8080
       <br>
       ps aux | grep java found the process then
       <br>
       kill -9 'process id'

    2) RDS “Access Denied” Error
       <br>
       Problem : Access denied for user 'admin'
       <br>
       Debugg : Verified application.properties correct credentials + open port 3306 in RDS SG

    3) Java Error
       <br>
       Problem : the JDK version 17 requiered version 20
       <br>
       Debugg : dpkg --list | grep openjdk found openjdk-17-jdk than apt remove openjdk-17-jdk -y than apt autoremove -y to remove leftover dependencies than installed JDK version 20

    4) Node version Incompatibility
       Problem : vite error : crypto.hash is not a function
       <br>
       Debugg : Node 18 installed vite required Node 20+ so installed new version

    5) Foreign Key Constraint on Delete
       Problem : Deleting student caused 500 error attendance table had FK reference to student fail to delete any student
       <br>
       Debugg : @OneToMany(mappedBy="student",
       <br>
       cascade=CascadeType.ALL,
       <br>
       orphanRemoval=true)
       <br>
       Relational integrity + cascading deletes

    6) Hibernate Dialect Not Determine
       <br>
       Problem : Unable to determine dialect DB connection failed so hibernate couldn’t detect DB type
       <br>
       Debugg : Issue in DB password corrected the password

    7) Whitelabel Error Page (404)
       <br>
       Problem : Accessed root / on backend no controller mapped to /
       <br>
       Debugg : Tested proper endpoint /student

    8) Backend Stops After SSH Close
       <br>
       Problem : Closing terminal stopped backend process tied to SSH session
       <br>
       Debugg : Run the process in background
       <br>
       nohup java -jar app.jar > app.log 2>&1 &

    9) CORS Issues
        <br>
       Problem : Frontend couldn’t call backend
       <BR>
       Debugg : @CrossOrigin

    10) Build Failure (Compilation Error)
        <br>
        Problem : cannot find symbol reached end of file while parsing becoz of missing braces / duplicate methods
        <br>
        Debugg : Repaired class structure

    11) Maven Dependency Issues
        <br>
        Problem : Build failure due to missing dependencies
        <br>
        Debugg : Ensured pom.xml correct
        <br>
        mvn clean package

    12) Database SSL Warning
        <br>
        Problem : useSsl deprecated outdated connection parameters
        <br>
        Debugg : Updated connection string to newer format

    13) React build errors
        <br>
        Problem : missing package.json
        <br>
        Debugg : recreated Vite project
        
10. Future Use : If expanded further this project becomes:
<br>
1. Role based login
<br>
2. Student portal
<br>
3. File upload
<br>
4. Graph analytics
<br>
5. JWT authentication

   <h3>UI<h3></h3>
   <img width="1024" height="666" alt="Screenshot 2026-05-30 at 2 25 55 AM" src="https://github.com/user-attachments/assets/a3cebac3-aa82-460f-a60e-b2560b9ee83a" />



        
       
       

       
       
    



   
