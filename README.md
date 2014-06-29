Codefest 2014 team A repository
===========

# The team A


TEAM MEMBER | EXO TEAM
------------ | ------------- 
Nguyen Anh Vu | PLF/ECMS
Nguyen Truong Giang | UI
Le Thi Thu Ha | PLF/ECMS
Dang Thi May | PLF/ECMS

# How to build

	git clone git@github.com:exo-codefest/2014-team-A.git
	cd 2014-team-A
	mvn clean install


# Overview
* Name: Tasks Management Add-on
* Purposes: Provide an effective tool which facilitates users to manage personal and group projects, tasks and is integratable easily to Platform

# Definitions
- A project could have a member (personal project) or many members (group project)
- A project is organized in many tasks
- One or many members in project are going to be assigned to perform tasks
- A task could have many state (open, inprogress, pending, done, etc.). The state of tasks is updated by members.
- States are configurable and customizable by users.
- Only members of a project can see the project and its tasks on his board.

# Features
- Create new projects
- Add members to project
- Search projects
- Add new state/task to project
- Configure Tasks (name, assignees, due date, attachments, type, etc.)
- Change state of task
- Search task
- Remove state/task from project

# How to use

# Improvements
Because development time is quite limited so we decided to implement the add-on as a "proof of concept" with basic functions. The add-on certainly could evolve more with many interesting and potential features. We list some perspectives below:
* Notification for members by email
* Import projects and tasks from other system (Trello, basecamp, google, etc.)
