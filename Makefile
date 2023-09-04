PROJECT_VERSION := 1.0.0

kill_process_on_5000:
	@echo "\nKilling any process running on 5000..."
	@lsof -i :5000 | awk 'NR==2 {print $$2}' | xargs -r kill

clean:
	@echo "\nCleaning...\n"
	@cd ./server && mvn clean > cleanOutput.txt 2>&1

build-test-server: kill_process_on_5000 clean
	@echo "Package Server For Testing..."
	@cd ./server && mvn package -DskipTests

run-tests:
	@echo "\nRunning Tests Against Server...\n"
	cd ./server && mvn test -Dtest=*LoginTest
	cd ./server && mvn test -Dtest=*LoginTest2
	cd ./server && mvn test -Dtest=*LoginTest3
	cd ./server && mvn test -Dtest=*RegistrationTest
	cd ./server && mvn test -Dtest=*GetMessagesTest
	@echo "\nStopping Server...\n"
	@pkill -f server-${PROJECT_VERSION}.jar

start-test-server:
	@echo "\nStarting Server..."
	java -jar ./server/target/server-${PROJECT_VERSION}.jar > serverOutput.txt 2>&1 &

run-tests-against-server: kill_process_on_5000 build-test-server start-test-server run-tests
	@echo "Testing Successful!"

build-server: build-test-server run-tests-against-server
	@echo "\nServer Build Successful!"