package org.db.identifiers;

import java.sql.*;
import java.util.Properties;
import java.util.UUID;

public class UUIDIdentifier {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://XXXXX:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "XXXXX");
        props.setProperty("password", "XXXXX");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            long start = System.currentTimeMillis();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO public.users_uuid (user_id, username, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 1; i <= 10_000_000; i++) {
                    UUID userId = UUID.randomUUID();
                    String username = "user_" + i;
                    String email = "user_" + i + "@example.com";

                    ps.setObject(1, userId);
                    ps.setString(2, username);
                    ps.setString(3, email);

                    ps.addBatch();

                    if (i % 10_000 == 0) { // commit every 10k rows
                        ps.executeBatch();
                        conn.commit();
                        System.out.println("Inserted " + i + " rows...");
                    }
                }
                ps.executeBatch();
                conn.commit();
                System.out.println("Finished inserting 10M rows");
            }
            long end = System.currentTimeMillis();
            System.out.println("Batch insert time: " + (end - start) / 1000.0 + " sec");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//✅ Finished inserting 1M rows
//Batch insert time: 244.374 sec
//9c92dd28-25a5-4e13-8ebb-5b50762cbcb1
//8b33743d-e7ca-4f4a-ace9-c067c63118bf
//ac1dc60d-0c7d-460a-8132-83ecd7e89876
//20590909-e824-46ab-aba5-75999a53d722
//760c021a-b5e1-4921-a8c3-918667fb8ead


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
// users_uuid_v7_pkey	37 MB