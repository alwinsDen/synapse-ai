package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/alwinsden/synapse-ai/server/internal/auth/table"
	"github.com/alwinsden/synapse-ai/server/internal/cache"
	"github.com/alwinsden/synapse-ai/server/internal/handlers"
	"github.com/alwinsden/synapse-ai/server/internal/middleware"
	"github.com/joho/godotenv"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func main() {
	err := godotenv.Load("secret.properties")
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	//configure secret keys
	var (
		cfgPort      = os.Getenv("GO_PORT")
		cfgValkey    = os.Getenv("GO_VALKEY_ADDR")
		cfgGClient   = os.Getenv("CLIENT_ID_GOOGLE_AUTH")
		psqlHost     = os.Getenv("GO_PG_HOST")
		psqlUserName = os.Getenv("GO_PG_USERNAME")
		psqlDbName   = os.Getenv("GO_PG_DATABASE")
		psqlPassword = os.Getenv("GO_PG_PASSWORD")
		psqlPort     = os.Getenv("GO_PG_PORT")
	)

	postgresDsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		psqlHost,
		psqlUserName,
		psqlPassword,
		psqlDbName,
		psqlPort,
	)

	db, err := gorm.Open(postgres.New(postgres.Config{
		DSN:                  postgresDsn,
		PreferSimpleProtocol: true,
	}))

	if err != nil {
		log.Fatal(err)
	}

	/*migrations*/
	err = db.AutoMigrate(auth.UserInfo{})
	if err != nil {
		log.Fatalf("Failed to run some migrations: %s", err)
	}

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
