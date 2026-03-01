package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/alwinsden/synapse-ai/server/internal/cache"
	"github.com/alwinsden/synapse-ai/server/internal/handlers"
	"github.com/alwinsden/synapse-ai/server/internal/middleware"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
)

func main() {
	err := godotenv.Load(".env")
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	//configure secret keys
	cfgPort := os.Getenv("PORT")
	cfgValkey := os.Getenv("VALKEY_ADDR")
	cfgGClient := os.Getenv("GOOGLE_CLIENT_ID")
	cfgPgClient := os.Getenv("PG_CLIENT")
	cfgDbName := os.Getenv("PG_DATABASE")
	cfgPswd := os.Getenv("PG_PSWD")

	/*start postgres connection*/
	dsn := fmt.Sprintf("user=%s dbname=%s password=%s", cfgPgClient, cfgDbName, cfgPswd)
	db, err := sql.Open("postgres", dsn)
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	valkey := cache.New(cfgValkey)
	defer valkey.Close()

	h := &handlers.Handler{
		Valkey:         valkey,
		GoogleClientId: cfgGClient,
		PsqlDb:         db,
	}

	mux := http.NewServeMux()
	mux.HandleFunc("/health", h.Health)
	mux.HandleFunc("/generate-nonce", h.GenerateNonce)
	mux.HandleFunc("/login", h.Login)

	handler := middleware.CORS(mux)

	addr := fmt.Sprintf(":%s", cfgPort)
	log.Printf("Server starting on %s", addr)
	if err := http.ListenAndServe(addr, handler); err != nil {
		log.Fatalf("Server failed: %v", err)
	}
}
