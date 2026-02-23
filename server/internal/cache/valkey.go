package cache

import (
	"context"
	"time"

	"github.com/redis/go-redis/v9"
)

type Valkey struct {
	client *redis.Client
}

func New(addr string) *Valkey {
	client := redis.NewClient(&redis.Options{
		Addr: addr,
	})
	return &Valkey{client: client}
}

func (v *Valkey) Set(ctx context.Context, key string, value string, ttl time.Duration) error {
	return v.client.Set(ctx, key, value, ttl).Err()
}

func (v *Valkey) Get(ctx context.Context, key string) (string, error) {
	return v.client.Get(ctx, key).Result()
}

func (v *Valkey) Delete(ctx context.Context, key string) error {
	return v.client.Del(ctx, key).Err()
}

func (v *Valkey) Close() error {
	return v.client.Close()
}
