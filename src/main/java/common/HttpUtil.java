package common;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by weiqiang.yuan on 2017/1/14 11:40.
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送http post请求
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> body) {
        return doPost(url, headers, body, 5000, 60000);
    }

    /**
     * 发送http post请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, ListMultimap<String, String> body) {
        return doPost(url, headers, body, 5000, 60000);
    }

    /**
     * 发送http get请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, 5000, 120000);
    }

    /**
     * 发送http get请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url, int connectionTimeOut, int soTimeOut) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            } else {
                logger.warn("请求链接:{}, 返回code:{}", url, code);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    /**
     * 发送http post请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> body,
            int connectionTimeOut, int soTimeOut) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
        if (headers != null) {
            Set<String> headerKeys = headers.keySet();
            // 设置http请求头
            for (String headerKey : headerKeys) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }
        try {
            // 设置http请求内容
            List<NameValuePair> params = Lists.newArrayList();
            Set<String> keys = body.keySet();
            for (String key : keys) {
                params.add(new BasicNameValuePair(key, body.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            } else {
                logger.warn("请求链接:{}, 返回code:{}", url, code);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    /**
     * 发送http post请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, ListMultimap<String, String> body,
            int connectionTimeOut, int soTimeOut) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
        if (headers != null) {
            Set<String> headerKeys = headers.keySet();
            // 设置http请求头
            for (String headerKey : headerKeys) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }
        try {
            // 设置http请求内容
            List<NameValuePair> params = Lists.newArrayList();
            Set<String> keys = body.keySet();
            for (String key : keys) {
                List<String> values = body.get(key);
                for (String value : values) {
                    params.add(new BasicNameValuePair(key, value));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            } else {
                logger.warn("请求链接:{}, 返回code:{}", url, code);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    /**
     * 发送http post请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doHttpsPost(String url, Map<String, String> headers, Map<String, String> body,
            int connectionTimeOut, int soTimeOut) {
        // 设置http请求内容
        List<NameValuePair> params = Lists.newArrayList();
        Set<String> keys = body.keySet();
        for (String key : keys) {
            params.add(new BasicNameValuePair(key, body.get(key)));
        }
        return doHttpsPost(url, headers, params, connectionTimeOut, soTimeOut);
    }

    /**
     * 发送http post请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doHttpsPost(String url, Map<String, String> headers, List<NameValuePair> body,
            int connectionTimeOut, int soTimeOut) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient = wrapHttpsClient(httpClient);
        HttpPost httpPost = new HttpPost(url);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
        httpClient.getParams().setParameter("http.protocol.content-charset", "utf-8");
        if (headers != null) {
            Set<String> headerKeys = headers.keySet();
            // 设置http请求头
            for (String headerKey : headerKeys) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }
        try {
            // 设置http请求内容
            List<NameValuePair> params = body;
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = httpClient.execute(httpPost);
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, response);
            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                return result;
            } else {
                logger.warn("请求链接:{}, 返回code:{}", url, code);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    private static HttpClient wrapHttpsClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, httpClient.getParams());
        } catch (Exception ex) {
            logger.error("wrap client error", ex);
            return null;
        }
    }
}
