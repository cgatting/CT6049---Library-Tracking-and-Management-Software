# CT6049 Library Database System — Submission Package (turn_in)

This folder is a self-contained package for review and demonstration. It includes runnable executables, the shaded JAR, database scripts, and third-party dependencies.

## Contents

```
turn_in/
├── executables/          # Windows executables, launcher scripts, bundled JAR
├── dependencies/         # Required third‑party libraries (JARs)
├── database/             # Oracle & MongoDB setup scripts
├── documentation/        # Assessment docs and setup notes
└── SUBMISSION_README.md  # This file
```

## Prerequisites

- `Java 8+` (JRE or JDK) available on `PATH` (`java -version` works)
- For Oracle backend: Oracle XE running on `localhost:1521/xe` with the provided user
- For MongoDB backend: A reachable MongoDB instance per your environment

## How To Run

- Oracle backend (GUI):
  - Double‑click `executables\library_oracle.exe`
  - Or run: `executables\java_oracle.bat`

- MongoDB backend (GUI):
  - Double‑click `executables\library_mongo.exe`
  - Or run: `executables\java_mongo.bat`

- Run via Java (cross‑platform):
  - `java -cp "executables\library-database-system-1.0.0.jar;dependencies\*" com.library.LibraryApp --backend=oracle`
  - `java -cp "executables\library-database-system-1.0.0.jar;dependencies\*" com.library.LibraryApp --backend=mongo`

Note: `executables\run-app.bat` is a self-contained launcher that runs the bundled JAR using `dependencies\*` — no Maven required.

## Database Setup & Verification

- Oracle XE user and schema:
  - Create user: `database\create_oracle_user.sql`
  - Schema scripts: `database\oracle\` (run after user creation)
  - Quick connectivity test: run `executables\test-oracle-connection.bat`
    - Expected `[ok] Connected successfully` if XE and credentials are correct

- MongoDB:
  - Optional seed data: `database\mongodb\` (if provided)
  - Ensure the MongoDB service is running and accessible

## Functional Verification

- Launch with either backend and confirm the GUI loads.
- Perform basic operations:
  - Add a sample student and a sample book
  - Borrow then return a book; observe validation on duplicate returns
  - Generate monthly loan and fine reports from the GUI

## Troubleshooting

- If a batch script reports Java not found, install JRE/JDK and reopen the terminal.
- Oracle: verify services running (`OracleServiceXE`, listener), credentials match setup scripts.
- MongoDB: confirm the service is running and reachable; check firewall/VPN.
- Run from the `turn_in` folder (scripts expect sibling paths like `executables` and `dependencies`).

## Building From Source

This submission is intended for running, not building. To rebuild the full project:
- Open the full source repository (not `turn_in`) and run `mvn clean package`.
- The submission’s `executables\compile-project.bat` explains this and guides you to the runner scripts.

---

Thank you for reviewing this CT6049 Library Database System submission.