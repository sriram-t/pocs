# Shard Balancing: Moving Shops Confidently with Zero-Downtime at Terabyte-Scale

**Author:** [Paarth Madan](https://shopify.engineering/authors/paarth-madan)  
**Published on:** [Shopify Engineering Blog](https://shopify.engineering/mysql-database-shard-balancing-terabyte-scale)  
**Date:** September 24, 2021

---

## 🧭 Overview

Shopify powers millions of merchants, storing each shop’s data in dedicated **MySQL shards**.  
Over time, some shards accumulate significantly more load and data than others, causing performance bottlenecks and operational risks.

To maintain system balance and scalability, Shopify built an automated, **zero-downtime shard balancing system** that can move shops across terabyte-scale MySQL shards online — without affecting merchant experience.

---

## 🏗️ Architecture Context

- Shopify’s infrastructure runs in **pods**, each containing:
    - A MySQL shard
    - Redis and Memcached instances
    - Supporting application services
- All web requests pass through a **routing layer**, which uses a control-plane “routing table” mapping `shop_id` → shard.
- Each shop’s data is **completely isolated** by `shop_id`, making it a natural sharding key.
- Moving a shop means migrating *all rows across all tables* tied to a given `shop_id`.

---

## ⚖️ The Need for Shard Rebalancing

1. **Uneven Growth:** Some merchants grow faster than others, leading to hot shards.
2. **Resource Efficiency:** Under-utilized shards waste resources.
3. **Operational Safety:** Balancing avoids over-loaded database hosts and reduces failure blast radius.

Simple approaches like “equal number of shops per shard” fail because shops differ dramatically in data size and traffic patterns.

Shopify instead performs **data-driven shard load analysis** and simulations to plan migrations.

---

## 📊 Identifying Shops to Move

- Collect historical metrics: data size, query volume, and growth trend.
- Simulate shard utilization if a shop were moved.
- Build a **candidate move list** prioritizing the largest imbalance reduction with the least operational risk.

---

## 🚀 Migration Strategy

Shopify’s system performs live migrations in **three distinct phases**:

### **Phase 1: Batch Copy + Binlog Tailing**

- The open-source [**Ghostferry**](https://github.com/Shopify/ghostferry) library is used to copy shop data:
    1. Perform **batch copy** of existing rows for the shop from source to target shard.
    2. **Tail the MySQL binlog** to capture and replay ongoing writes from source to target in real time.
- Batch copy uses `SELECT ... FOR UPDATE` to lock rows and avoid race conditions.
- Throughout this phase, the shop remains fully **online** and served by the source shard.

---

### **Phase 2: Cutover (Zero-Downtime Switch)**

1. When replication lag < a few seconds, the migration acquires a **global write lock** on the shop.
2. Final binlog position is recorded and all pending writes are applied to the target shard.
3. The system updates the **control-plane routing table** to point to the new shard.
4. Writes are resumed on the target shard — total shop freeze time is typically **a few seconds**.

---

### **Phase 3: Verification & Cleanup**

- Post-migration, verifiers confirm **data completeness and integrity** between source and target.
- Once verification passes, stale data on the source shard is safely pruned.

---

## 🧪 Safety & Verification

- **Formal verification:** A TLA⁺ specification models Ghostferry’s correctness.
- **Continuous verification:** Ghostferry’s built-in verifiers ensure no row loss or duplication.
- **Rollback capability:** Any anomaly during migration triggers automatic rollback to the source shard.

---

## 📈 Results & Impact

| Metric | Before | After |
|---------|---------|--------|
| Hotspot load variance | High | Greatly reduced |
| Downtime during move | Minutes | Zero (few-second freeze only) |
| Migration safety | Manual verification | Automated, formally verified |

**Benefits:**
- Balanced shards → improved database reliability and query latency
- Continuous online operation → no merchant disruption
- Simplified horizontal scaling for future growth

---

## 💡 Key Takeaways

1. **Tenant isolation** using `shop_id` enables natural sharding.
2. **Two-phase migration (copy + tail)** ensures no downtime.
3. **Simulation-driven planning** identifies optimal shops to move.
4. **Formal verification and automation** turn risky migrations into safe, routine operations.

---

## 🧰 Tools & Technologies

- **MySQL** — primary data store
- **Ghostferry** — Shopify’s open-source online data mover
- **TLA⁺** — formal specification for migration correctness
- **Internal routing service** — updates shard mapping for each shop

---

## 🧑‍💻 References

- [Shopify Engineering Blog: “Shard Balancing at Terabyte Scale”](https://shopify.engineering/mysql-database-shard-balancing-terabyte-scale)
- [Ghostferry GitHub Repository](https://github.com/Shopify/ghostferry)
- [Author: Paarth Madan](https://shopify.engineering/authors/paarth-madan)

---

## 🏁 Summary

Shopify’s shard balancing system demonstrates how to operate **massive multi-tenant MySQL clusters at terabyte scale** while ensuring:
- Zero-downtime migrations
- Automated safety guarantees
- Sustainable scalability

By blending engineering discipline (formal verification, automation) with production pragmatism (binlog tailing, controlled cutovers), Shopify achieved a rare combination: **continuous rebalancing with zero merchant impact.**
