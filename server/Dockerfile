FROM golang:1.23-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY server/ ./server/
RUN go build -o bin/api ./server/cmd/api

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/bin/api .

EXPOSE 3001

CMD ["./api"]
