package config

import "os"

type Config struct {
	Port           string
	GoogleClientID string
	ValkeyAddr     string
}

func Load() *Config {
	return &Config{
		Port:           getEnv("PORT", "3001"),
		GoogleClientID: getEnv("GOOGLE_CLIENT_ID", ""),
		ValkeyAddr:     getEnv("VALKEY_ADDR", "localhost:6379"),
	}
}

func getEnv(key, defaultVal string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultVal
}
