/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.loading.FMLPaths
 */
package ru.lhs.auth.client;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.minecraftforge.fml.loading.FMLPaths;

public final class AuthStore {
    private static final SecureRandom RNG = new SecureRandom();

    private AuthStore() {
    }

    private static Path file() {
        return FMLPaths.CONFIGDIR.get().resolve("lhs_auth_session.dat");
    }

    private static SecretKeySpec key() throws Exception {
        String string = "LHS-AUTH-v2|" + System.getProperty("user.name") + "|" + System.getProperty("user.home") + "|" + System.getProperty("os.name") + "|" + System.getProperty("os.arch");
        byte[] byArray = MessageDigest.getInstance("SHA-256").digest(string.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(byArray, "AES");
    }

    public static Creds load() {
        try {
            Path path = AuthStore.file();
            if (!Files.isRegularFile(path, new LinkOption[0])) {
                return null;
            }
            byte[] byArray = Base64.getDecoder().decode(Files.readString(path, StandardCharsets.UTF_8).trim());
            if (byArray.length < 29) {
                return null;
            }
            byte[] byArray2 = new byte[12];
            System.arraycopy(byArray, 0, byArray2, 0, 12);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, (Key)AuthStore.key(), new GCMParameterSpec(128, byArray2));
            String string = new String(cipher.doFinal(byArray, 12, byArray.length - 12), StandardCharsets.UTF_8);
            int n = string.indexOf(10);
            if (n <= 0 || n >= string.length() - 1) {
                return null;
            }
            return new Creds(string.substring(0, n), string.substring(n + 1));
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    public static void save(String string, String string2) {
        try {
            byte[] byArray = new byte[12];
            RNG.nextBytes(byArray);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(1, (Key)AuthStore.key(), new GCMParameterSpec(128, byArray));
            byte[] byArray2 = cipher.doFinal((string + "\n" + string2).getBytes(StandardCharsets.UTF_8));
            byte[] byArray3 = new byte[12 + byArray2.length];
            System.arraycopy(byArray, 0, byArray3, 0, 12);
            System.arraycopy(byArray2, 0, byArray3, 12, byArray2.length);
            Path path = AuthStore.file();
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            Files.writeString(path, (CharSequence)Base64.getEncoder().encodeToString(byArray3), StandardCharsets.UTF_8, new OpenOption[0]);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void clear() {
        try {
            Files.deleteIfExists(AuthStore.file());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static final class Creds {
        public final String user;
        public final String pass;

        Creds(String string, String string2) {
            this.user = string;
            this.pass = string2;
        }
    }
}
