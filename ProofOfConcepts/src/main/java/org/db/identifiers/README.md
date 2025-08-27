# 🚀 Primary Key Benchmarking: BIGINT vs UUIDv7 vs UUIDv4

This project benchmarks different primary key strategies in **PostgreSQL** to help guide database design decisions.  
We compare:

- **BIGINT (auto-increment IDs)**
- **UUIDv7 (time-ordered UUIDs)**
- **UUIDv4 (random UUIDs)**

across **insert performance, query efficiency, storage overhead, and global uniqueness**.

---

## 📌 Why This Matters
Primary keys are fundamental to how databases store and retrieve data.  
The choice impacts:

- ⚡ **Insert speed** and index maintenance
- 🔍 **Query performance** (especially pagination with `WHERE id > ? ORDER BY id ASC`)
- 💾 **Storage size** and **index fragmentation**
- 🌍 **Global uniqueness** for distributed systems

---

## ⚖️ Comparison

### 🔹 Insert Performance
- **BIGINT** → 🚀 Best (sequential inserts, minimal page splits)
- **UUIDv7** → ✅ Good (~30–40% faster than UUIDv4 due to time ordering)
- **UUIDv4** → ❌ Poor (random inserts → fragmentation → slower)

👉 Ranking: **BIGINT > UUIDv7 > UUIDv4**

---

### 🔹 Global Uniqueness
- **BIGINT** → Unique only within a single DB instance
- **UUIDv7** / **UUIDv4** → Globally unique (distributed safe)

---

### 🔹 Read Performance & Pagination
- **BIGINT** → ⚡ Excellent (range scans, ordered pagination)
- **UUIDv7** → ✅ Good (time component improves scans)
- **UUIDv4** → ❌ Poor (random order, fragmented indexes)

---

### 🔹 Storage & Index Size
| Type     | Size per Key | Notes                          |
|----------|--------------|--------------------------------|
| BIGINT   | 8 bytes      | Smallest, most efficient       |
| UUIDv7   | 16 bytes     | Larger, but time-ordered       |
| UUIDv4   | 16 bytes     | Largest + random fragmentation |

---

## 📊 Benchmark Results (10 Million Rows)

- **Insert Time**
    - UUIDv4 → ⏱️ 55 min
    - UUIDv7 → ⏱️ 25 min

- **Primary Index Size**
    - UUIDv4 → 1722 MB
    - UUIDv7 → 1639 MB

- **Query Performance (`LIMIT 1000` with `WHERE id > ?`)**
    - UUIDv7 → Planning: 0.162 ms, Execution: 7.479 ms
    - UUIDv4 → Planning: 0.523 ms, Execution: 9.351 ms

---

## ✅ Conclusions
- Use **BIGINT** for **internal, small/medium systems** where IDs stay local.
- Use **UUIDv7** for **large-scale distributed systems** (events, logs, analytics).
- Use **UUIDv4** **only** if full randomness is a hard requirement (expect performance tradeoffs).

📌 Benchmarks show that UUIDv7 is ~40% faster than UUIDv4 for inserts, with smaller index sizes due to reduced fragmentation. Additionally, for a database with 10M rows, UUIDv7 delivers ~20% faster performance on limit-based queries. As the dataset grows, these advantages compound, providing even greater benefits. 
