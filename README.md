# Twixie

Twixie is a full-stack application with an Angular frontend and a Spring Boot backend. The frontend was generated with Angular CLI and uses Angular Material and PrimeNG for the UI. The backend provides a REST API with JWT authentication, AI-powered features (Gemini), and email support.

## Project Structure

- `Twixie-client/` – Angular frontend app 
  - `src/` – Application source code
  - `package.json` – Dependencies and npm scripts
  - `README.md` – Angular-specific CLI usage and commands
- `Twixie-server/` – Spring Boot backend
  - `src/main/java/` – Java source code
  - `pom.xml` – Maven dependencies and build config
  - H2 database (`TwixieDB.mv.db`) for development

## Tech Stack

### Frontend (Twixie-client)
- **Framework**: Angular 20
- **UI Libraries**: Angular Material, PrimeNG
- **Language**: TypeScript

### Backend (Twixie-server)
- **Framework**: Spring Boot 3.5
- **Java**: 17
- **Database**: H2 (development)
- **Security**: Spring Security, JWT
- **AI**: Spring AI (Gemini)
- **Other**: MapStruct, Spring Mail

## Getting Started

### Prerequisites

- **Node.js** and **npm** (for frontend)
- **Java 17** and **Maven** (for backend)
- **Environment variables** (for server):
  - `GEMINI_KEY` – API key for Gemini
  - `GMAIL_PASSWORD` – Gmail app password for email

### Backend (Twixie-server)

```bash
cd Twixie-server
mvn spring-boot:run
```

The API runs at `http://localhost:8080` by default. H2 console is enabled for development.

### Frontend (Twixie-client)

```bash
cd Twixie-client
npm install
npm start
# or: ng serve -o   (opens browser automatically)
```

Then open your browser at `http://localhost:4200/`.

## Scripts

### Twixie-client
From `Twixie-client/`:
```bash
npm start          # Start dev server (ng serve)
npm run build      # Build for production
npm run watch      # Watch builds in development mode
npm test           # Run unit tests
```

### Twixie-server
From `Twixie-server/`:
```bash
mvn spring-boot:run    # Run the server
mvn test               # Run tests
```

## Development Notes

- For detailed Angular CLI usage, see `Twixie-client/README.md`.
- The server uses H2 with `ddl-auto=update`; the database file is created in `Twixie-server/`.
- Set `GEMINI_KEY` and `GMAIL_PASSWORD` before running the server for AI and email features.
