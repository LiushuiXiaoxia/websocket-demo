package cn.mycommons.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class ClientMainTest {

    static class HTTPSTrustManager implements X509TrustManager {

        private final X509Certificate[] acceptedIssuers = new X509Certificate[0];

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return acceptedIssuers;
        }
    }

    public static X509TrustManager getX509TrustManager() {
        return new HTTPSTrustManager();
    }

    public static SSLSocketFactory getSSLSocketFactory() throws RuntimeException {
        try {

            InputStream stream = new FileInputStream("baiducom.crt");
            // 使用默认证书
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 去掉系统默认证书
            keystore.load(null);
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(stream);
            // 设置自己的证书
            keystore.setCertificateEntry("baidu", certificate);
            // 通过信任管理器获取一个默认的算法
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            // 算法工厂创建
            TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
            factory.init(keystore);

            TrustManager[] trustManagers = factory.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("getSSLSocketFactory fail");
    }

    @Test
    public void testSsl() {
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888)))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        System.out.println("verify s = " + s);
                        System.out.println("verify sslSession = " + sslSession);
                        return true;
                    }
                })
                .sslSocketFactory(getSSLSocketFactory())
                .build();

        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("response = " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}