Introduce
=====
This project is a dinner room management system. it contain three roles, the first is waiter, they can operate the order by a android app. the second is chef, they can get the ordered food by web, and if they finish a food, can send a notice to the waiter by web. the third is administrator, they operator the orders and do some other system management.<br/>
It use mysql to store the table: user、table、menu、option, and use mongodb to store order collection.<br/>
It use servlet to provide backgroud service in the tomcat container, make web page by JSP, and use JSON message to communicate with android app.<br/>

Directory
=====
Android - android app<br/>
Server/drhelper_db - mysql and mongodb database backup files<br/>
Server/tomcat - android and web backgroud service<br/>

Run
=====
You install the drhelper.apk after compile the android project, use it to provide waiter service.<br/>
You can access the root directory of this system in web browser, it will show the main page of this system.<br/>

Usage
====
1. after upload the sources, compile the Server directory project by Eclipse, and compile the Android directory project by adt-bundle.<br/>
2. install and deploy the Nignx and Tomcat in your server, config the Nginx to transfer all request to the Tomcat, meanwhile install and deploy the Mysql and Mongodb.<br/>
3. create a directory drhelper into the tomcat/webapps, then copy the files contained in the Server/tomcat into drhelper directory.<br/>
4. import the database backup files into Mysql and Mongodb at Server/drhelper_db directory.<br/>
5. install the drhelper.apk on the android mobile phones.<br/>
6. you can access the mobile service through the app, play a waiter role, and access web service through the browser, play a chef or administrator role.<br/>

Design
=====
Please refer to my blog(Chinese):<br/>
