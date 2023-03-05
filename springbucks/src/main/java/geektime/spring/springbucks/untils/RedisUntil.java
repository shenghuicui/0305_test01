package geektime.spring.springbucks.untils;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * @ClassName RedisUntil
 * @Description //TODO
 * @Author lkk
 * Date 2023/03/03/10:38
 * @Version 1.0
 **/
public class RedisUntil {


    public static JedisCluster getJedisCluster(String url,Integer port){
        HostAndPort hostAndPort =new HostAndPort(url,port);
        JedisCluster jedisCluster =new JedisCluster(hostAndPort);
        return jedisCluster;
    }



}
