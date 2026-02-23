.PHONY: run build test docker-up docker-down tidy

run:
	go run ./cmd/api

build:
	go build -o bin/api ./cmd/api

test:
	go test ./...

tidy:
	go mod tidy

docker-up:
	docker-compose up --build

docker-down:
	docker-compose down
