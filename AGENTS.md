# AGENTS.md

This file provides guidance to AI coding agents (Claude Code, Gemini CLI, Codex, Cursor, etc.) when working with code in
this repository.

> **Note for agents**: This is the source of truth. `CLAUDE.md` is auto-generated from this file during Gradle sync — do
> not edit it directly.

## Agent Rules

- Agents cannot automatically/request trigger server run or app builds unless prompted by user.
- **Module dependencies**: When working on `:server` module changes, you have full access to the `:shared` module to
  find shared instances and utilities. Shared types and services may be imported and used directly.

### Commit and Documentation Policy

- **IMPORTANT**: Whenever you make a commit that affects or adds any new architectural changes, you MUST update the
  README.md file to reflect those changes.
- Keep README.md synchronized with architectural decisions, new features, and significant structural changes.
- README.md is user-facing documentation; AGENTS.md is for AI agent instructions only.
- **NEVER commit automatically**: Always stage files with `git add`, draft the commit message, then ask the user to
  review before executing `git commit`. Wait for explicit approval before committing.
- **Do NOT include Co-Authored-By line**: Do not add "Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>" or any
  similar attribution in commit messages.

### Structure

The Go server lives in the `server/` directory at the repo root. It uses stdlib `net/http` with no HTTP framework. Key layout:

```
server/
├── cmd/api/main.go          # Entry point
├── internal/
│   ├── config/config.go     # Env var config
│   ├── cache/valkey.go      # Redis/Valkey client
│   ├── auth/google.go       # Google ID token verification
│   ├── middleware/cors.go   # CORS middleware
│   └── handlers/            # HTTP handlers (health, nonce, login)
├── Dockerfile
├── docker-compose.yml
└── Makefile
```

Run locally: `make -C server run` or `cd server && go run ./cmd/api`