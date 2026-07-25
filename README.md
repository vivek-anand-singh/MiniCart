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