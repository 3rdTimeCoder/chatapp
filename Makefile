PROJECT_VERSION := $(shell grep -oP '(?<=<project.version>)[^<]+' pom.xml)

kill_process_on_5000:
	@echo "\nKilling any process running on 5000..."
	@lsof -i :5000 | awk 'NR==2 {print $$2}' | xargs -r kill

build-server:
	@echo "Building Server..."
	cd ./server && mvn clean install


build-test-server: kill_process_on_5000 clean
	@echo "Packaging for dev..."
	@mvn package -DskipTests -Dproject.version=${PROJECT_VERSION}-SNAPSHOT
