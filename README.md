[![CI](https://github.com/mikeruk/offer-service/actions/workflows/actions.yml/badge.svg)](https://github.com/mikeruk/offer-service/actions/workflows/actions.yml)

# Angebotsservice (offer-service)

Spring Boot-Mikroservice, der **Versicherungsangebote erstellt und verwaltet**.  
Er speichert Angebote in **PostgreSQL** und ruft einen externen **Rechnerservice** auf, um den Endpreis und die zugrunde liegenden Faktoren (km / Fahrzeugtyp / Region) zu berechnen.

Starten Sie diesen Dienst:
```text
./gradlew bootRun
```
Der Dienst ist verfügbar unter: http://localhost:8181

## Was dieser Dienst leistet

- Nimmt eine **Angebotsanfrage** entgegen (Kilometer, Fahrzeugtyp, Postleitzahl)
- Speichert die Anfrage in der Datenbank
- Ruft den **calculator-service** auf, um den Versicherungspreis + Faktoren zu berechnen
- Speichert das resultierende **Angebot** mit Status + Ablaufzeit
- Stellt REST-Endpunkte zum Erstellen und Abrufen von Angeboten bereit

---

## Konfiguration (application.properties)

- Name des Dienstes: `offer-service`
- Läuft unter: `http://localhost:8181`
- Basis-URL des Rechners: `http://localhost:8282`
- PostgreSQL-DB: `insurance_offers` auf `localhost:5432`

> Stellen Sie sicher, dass PostgreSQL ausgeführt wird und die DB vorhanden ist, bevor Sie den Dienst starten.

---



## Hauptkomponenten

### Starter
**`OfferServiceApplication`**
- Spring Boot-Einstiegspunkt (startet den Anwendungskontext und den eingebetteten Server).

### HTTP-Client für den Rechnerdienst
**`CalculatorClient`**
- Spring HTTP-Schnittstellen-Client (`@HttpExchange`) mit dem Ziel `/api/v1/price`.
- Verwendet `POST /calculate`, um `PriceResponseDto` für ein bestimmtes `PriceRequestDto` abzurufen.

**`AppConfig`**
- Erstellt einen `WebClient` mit `calculator-service.base-url`, definiert in der Datei application.properties.

### REST-API-Schicht
**`OfferController` / `OfferControllerImpl`**
- Stellt Endpunkte unter `/api/v1/offers` bereit
- `POST /create`: Erstellt ein neues Angebot (validiert die Eingabe über `@Valid`)
- `GET /{id}`: Ruft ein Angebot anhand der **öffentlichen ID** ab
- `GET /all`: Listet alle Angebote auf

### DTOs
**`NewOfferRequestDto`**
- Eingehende Anfrage-Nutzlast (validiert)
- `kilometers` (erforderlich, positiv, max. < 1.000.000.000)
- `vehicleType` (erforderlich)
- `postcode` (erforderlich, nicht leer)

**`NewOfferResponseDto`**
- Ausgehende Antwort-Nutzlast
- Angebots-IDs (Angebot + Angebotsanfrage)
- Ursprüngliche Eingaben (km/Typ/Postleitzahl)
- Berechnete Werte (Preis + Faktoren)
- Status + Ablaufdatum

**`PriceRequestDto` / `PriceResponseDto`**
- Nutzlasten, die für den Aufruf des Rechnerservices verwendet werden.

### Persistenz (JPA-Entitäten)
**`NewOfferRequestEntity`** (`offer_request`)
- Speichert die ursprünglichen Angebotsanfragedaten.
- Generiert beim Einfügen eine `publicId` unter Verwendung von `System.nanoTime()`.

**`OfferEntity`** (`Angebot`)
- Speichert das berechnete Angebotsergebnis + Faktoren und einen 1:1-Link zur Anfrage.
- Generiert beim Einfügen eine `publicId` unter Verwendung von `System.currentTimeMillis()`.
- Enthält `status` und `expiresAt`.

### Repositorys
**`OfferRepository`**
– Standard-JPA-Repository + `findByPublicId(Long publicId)`.

**`OfferRequestRepository`**
– Standard-JPA-Repository für Angebotsanfragen.

### Geschäftslogik
**`OfferService` / `OfferServiceImpl`**
– `createOffer(...)`
1. Speichert die eingehende Anfrage (`NewOfferRequestEntity`)
2. Ruft den Rechnerservice auf (`CalculatorClient.calculatePrice`)
3. Speichert das Angebot (`OfferEntity`) mit `CREATED` und `expiresAt = now + 24h`
4. Gibt ein `NewOfferResponseDto` zurück

- `getOffer(publicId)`
- Lädt nach öffentlicher ID oder löst `OfferNotFoundException` aus
- Markiert Angebote mit `CREATED` automatisch als `EXPIRED`, wenn `expiresAt` in der Vergangenheit liegt

- `getAllOffers()`
    - Lädt alle Angebote und wendet die automatische Ablaufzuordnung an, bevor DTOs zurückgegeben werden

### Fehlerbehandlung
**`GlobalExceptionHandler` (`@RestControllerAdvice`)**
- `400` für Validierungsfehler (`MethodArgumentNotValidException`) mit feldspezifischen Meldungen
- `400` für fehlerhaftes JSON (`HttpMessageNotReadableException`)
- `404` für fehlende Angebote (`OfferNotFoundException`)
- `500` Fallback für alle anderen Ausnahmen

**`ErrorResponse`**
- Standard-Fehlerantworttext: Zeitstempel, Status, Fehler, Meldung, Pfad (+ optionale validationErrors-Zuordnung)

---

## API-Endpunkte

### Angebot erstellen
`POST /api/v1/offers/create`

Beispiel für den Textkörper:
```json
{
  „kilometers“: 12000,
  „vehicleType“: „PKW“,
  „postcode“: „10115“
}
```

## DOKUMENTATION – So generieren Sie die Dokumente automatisch mit dem AsciiDoc-Tool:

1. Die Datei „build.gradle” muss dieses Plugin enthalten:
```gradle
plugins {
    ...
    id ‚org.asciidoctor.jvm.convert‘ version ‚4.0.5‘
}

...

asciidoctor {
    sources {
        include ‚index.adoc‘
    }
    baseDirFollowsSourceDir()
}
```

2. Die folgende Ordnerstruktur muss manuell innerhalb des Projekts erstellt werden:
   mkdir -p src/docs/asciidoc

3. Erstellen Sie die Indexdatei:
   nano src/docs/asciidoc/index.adoc

4. Die Datei src/docs/asciidoc/index.adoc enthält BEREITS die Dokumente + Markup.

5. Führen Sie dann den Befehl aus:
   ./gradlew asciidoctor
   Dadurch werden die Dokumente im Ordner build/docs/asciidoc/index.html generiert – öffnen und lesen Sie die Dokumente.
