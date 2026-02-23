package auth

import (
	"context"

	"google.golang.org/api/idtoken"
)

func VerifyGoogleIDToken(ctx context.Context, token string, clientID string) (bool, error) {
	payload, err := idtoken.Validate(ctx, token, clientID)
	if err != nil {
		return false, err
	}

	if payload.Claims["aud"] != clientID {
		return false, nil
	}

	return true, nil
}
