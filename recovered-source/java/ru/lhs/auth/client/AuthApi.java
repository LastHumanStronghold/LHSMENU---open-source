/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 */
package ru.lhs.auth.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import net.minecraft.client.Minecraft;

public final class AuthApi {
    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8L)).build();
    private static volatile String accessToken;

    public static void authenticate(String mode, String username, String password, Callback callback) {
        Minecraft mc = Minecraft.m_91087_();
        String uuid = mc.f_91074_ == null ? "" : mc.f_91074_.m_20148_().toString();
        String mcName = mc.f_91074_ == null ? username : mc.f_91074_.m_36316_().getName();
        String body = "{\"mode\":\"" + AuthApi.esc(mode) + "\",\"username\":\"" + AuthApi.esc(username) + "\",\"password\":\"" + AuthApi.esc(password) + "\",\"minecraft_uuid\":\"" + AuthApi.esc(uuid) + "\",\"minecraft_username\":\"" + AuthApi.esc(mcName) + "\"}";
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://yzfpamwgdaixylsphhig.functions.supabase.co/lhs-auth")).timeout(Duration.ofSeconds(12L)).header("Content-Type", "application/json").header("x-lhs-client", "LHS-Launcher-2026-auth-v1").POST(HttpRequest.BodyPublishers.ofString(body)).build();
        HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString()).whenComplete((response, error) -> {
            if (error != null) {
                callback.done(false, "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f \u043a LHS", null);
                return;
            }
            try {
                String message;
                boolean ok;
                JsonObject json = JsonParser.parseString((String)((String)response.body())).getAsJsonObject();
                boolean bl = ok = json.has("ok") && json.get("ok").getAsBoolean();
                String string = json.has("error") ? json.get("error").getAsString() : (message = ok ? "OK" : "\u041e\u0448\u0438\u0431\u043a\u0430 \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u0438");
                if (ok && json.has("access_token") && !json.get("access_token").isJsonNull()) {
                    accessToken = json.get("access_token").getAsString();
                }
                callback.done(ok, message, json);
            }
            catch (Exception e) {
                callback.done(false, "\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0439 \u043e\u0442\u0432\u0435\u0442 \u0441\u0435\u0440\u0432\u0435\u0440\u0430", null);
            }
        });
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void clearSession() {
        accessToken = null;
    }

    private static String esc(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private AuthApi() {
    }

    public static interface Callback {
        public void done(boolean var1, String var2, JsonObject var3);
    }
}
