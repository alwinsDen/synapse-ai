package handlers

import (
	"github.com/alwinsden/synapse-ai/server/internal/cache"
	"gorm.io/gorm"
)

type Handler struct {
	Valkey         *cache.Valkey
	GoogleClientId string
	PsqlDb         *gorm.DB
}
