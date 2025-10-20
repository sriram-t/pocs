Source: https://www.reddit.com/r/RedditEng/comments/1avlywv/the_reddit_media_metadata_store/

# Reddit Media Metadata Store

## 📘 Overview
Reddit hosts billions of posts containing media content — images, videos, GIFs, and embedded third-party assets. As the platform evolves into a more media-oriented ecosystem, efficiently **managing, analyzing, and auditing** this vast library has become a critical challenge.

To address these needs, Reddit built the **Media Metadata Store** — a unified, high-performance database system that centralizes all media metadata and serves as the single source of truth for post-related media data.

---

## 🧩 Problem Statement
Historically, media metadata was fragmented across multiple systems:
- Different storage formats and query patterns per asset type
- Limited ability to audit, search, or analyze media metadata
- Some queries required accessing S3 directly or even downloading assets
- Inconsistent latency and poor debugging experience across data systems

The lack of a unified solution made it difficult to retrieve information such as:
- Media existence and upload date
- Size and encoding details
- Available resolutions, bitrates, and transcode artifacts
- Access permissions and takedown history

---

## 🧠 Objectives
The Media Metadata Store was designed to meet the following goals:

1. **Unify existing metadata** from multiple sources
2. **Serve reads at scale** — 100K+ requests per second with <50 ms latency
3. **Support metadata creation and updates** with moderate write throughput
4. **Enable anti-evil operations** (e.g., content takedowns)
5. **Simplify debugging and incident response** via SQL-accessible data

---

## ⚙️ System Architecture

### 🗄️ Database
- **Database Engine:** AWS Aurora PostgreSQL
- **Schema Design:** Denormalized JSONB-based structure
- **Partitioning:** Range-based partitioning on `post_id`
- **Partition Management:** [`pg_partman`](https://github.com/pgpartman)
- **Scheduling:** [`pg_cron`](https://github.com/citusdata/pg_cron)

### 🧩 Architecture Overview
A dedicated service layer provides APIs for reading and writing media metadata.  
All reads and writes go through this service, which communicates with Aurora Postgres.

