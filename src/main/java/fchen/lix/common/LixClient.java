package fchen.lix.common;

import java.io.IOException;
import net.spy.memcached.*;


public class LixClient {
  private final static String HOST = "13.77.177.4";
  private final static String PORT = "11211";
  private MemcachedClient mc;

  public LixClient() {
    try {
      mc = new MemcachedClient(
          new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.TEXT).build(),
          AddrUtil.getAddresses(HOST + ":" +PORT)
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getTreatment(String lixKey) {
    return (String) mc.get(lixKey);
  }

  public void close() {
    if (mc != null) {
      mc.shutdown();
    }
  }
}
