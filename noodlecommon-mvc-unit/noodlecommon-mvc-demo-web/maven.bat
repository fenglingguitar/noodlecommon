call cd D:\work\source-my\noodlecommon\noodlecommon
call mvn install -P=dev -Dmaven.test.skip=true
call cd D:\work\source-my\noodlecommon\noodlecommon\noodlecommon-mvc-unit\noodlecommon-mvc-demo-web
call mvn dependency:copy-dependencies -DoutputDirectory=D:\work\source-my\noodlecommon\noodlecommon\noodlecommon-mvc-unit\noodlecommon-mvc-demo-web\src\main\webapp\WEB-INF\lib