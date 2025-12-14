[![CI](https://github.com/mikeruk/offer-service/actions/workflows/actions.yml/badge.svg)](https://github.com/mikeruk/offer-service/actions/workflows/actions.yml)

# Offer Service (offer-service)

Spring Boot microservice that **creates and manages insurance offers**.  
It persists offers in **PostgreSQL** and calls an external **calculator-service** to calculate the final price and the underlying factors (km / vehicle type / region).

Start this service:
```text
./gradlew bootRun
```
Service will be available at: http://localhost:8181

## What this service does

- Accepts an **offer request** (kilometers, vehicle type, postcode)
- Stores the request in the database
- Calls **calculator-service** to compute the insurance price + factors
- Stores the resulting **offer** with status + expiration time
- Exposes REST endpoints to create and fetch offers

---

## Configuration (application.properties)

- Service name: `offer-service`
- Runs on: `http://localhost:8181`
- Calculator base URL: `http://localhost:8282`
- PostgreSQL DB: `insurance_offers` on `localhost:5432`

> Make sure PostgreSQL is running and the DB exists before starting the service.

---

## Main Components

### Starter
**`OfferServiceApplication`**
- Spring Boot entry point (starts the application context and embedded server).

### HTTP Client to calculator-service
**`CalculatorClient`**
- Spring HTTP Interface client (`@HttpExchange`) targeting `/api/v1/price`.
- Uses `POST /calculate` to fetch `PriceResponseDto` for a given `PriceRequestDto`.

**`AppConfig`**
- Builds a `WebClient` with `calculator-service.base-url` defined in application.properties file.

### REST API Layer
**`OfferController` / `OfferControllerImpl`**
- Exposes endpoints under `/api/v1/offers`
    - `POST /create`: create a new offer (validates input via `@Valid`)
    - `GET /{id}`: get one offer by **public id**
    - `GET /all`: list all offers

### DTOs
**`NewOfferRequestDto`**
- Incoming request payload (validated)
    - `kilometers` (required, positive, max < 1,000,000,000)
    - `vehicleType` (required)
    - `postcode` (required, not blank)

**`NewOfferResponseDto`**
- Outgoing response payload
    - offer ids (offer + offerRequest)
    - original inputs (km/type/postcode)
    - computed values (price + factors)
    - status + expirationDate

**`PriceRequestDto` / `PriceResponseDto`**
- Payloads used for calling calculator-service.

### Persistence (JPA Entities)
**`NewOfferRequestEntity`** (`offer_request`)
- Stores the original offer request data.
- Generates a `publicId` at insert time using `System.nanoTime()`.

**`OfferEntity`** (`offer`)
- Stores the calculated offer result + factors and a 1:1 link to the request.
- Generates a `publicId` at insert time using `System.currentTimeMillis()`.
- Contains `status` and `expiresAt`.

### Repositories
**`OfferRepository`**
- Standard JPA repository + `findByPublicId(Long publicId)`.

**`OfferRequestRepository`**
- Standard JPA repository for offer requests.

### Business Logic
**`OfferService` / `OfferServiceImpl`**
- `createOffer(...)`
    1. Saves the incoming request (`NewOfferRequestEntity`)
    2. Calls calculator-service (`CalculatorClient.calculatePrice`)
    3. Saves the offer (`OfferEntity`) with `CREATED` and `expiresAt = now + 24h`
    4. Returns a `NewOfferResponseDto`

- `getOffer(publicId)`
    - Loads by public id or throws `OfferNotFoundException`
    - Automatically marks `CREATED` offers as `EXPIRED` if `expiresAt` is in the past

- `getAllOffers()`
    - Loads all offers and applies auto-expiration mapping before returning DTOs

### Error Handling
**`GlobalExceptionHandler` (`@RestControllerAdvice`)**
- `400` for validation errors (`MethodArgumentNotValidException`) with per-field messages
- `400` for malformed JSON (`HttpMessageNotReadableException`)
- `404` for missing offers (`OfferNotFoundException`)
- `500` fallback for all other exceptions

**`ErrorResponse`**
- Standard error response body: timestamp, status, error, message, path (+ optional validationErrors map)

---

## API Endpoints

### Create Offer
`POST /api/v1/offers/create`

Example body:
```json
{
  "kilometers": 12000,
  "vehicleType": "PKW",
  "postcode": "10115"
}
```

## DOCUMENTATION - how to auto-generate the docs via AsciiDoc tool:

1. The build.gradle file must have this plugin:
```gradle
plugins {
    ...
    id 'org.asciidoctor.jvm.convert' version '4.0.5'
}

...

asciidoctor {
    sources {
        include 'index.adoc'
    }
    baseDirFollowsSourceDir()
}
```

2. Have to manually create the following folder structure inside the project:
   mkdir -p src/docs/asciidoc

3. and create the index file:
   nano src/docs/asciidoc/index.adoc

4. The file src/docs/asciidoc/index.adoc ALREADY contains the docs + markup.

5. Then run the command:
   ./gradlew asciidoctor
   , this will generate the docs in the folder: build/docs/asciidoc/index.html - open and read the docs


