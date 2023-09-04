PROJECT_VERSION := 1.0.0

kill_process_on_5000:
	@echo "\nKilling any process running on 5000..."
	@lsof -i :5000 | awk 'NR==2 {print $$2}' | xargs -r kill

clean:
	@echo "\nCleaning...\n"
	@cd ./server && mvn clean > cleanOutput.txt 2>&1

build-test-server: kill_process_on_5000 clean
	@echo "Package Server For Testing..."
	@cd ./server && mvn package -DskipTests -Dproject.version=${PROJECT_VERSION}-SNAPSHOT

run-tests-against-server: kill_process_on_5000
	@echo "\nStarting Server..."
	cd ./server && @java -jar target/server-${PROJECT_VERSION}-SNAPSHOT.jar > serverOutput.txt 2>&1 &
	@echo "\nRunning Tests Against Server...\n"
	cd ./server && mvn test -Dtest=*Test
	@echo "\nStopping Server...\n"
	@pkill -f server-${PROJECT_VERSION}-SNAPSHOT.jar

build-server: build-test-server run-tests-against-server
	@echo "Building Server..."
	@cd ./server && mvn package -DskipTests