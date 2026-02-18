
Project Instructions — Twixie_Angular

Purpose
- A short guide for managing and developing the project: installation, development workflow, and release steps.

Repository layout
- Main source code: `src/`.
- Angular/TypeScript config: `angular.json`, `tsconfig.json`.
- Components: `src/app/Components/`.

Local setup / Installation
1. Install dependencies:

```bash
npm install
```

2. Run the development server:

```bash
npm start
# or
ng serve --open
```

3. Run unit tests:

```bash
ng test
```

Build for production

```bash
ng build --prod
```

Branching strategy
- `main`: production-ready stable releases.
- `develop`: integration branch for daily development (optional).
- `feature/<feature-name>`: feature branches.
- `hotfix/<desc>`: critical fixes for immediate release.
- `release/<version>`: release preparation branches (optional).

Commit / PR guidelines
- Follow Conventional Commits: `feat:`, `fix:`, `docs:`, `chore:`, etc.
- Every PR should describe the issue/feature, how to test it, and link the Issue if available.
- Assign at least 1–2 reviewers before merging.

Issue tracking
- Create an Issue for each significant feature or bug.
- Use labels such as `bug`, `enhancement`, `help wanted`, `priority` to categorize.

Testing and code quality
- If CI is configured, it should run tests automatically before merging.
- Use linting and formatting tools (for example `eslint` and `prettier`).

Deployment
- Deployment steps depend on your hosting. Typically build with `ng build --prod` and upload the `dist/` folder to the server or CDN.
- Document deployment steps in the README or CI pipeline.

Roles and contacts
- Product Owner / Project Manager: [name or profile link]
- Tech Lead: [name]
- For technical questions open an Issue or contact via Slack/Teams/Email.

Best practices
- Keep commits small and focused.
- Add brief documentation for complex components in the files or README.
- Add screenshots or a short demo in the PR for significant UI changes.

Local troubleshooting
- Remove modules and reinstall if needed:

```bash
rm -rf node_modules package-lock.json
npm install
```

Notes
- Update this file when processes or infrastructure change.
- I can also add a PR/Issue template or a starter CI workflow (GitHub Actions) if you want.
