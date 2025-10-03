# PostgreSQL Read-After-Write Consistency Using WAL LSN

This guide explains how to use PostgreSQL's WAL (Write-Ahead Log) Log Sequence Numbers (LSNs) to ensure read-after-write consistency when working with replicas.

By leveraging LSNs, applications can determine which read replica has caught up with the latest transaction and safely route reads without waiting for replication lag.

---

## Overview

- **Primary**: Executes write transactions and provides the current WAL insert position using `pg_current_wal_insert_lsn()`.
- **Replica(s)**: Continuously replay WAL. Their progress can be checked with `pg_last_wal_replay_lsn()`.
- **Goal**: Route reads only to replicas that have replayed the transaction’s commit LSN.

---

## Example Workflow

### 1. Perform Transaction on Primary

```sql
BEGIN;
INSERT INTO your_table (column1, column2) VALUES ('value1', 'value2');
SELECT pg_current_wal_insert_lsn(); -- Capture commit LSN
COMMIT;
