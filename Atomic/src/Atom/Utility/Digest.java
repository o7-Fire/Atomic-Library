package Atom.Utility;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest {


    public static byte[] sha256(String text) {
        return sha256(text.getBytes());
    }

    public static byte[] sha256(File file) throws IOException {
        return sha256(Files.readAllBytes(file.toPath()));
    }

    public static byte[] sha256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(bytes);
        }catch (NoSuchAlgorithmException e) {
            //how there is no this algorithm
            throw new RuntimeException(e);
        }
    }

    public static byte[] sha1(String text) {
        return sha1(text.getBytes());
    }

    public static byte[] sha1(File file) throws IOException {
        return sha1(Files.readAllBytes(file.toPath()));
    }

    public static byte[] sha1(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(bytes);
        }catch (NoSuchAlgorithmException e) {
            //how there is no this algorithm
            return new byte[0];
        }
    }


    public static byte[] md5(String text) throws NoSuchAlgorithmException {
        return md5(text.getBytes());
    }

    public static byte[] md5(File file) throws NoSuchAlgorithmException, IOException {
        return md5(Files.readAllBytes(file.toPath()));
    }

    public static byte[] md5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        return digest.digest(bytes);
    }

}
