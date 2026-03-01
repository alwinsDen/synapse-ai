package handlers

import (
	"database/sql"

	"github.com/alwinsden/synapse-ai/server/internal/cache"
)

type Handler struct {
	Valkey         *cache.Valkey
	GoogleClientId string
	PsqlDb         *sql.DB
}
