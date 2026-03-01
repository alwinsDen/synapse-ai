package auth

import (
	"context"

	auth "github.com/alwinsden/synapse-ai/server/internal/auth/table"
	"google.golang.org/api/idtoken"
	"gorm.io/gorm"
)

func VerifyGoogleIDToken(ctx context.Context, token string, clientID string, db *gorm.DB) (bool, error) {
	payload, err := idtoken.Validate(ctx, token, clientID)
	if err != nil {
		return false, err
	}

	if payload.Claims["aud"] != clientID {
		return false, nil
	}

	var (
		claimsEmail       = payload.Claims["email"].(string)
		claimsProfile     = payload.Claims["picture"].(string)
		claimsGoogleId    = payload.Claims["sub"].(string)
		claimsAccessState = payload.Claims["email_verified"].(bool)
	)

	err = gorm.G[auth.UserInfo](db).Create(ctx, &auth.UserInfo{
		AccessState: claimsAccessState,
		GoogleId:    claimsGoogleId,
		Email:       claimsEmail,
		Profile:     claimsProfile,
	})

	if err != nil {
		return false, err
	}

	return true, nil
}
