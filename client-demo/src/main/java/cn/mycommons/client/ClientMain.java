package cn.mycommons.client;

import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Date;

public class ClientMain {

    static WebSocket socket;

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url();
                        String method = request.method();
                        System.out.println(new Date() + ": " + method + " --> " + url);
                        Response response = chain.proceed(request);
                        System.out.println(new Date() + ": " + response.code());
                        return response;
                    }
                })
                .build();
        Request request = new Request.Builder()
                .url("ws://192.168.237.23:8080/ws/asset")
                .get()
                .build();
        socket = client.newWebSocket(request, new WebSocketListener() {
            
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("onClosed");
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("onFailure code = " + code);
                System.out.println("onFailure reason = " + reason);
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                System.out.println("onFailure t = " + t);
                System.out.println("onFailure response = " + response);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                System.out.println("onMessage2");
                System.out.println("text + " + text);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                System.out.println("onMessage1");
                System.out.println("bytes + " + bytes);
                System.out.println("bytes + " + bytes.string(Charset.defaultCharset()));
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                System.out.println("open");
                socket.send("hehe haha");
            }
        });

        new Thread(() -> {
            while (true) {
                if (socket != null) {
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("now = " + now);
                    socket.send("time = " + now);
                }
                try {
                    Thread.sleep(3_000L);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}