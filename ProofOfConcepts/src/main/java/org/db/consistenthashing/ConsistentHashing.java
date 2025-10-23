package org.db.consistenthashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class ConsistentHashing {
    static String[] nodes;
    static BigInteger[] keys;

    public static void main(String args []){
        int numberOfNodes = 10;
        String key = "ksnajksnasasasalksmlkamslkaslkajsklamks kljnakjsnjkans kjnasjknaskj kjansjkansnkjanskjanskjasn";
        BigInteger maxHash = BigInteger.valueOf(2).pow(256);
        BigInteger rangeSize = maxHash.divide(BigInteger.valueOf(numberOfNodes));
        createHashSpace(numberOfNodes, rangeSize);

        System.out.println(getNode(key, numberOfNodes));
    }

    // Adding a node is just finding out which is hot node.
    // Get Keys Space for that node.
    // Divide keys space into two.
    // Add node and store in sorted order.
    // The same for addNode and removeNodes


    public static String getNode(String key, int numberOfNodes) {
        BigInteger keyPosition = hashStringSHA256(key);
        System.out.println("keyPosition "+ keyPosition);

        int i;
        for (i = 0; i < numberOfNodes; i++) {
            if (keys[i].compareTo(keyPosition) > 0) {
                break;
            }
        }

        return nodes[i];
    }


    public  static void createHashSpace(int numberOfNodes, BigInteger rangeSize) {
        nodes = new String[numberOfNodes];
        keys = new BigInteger[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            nodes[i] = "Node-" + i;
        }
        System.out.print("rangeSize" + rangeSize);

        for (int i = 0; i < numberOfNodes; i++) {
            keys[i] = rangeSize.multiply(new BigInteger(String.valueOf(i))).add(rangeSize);
            System.out.print("keys["+ i+ "]" +  keys[i]);
        }

    }

    public static BigInteger hashStringSHA256(String input) {
        try {
            // Get an instance of the SHA-256 message digest algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to bytes using UTF-8 encoding
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            System.out.println(hashBytes);
            BigInteger position = new BigInteger(1, hashBytes);
            System.out.println(position);
            return position;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found: " + e.getMessage());
            return null;
        }
    }
}
