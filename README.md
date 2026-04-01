## Prerequisites
- **Java 17+**
- **PostgreSQL** (Running on port 5432)
- **Ollama** (For Local AI features)
- **MaxMind GeoLite2-City.mmdb** (Download the free database from [MaxMind](https://dev.maxmind.com/geoip/geolite2-free-geolocation-data))

---

## Local Setup

### 1. Environment Variables
Create a `.env` file in the **root directory** of the project

```env
# Database Configuration
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Security
JWT_SECRET=
JWT_EXPIRATION=86400000

# Mail Configuration
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

### 2. Geolocation Database
1. Download `GeoLite2-City.mmdb`.
2. Place the file in: `src/main/resources/GeoLite2-City.mmdb`.

### 3. Local AI (Ollama)
NextoBite uses a locally hosted Llama 3 model. You must have Ollama running:
```bash
# Install Ollama from ollama.com
ollama pull llama3
ollama run llama3
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

---

## API Endpoints

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/auth/register` | `POST` | Register with auto-location detection |
| `/api/auth/login` | `POST` | Get JWT token |
| `/api/recipes/local-feed` | `GET` | Get recipes for your detected city |
| `/api/ai/tweak/{id}` | `GET` | Get AI culinary adjustments (Vegan, Keto, etc.) |

---

## Project Structure
```text
src/main/java/com/backend/
├── config/         # Security, JWT, and Web Resource Handlers
├── controller/     # REST API Entry points
├── entity/         # JPA Models (User, Recipe)
├── repository/     # Data Access Layer
├── service/        # Business Logic (Geo, AI, Email, Recipes)
└── util/           # JWT & String Utilities
```
