package org.db.identifiers;

import java.sql.*;
import java.util.Properties;
import com.github.f4b6a3.uuid.UuidCreator;

public class UUIDV7Indentifier {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://XXXXX:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "XXXXX");
        props.setProperty("password", "XXXXX");
        long start = System.currentTimeMillis();

        try (Connection conn = DriverManager.getConnection(url, props)) {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO public.users_uuid_v7 (user_id, username, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 1; i <= 10_000_000; i++) {
                    // Generate UUIDv7
                    java.util.UUID userId = UuidCreator.getTimeOrderedEpoch();

                    String username = "user_" + i;
                    String email = "user_" + i + "@example.com";

                    ps.setObject(1, userId);
                    ps.setString(2, username);
                    ps.setString(3, email);

                    ps.addBatch();

                    if (i % 10_000 == 0) { // commit in chunks
                        ps.executeBatch();
                        conn.commit();
                        System.out.println("Inserted " + i + " rows...");
                    }
                }
                ps.executeBatch();
                conn.commit();
                System.out.println("Finished inserting 10M rows into users_uuid_v7");
            }
            long end = System.currentTimeMillis();
            System.out.println("Batch insert time: " + (end - start) / 1000.0 + " sec");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//✅ Finished inserting 1M rows into users_uuid_v7
//Batch insert time: 236.79 sec
//0198ec33-7b73-71c7-9f09-da2771224b7a
//0198ec33-7b73-71c7-9f0a-6ba9b22f0711
//0198ec33-7b74-7485-93bc-e62a68acf034
//0198ec33-7b74-7485-93bd-487a9ace17f0
//0198ec33-7b74-7485-93be-2a70fea9b8ba


//SELECT
//i.relname AS pk_index,
//pg_size_pretty(pg_relation_size(i.oid)) AS index_size
//FROM
//pg_class t
//JOIN pg_index ix ON t.oid = ix.indrelid
//JOIN pg_class i ON i.oid = ix.indexrelid
//        WHERE
//t.relname = 'users_uuid_v7'
//AND ix.indisprimary;
// users_uuid_v7_pkey	30 MB


