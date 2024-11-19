#!/bin/sh
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

kill_port_8080() {
  PID=$(lsof -t -i:8080)

  if [ -n "$PID" ]; then
    echo "\n${BLUE}Port 8080 is in use by PID $PID. Stopping the process...${NC}"
    kill -9 $PID
    echo "${BLUE}Process $PID has been stopped.${NC}"
  fi
}

stop_spring_boot() {
  echo "\n\n${YELLOW}Spring Boot application finished successfully! Stopping Spring Boot...${NC}"
  kill "$SPRING_BOOT_PID" 2>/dev/null
  kill_port_8080
}

monitor_parent_process() {
  while kill -0 "$PPID" >/dev/null 2>&1; do
    sleep 1
  done
  
  stop_spring_boot
}

# Handles control + c (SIGINT) and termination (SIGTERM)
trap stop_spring_boot INT TERM

kill_port_8080

# Runs Gradle bootRun task in the background
./gradlew --quiet bootRun &
SPRING_BOOT_PID=$!

# Start monitoring parent process in the background
monitor_parent_process &

sleep 0.1

echo "\n* Running on ${BLUE}http://localhost:8080${NC}\n"

# Wait for the Spring Boot process to finish
wait "$SPRING_BOOT_PID"
