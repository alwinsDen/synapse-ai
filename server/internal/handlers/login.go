package handlers

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/alwinsden/synapse-ai/server/internal/auth"
	"github.com/alwinsden/synapse-ai/server/internal/cache"
	"github.com/redis/go-redis/v9"
)

type LoginRequest struct {
	Token string `json:"token"`
	Nonce string `json:"nonce"`
}

type LoginResponse struct {
	Status string `json:"status"`
}

type ErrorResponse struct {
	Error string `json:"error"`
}

func Login(valkey *cache.Valkey, clientID string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			return
		}

		var req LoginRequest
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusBadRequest)
			err := json.NewEncoder(w).Encode(ErrorResponse{Error: "Invalid request"})
			if err != nil {
				return
			}
			return
		}

		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()

		_, err := valkey.Get(ctx, req.Nonce)
		if err == redis.Nil {
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusUnauthorized)
			err := json.NewEncoder(w).Encode(ErrorResponse{Error: "Invalid or expired nonce"})
			if err != nil {
				return
			}
			return
		}
		if err != nil {
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusInternalServerError)
			err := json.NewEncoder(w).Encode(ErrorResponse{Error: "Cache error"})
			if err != nil {
				return
			}
			return
		}

		valid, err := auth.VerifyGoogleIDToken(ctx, req.Token, clientID)
		if err != nil || !valid {
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusUnauthorized)
			err := json.NewEncoder(w).Encode(ErrorResponse{Error: "Invalid token"})
			if err != nil {
				return
			}
			return
		}

		valkey.Delete(ctx, req.Nonce)

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		err = json.NewEncoder(w).Encode(LoginResponse{Status: "ok"})
		if err != nil {
			return
		}
	}
}
