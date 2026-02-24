.PHONY: run build test docker-up docker-down tidy

run:
	cd server && go run ./cmd/api

build:
	cd server && go build -o bin/api ./cmd/api

test:
	cd server && go test ./...

tidy:
	cd server && go mod tidy

docker-up:
	cd server && docker-compose up --build

docker-down:
	cd server && docker-compose down
