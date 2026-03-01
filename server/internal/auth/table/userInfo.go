package auth

import "gorm.io/gorm"

type UserInfo struct {
	gorm.Model
	GoogleId    string `gorm:"uniqueIndex"`
	Email       string
	Profile     string
	AccessState bool
}
