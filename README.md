# justAGame
a simple game

pring Boot backend for the Power Duel game: real Google login, a persisted
`Player` record (power level, training level), and the game screen served
straight from the app so login works with zero CORS setup.

## What's in here

- `POST/GET /oauth2/authorization/google` — starts Google sign-in (Spring Security's built-in endpoint)
- `GET /api/auth/me` — returns `{ authenticated, name, powerLevel, trainingLevel }` for the current session
- `GET /logout` — signs the player out
- `src/main/resources/static/index.html` — the Power Duel game screen, wired to the endpoints above (this is your existing `power-duel.html`, with the mock login swapped for real calls)
- `Player` entity — one row per Google account, created/updated on every login
- H2 file-based datastore (`./data/power-duel`) — swap for Postgres later by changing `application.yml` and adding the Postgres driver dependency

## 1. Get Google OAuth credentials

1. Go to the [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
2. Create an OAuth 2.0 Client ID → Application type: **Web application**
3. Add an authorized redirect URI:
   `http://localhost:8080/login/oauth2/code/google`
4. Copy the generated **Client ID** and **Client Secret**

## 2. Set credentials as environment variables

Following the pattern from your other projects (env vars / `.env`, not committed):

```bash
export GOOGLE_CLIENT_ID=your-client-id
export GOOGLE_CLIENT_SECRET=your-client-secret
```

(or use `spring-dotenv` / a Spring Profile / Vault, same as your usual setup — `application.yml` just reads `${GOOGLE_CLIENT_ID}` / `${GOOGLE_CLIENT_SECRET}`, nothing is hardcoded)

## 3. Run it

```bash
mvn spring-boot:run
```

Then open **http://localhost:8080** — that's the game screen itself, served
by Spring Boot. "Sign in with Google" now does a real OAuth2 login; after
consenting, you're redirected back signed in, with your name and stored
power level shown top-right.

## Extending toward training / adventure

The `Player` entity already has `powerLevel` and `trainingLevel` fields to
build on. The natural next endpoints:

- `POST /api/training/session` — run a training activity, increment `trainingLevel`, bump `powerLevel`
- `POST /api/duel/attack` — move the fight's turn resolution server-side (stops the frontend from being able to fake a win)
- `POST /api/adventure/start` / `POST /api/adventure/choice` — narrative/adventure activities, same `Player` persistence pattern

All of these would follow the same shape as `AuthController`: read the
logged-in `Player` via `@AuthenticationPrincipal`, look it up by
`googleSub`, update it, save it.

## Note on this scaffold

- CSRF is left enabled everywhere except `/logout` and `/h2-console`, to keep
  the sign-out link a plain `<a href="/logout">` for this first pass — worth
  tightening (a proper POST + token) once this moves past local dev.
- H2 is file-based so player data survives restarts locally; swap to Postgres
  before deploying anywhere shared.
