# Invoice Creator – Spring Boot (Hexagonal Architecture)

Spring Boot backend that exposes a **health probe** and an **invoice creation API** returning a PDF, built with **Hexagonal Architecture**.

## Run

```bash
mvn spring-boot:run
```

Server: `http://localhost:8080`

## APIs

### 1. Health probe (Spring Boot standard)

- **GET** `/actuator/health`
- Returns standard Spring Boot Actuator health (e.g. `{"status":"UP"}`).
- Use for Kubernetes/orchestrator liveness/readiness.

### 2. Invoice creation (v1)

- **POST** `/api/v1/invoices`
- **Content-Type:** `application/json`
- **Accept / Response:** `application/pdf` (file download)

**Language (PDF labels):** Supported languages are **English** (default) and **Spanish**. Use either:
- **Query parameter:** `?lang=es` for Spanish, `?lang=en` for English.
- **Header:** `Accept-Language: es` (or `en`). If both are present, the query parameter wins.

Request body: JSON with the following structure (all monetary values in Euro).

#### Top-level

| Field | Type | Description |
|-------|------|-------------|
| `invoiceDate` | date (ISO) | Invoice date |
| `invoiceNumber` | string | Invoice number |
| `deliveryDate` | date (ISO) | Delivery date |
| `dueDate` | date (ISO) | Due date |
| `invoiceItems` | array | Line items (see below) |
| `totalNetPrice` | number | Total net |
| `vatRate` | number | VAT rate (e.g. 19) |
| `vatAbsolute` | number | VAT amount |
| `totalGrossPrice` | number | Total gross |
| `finalNotes` | string | Optional notes |
| `companyDetails` | object | Issuer (see below) |
| `customer` | object | Bill-to (see below) |

#### `companyDetails`

- `name` – Company name (incl. legal form)
- `address` – See **Address**
- `phone`, `email`, `placeOfJurisdiction`, `companyId`, `ceoOrDirector`, `vatId`
- `bankAccount` – See **Bank account**

#### `companyDetails.bankAccount` (Bank account)

- `bankName`, `accountOwner`, `iban`, `bic`

#### `customer`

- `companyName` – Customer company (incl. legal form)
- `customerNumber` – “Your customer no.”
- `vatId` – “Your VAT-ID” (optional)
- `contact` – “Your contact”
- `address` – See **Address**

#### `address` (company or customer)

- `streetAndNumber`, `postalCode`, `city`, `countryCode`

#### `invoiceItems[]` (each item)

- `description`, `quantity`, `unit` (must be `"h"` for hours), `unitPriceEuro`, `totalPrice`

#### Example (minimal)

```json
{
  "invoiceDate": "2025-02-14",
  "invoiceNumber": "INV-2025-001",
  "deliveryDate": "2025-02-14",
  "dueDate": "2025-03-14",
  "invoiceItems": [
    {
      "description": "Consulting",
      "quantity": 10,
      "unit": "hours",
      "unitPriceEuro": 120.00,
      "totalPrice": 1200.00
    }
  ],
  "totalNetPrice": 1200.00,
  "vatRate": 19,
  "vatAbsolute": 228.00,
  "totalGrossPrice": 1428.00,
  "finalNotes": "Thank you for your business.",
  "companyDetails": {
    "name": "My Company GmbH",
    "address": { "streetAndNumber": "Main St 1", "postalCode": "10115", "city": "Berlin", "countryCode": "DE" },
    "phone": "+49 30 123456",
    "email": "billing@example.com",
    "placeOfJurisdiction": "Berlin",
    "companyId": "HRB 12345",
    "ceoOrDirector": "Jane Doe",
    "bankAccount": { "bankName": "Bank", "accountOwner": "My Company GmbH", "iban": "DE89...", "bic": "COBADEFFXXX" },
    "vatId": "DE123456789"
  },
  "customer": {
    "companyName": "Client AG",
    "customerNumber": "C-001",
    "vatId": "DE987654321",
    "contact": "John Smith",
    "address": { "streetAndNumber": "Client Str 2", "postalCode": "80331", "city": "Munich", "countryCode": "DE" }
  }
}
```

Response: PDF file with the same data in a standard invoice layout.

## Configuration

Optional overrides in `application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `invoice.pdf.date-format` | `dd.MM.yyyy` | Date format in PDF |
| `invoice.pdf.margin-mm` | `40` | Page margin (mm) |
| `invoice.pdf.title-font-size` | `18` | Company title font size |
| `invoice.pdf.heading-font-size` | `10` | Section heading font size |
| `invoice.pdf.normal-font-size` | `10` | Body text font size |

## Tests

```bash
mvn test
```

- **InvoiceRequestMapperTest** – DTO → domain mapping
- **CreateInvoiceServiceTest** – service delegates to PDF port
- **OpenPdfInvoiceGeneratorTest** – PDF output is non-empty and valid
- **InvoiceControllerTest** – controller returns PDF with correct headers
- **InvoiceItemTest** – unit validation (only `"h"` allowed)

## Architecture (Hexagonal)

- **Domain** (`domain/`): `Invoice`, `InvoiceItem`, `CompanyDetails`, `Customer`, `Address`, `BankAccount` (value objects / aggregate).
- **Application** (`application/`): `CreateInvoiceService`; port `PdfGenerationPort` for PDF output.
- **Adapters**
  - **Inbound**: REST controller + DTOs + `InvoiceRequestMapper` (HTTP → domain).
  - **Outbound**: `OpenPdfInvoiceGenerator` implements `PdfGenerationPort` (domain → PDF via OpenPDF).

Dependencies point inward: adapters depend on application/domain; domain has no framework or adapter dependencies.
