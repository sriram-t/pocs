# Rate Limiters & Load Shedding

This project demonstrates strategies for **rate limiting** and **load shedding** in API systems. These techniques are critical for protecting backend services from overload, ensuring fairness across clients, and maintaining high availability.

---

## 📌 Overview

APIs need protection against traffic spikes (both accidental and malicious). Without safeguards, a single misbehaving client can degrade performance for all users.

**Rate limiting** and **load shedding** provide this protection by controlling request flow and prioritizing resources.

---

## 🚦 Types of Limiters

### 1. Request Rate Limiter
- Limits each user to *N requests per second*.
- Allows short bursts above the cap (e.g., using **token bucket algorithm**).
- Prevents accidental floods.

### 2. Concurrent Requests Limiter
- Limits the number of *in-flight* requests per user.
- Protects against endpoints that are CPU/memory intensive.

### 3. Fleet Usage Load Shedder
- Reserves capacity for critical API methods.
- Non-critical requests above a threshold are rejected (`503 Service Unavailable`).

### 4. Worker Utilization Load Shedder
- Applies when backend workers are saturated.
- Sheds lower-priority traffic (e.g., test-mode requests) first.

---

## ⚙️ Implementation

- **Algorithm**: [Token Bucket](https://en.wikipedia.org/wiki/Token_bucket)
- **Data store**: Centralized (e.g., Redis) for distributed consistency
- **Middleware Integration**: Apply uniformly across all APIs
- **Failure Handling**: Fail-open (if limiter itself fails, don’t block everything)
- **Error Codes**:
    - `429 Too Many Requests` — client exceeded rate limit
    - `503 Service Unavailable` — load shedding due to overload

---

## 🚀 Rollout Strategy

1. Start with **Request Rate Limiter** (simplest, most effective baseline).
2. Add **Concurrent Requests Limiter** for resource-heavy endpoints.
3. Introduce **load shedding** to protect critical operations during incidents.
4. Use **dark launch** first — observe traffic that *would* be blocked before enforcing.
5. Provide **kill switches / feature flags** to quickly disable if needed.

---

## 📊 Best Practices

- **Monitor** limiter metrics (hit ratio, dropped requests, latency).
- **Segment** traffic by priority (critical vs non-critical).
- **Educate clients** with clear error messages and retry guidance.
- **Combine with retries + jitter** on client side to avoid synchronized retry storms.

---

## 📚 References
- [Stripe Blog: Scaling your API with rate limiters](https://stripe.com/blog/rate-limiters)
- [Token Bucket Algorithm](https://en.wikipedia.org/wiki/Token_bucket)