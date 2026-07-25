# MiniCart

A small grocery cart API built with Spring Boot 3 and Java 17. Supports adding products to a cart, updating quantities, and getting a computed bill with delivery fees.

## How to run

You'll need MySQL running locally. Create the database and user:

```sql
CREATE DATABASE minicart;
CREATE USER 'minicartuser'@'localhost' IDENTIFIED BY '123';
GRANT ALL PRIVILEGES ON minicart.* TO 'minicartuser'@'localhost';
```

Then start the app:

```bash
./mvnw spring-boot:run
```

On first startup, 10 grocery products are seeded automatically via `data.sql`. The app runs on port 8080.

To run tests (no MySQL needed — uses H2 in-memory):

```bash
./mvnw test
```

## Endpoints

**List all products**
```bash
curl http://localhost:8080/products
```

**Add item to cart**
```bash
curl -X POST http://localhost:8080/cart/items \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

Adding the same product again increments the quantity. Unknown product ID returns 404, quantity ≤ 0 returns 400.

**Update quantity** (set to 0 to remove)
```bash
curl -X PATCH http://localhost:8080/cart/items/1 \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

**View cart with bill**
```bash
curl http://localhost:8080/cart
```

Delivery is ₹30 (3000 paise), free when the item total crosses ₹500 (50000 paise).

## A few decisions I made

- **Money as paise (integers) everywhere** — floats are unreliable for currency. Storing ₹25 as `2500` keeps all arithmetic exact.
- **Single global cart, no auth** — the assignment didn't need it, so I didn't build it. Adding user-scoped carts later would just mean adding a `userId` foreign key to `CartItem`.
- **Bill computed at request time in the service layer** — never stored. This means the total is always accurate even if product prices change.
- **H2 for tests, MySQL for production** — tests run with `@ActiveProfiles("test")` and an in-memory H2 database so you don't need MySQL running in CI.
- **`INSERT IGNORE` in data.sql** — makes the seed idempotent. Restarting the app won't duplicate the 10 products.
