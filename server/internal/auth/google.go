package auth

import (
	"context"
	"net/http"

	auth "github.com/alwinsden/synapse-ai/server/internal/auth/table"
	"google.golang.org/api/idtoken"
	"gorm.io/gorm"
)

func VerifyGoogleIDToken(ctx context.Context, token string, clientID string, db *gorm.DB, headers http.Header) (bool, error) {
	payload, err := idtoken.Validate(ctx, token, clientID)
	if err != nil {
		return false, err
	}

	if payload.Claims["aud"] != clientID {
		return false, nil
	}

	claimsGoogleId, subIdState := payload.Claims["sub"].(string)
	if !subIdState {
		return false, nil
	}
	/*
		below two fields could be empty based on scope.
		lets purely depend on account sub ID.
		Since "sub" id is ALWAYS there.
	*/
	claimsEmail, _ := payload.Claims["email"].(string)
	claimsAccessState, _ := payload.Claims["email_verified"].(bool)

	osTypeHeader := headers.Get("X-OS-TYPE")
	osTypeVersionHeader := headers.Get("X-OS-VERSION")

	err = gorm.G[auth.UserInfo](db).Create(ctx, &auth.UserInfo{
		AccessState: claimsAccessState,
		GoogleId:    claimsGoogleId,
		Email:       claimsEmail,
		OS:          osTypeHeader,
		OSVersion:   osTypeVersionHeader,
	})

	if err != nil {
		return false, err
	}

	return true, nil
}
