package util;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Fairy_LFen on 2017/2/13.
 */
public class ESUtil {
    private static ESUtil esUtil;

    public static ESUtil getInstance(){
        if(esUtil == null){
            esUtil = new ESUtil();
            return esUtil;
        }else{
            return esUtil;
        }
    }

    public TransportClient getTransportClient() throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        return client;
    }
}
