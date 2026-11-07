# toogoodtogo-clone

This is a university project for the Backend Engineering course at FH Technikum Wien. It is a small REST API inspired by Too Good To Go. Stores can offer surprise bags with leftover food, and users can reserve these bags for a lower price and pick them up later.

The project uses Spring Boot with Java 21, Spring Data JPA and a MariaDB database.

## Data model

There are four entities: User, Store, Offer and Reservation.

| Entity | Main fields |
|---|---|
| User | firstName, lastName, email (unique) |
| Store | name, description, street, postalCode, city |
| Offer | title, description, price, originalValue, quantityAvailable, pickupStart, pickupEnd |
| Reservation | quantity, createdAt, status |

All entities use a UUID as id.

### How the entities are connected

A store can have many offers, but every offer belongs to exactly one store. So Offer has a `@ManyToOne` to Store, and Store has a `@OneToMany` list of its offers.

A user can have many reservations, and an offer can also have many reservations. A reservation always belongs to one user and one offer. This means Reservation is the link between User and Offer and holds two foreign keys (`user_id` and `offer_id`).

```
Store 1 ---- n Offer 1 ---- n Reservation n ---- 1 User
```

Deleting works from the top down. If a store is deleted, its offers are deleted too, and with them all reservations for these offers. If a user is deleted, all reservations of that user are deleted as well.

A reservation has a status, which is one of `RESERVED`, `PICKED_UP` or `CANCELLED`. New reservations always start with `RESERVED`.

##  Rules

* A user can only reserve as many bags as the offer still has. When a reservation is created, the quantity of the offer goes down.
* If a reservation is cancelled or deleted while it is still `RESERVED`, the bags go back to the offer.
* Once a reservation is picked up or cancelled, its status can not be changed anymore.
* The email of a user has to be unique.
* For an offer, pickupEnd has to be after pickupStart, and the price can not be higher than the original value.

Input is checked with Bean Validation in the DTOs. Unknown ids return 404, broken rules return 400 or 409.

## Endpoints

Every resource has the usual CRUD endpoints (GET all, GET by id, POST, PUT, DELETE).

| Path | Extra |
|---|---|
| `/users` | `GET /users/{id}/reservations` returns all reservations of a user |
| `/stores` | `GET /stores?city=Wien` filters by city, `GET /stores/{id}/offers` returns all offers of a store |
| `/offers` | |
| `/reservations` | `PUT` only changes the status |

The full API can be seen in Swagger UI at `http://localhost:8080/swagger-ui/index.html` while the app is running.

## Project structure

The code follows the layered structure from the course:

* `controller` handles the HTTP requests
* `service` contains the business logic
* `repository` talks to the database
* `entity` contains the JPA entities
* `dto` contains the records that are sent and received by the API

## How to run

First start the database with Docker:

```bash
cd src
docker compose up -d
```

Then start the app from the project root:

```bash
./mvnw spring-boot:run
```
