package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/alwinsden/synapse-ai/server/internal/cache"
	"github.com/alwinsden/synapse-ai/server/internal/handlers"
	"github.com/alwinsden/synapse-ai/server/internal/middleware"
	"github.com/joho/godotenv"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	//configure secret keys
	cfg_port := os.Getenv("PORT")
	cfg_valkey := os.Getenv("VALKEY_ADDR")
	cfg_g_client := os.Getenv("GOOGLE_CLIENT_ID")

	valkey := cache.New(cfg_valkey)
	defer valkey.Close()

	mux := http.NewServeMux()
	mux.HandleFunc("/health", handlers.Health)
	mux.HandleFunc("/generate-nonce", handlers.GenerateNonce(valkey))
	mux.HandleFunc("/login", handlers.Login(valkey, cfg_g_client))

	handler := middleware.CORS(mux)

	addr := fmt.Sprintf(":%s", cfg_port)
	log.Printf("Server starting on %s", addr)
	if err := http.ListenAndServe(addr, handler); err != nil {
		log.Fatalf("Server failed: %v", err)
	}
}
