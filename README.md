# direct-axis-racing-demo
Direct Axis Racing console demo app

Development tools and libraries used to create and build the project.
=====================================================================
- Java:      1.8
- IDE:       Eclipse (Ver 2019-09 R (4.13.0))
- Maven:     4.0
- Hibernate: 5.3.7.Final
- JPA:	   2.2
- MySql:     5.7

PRE-REQUISITES:
===============
1.	Run the DB setup script (in github_repo/db_scripts) to create the required DB and tables
2.	Run the Table setup script to initialize the DB
3.	Unzip project and update DB details accordingly in ~\resources\META-INF\persistence.xml
4.  Ensure the correct java version is used and it's accessible system-wide.

Steps to build project in Eclipse:
==================================
1.	Import the project into eclipse
2.	Update the project as following:
	Right click on the Project -> Maven -> Update Project...
3.	Build the project as following
	Right click the project -> Run As -> Maven build... -> Set Goals as "clean install" -> Run

To run the project in Eclipse:
==============================
1.	Right click on the project -> Run As -> Java Application

To run the project outside of eclise:
=====================================
1.  Navigate to: ~\.m2\repository\da\demo\racing\1
2.  java -jar racing-1.jar
