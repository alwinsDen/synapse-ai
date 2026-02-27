.PHONY: run build test tidy docker-up docker-down

run:
	go run ./server/cmd/api

build:
	go build -o bin/api ./server/cmd/api

test:
	go test ./server/...

tidy:
	go mod tidy

docker-up:
	docker-compose up --build

docker-down:
	docker-compose down
