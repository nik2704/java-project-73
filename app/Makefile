setup:
	gradle wrapper --gradle-version 7.4

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew install

start-dist:
	./build/install/app/bin/app

generate-migrations:
	gradle diffChangeLog

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

.PHONY: build

#setup:
#	gradle wrapper --gradle-version 7.4
#
#clean:
#	./app/gradlew clean
#
#build:
#	./app/gradlew clean build
#
#run:
#	./app/gradlew run
#
#lint:
#	./app/gradlew checkstyleMain
#
#install: clean
#	./app/gradlew install
#
#run-dist:
#	./app/build/install/app/bin/app
#
#check-updates:
#	./app/gradlew dependencyUpdates
#
#.PHONY: build
