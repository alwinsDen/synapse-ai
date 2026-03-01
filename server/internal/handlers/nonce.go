package handlers

import (
	"context"
	"net/http"
	"time"

	"github.com/google/uuid"
)

func (h *Handler) GenerateNonce(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	nonce := uuid.New().String()
	err := h.Valkey.Set(ctx, nonce, "1", 60*time.Second)
	if err != nil {
		http.Error(w, "Failed to store nonce", http.StatusInternalServerError)
		return
	}
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(nonce))
}
