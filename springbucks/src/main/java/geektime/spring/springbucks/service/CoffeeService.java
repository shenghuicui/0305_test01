package geektime.spring.springbucks.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.repository.CoffeeRepository;
import geektime.spring.springbucks.untils.RedisUntil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    @Value("${spring.redis.host}")
    private String url;

    @Value("${spring.redis.port}")
    private Integer port;

    @Transactional(rollbackOn = Exception.class)
    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }


    @Transactional(rollbackOn = Exception.class)
    public void save(Coffee coffee) throws Exception {
        try {
            coffeeRepository.save(coffee);
        } catch (Exception e) {
            log.error("保存失败");
            throw new Exception();
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public Coffee update(Coffee coffee) throws Exception {
        try {
            return coffeeRepository.save(coffee);
        } catch (Exception e) {
            log.error("修改失败");
            throw new Exception();
        }
    }


    @Transactional(rollbackOn = Exception.class)
    public void deleter(Long id) throws Exception {
        try {
            coffeeRepository.deleteById(id);
        } catch (Exception e) {
            log.error("删除失败");
            throw new Exception();
        }
    }


    @Transactional(rollbackOn = Exception.class)
    public List<Coffee> findAll() {
        List<Coffee> coffees = coffeeRepository.findAll();
        JedisCluster jedisCluster = RedisUntil.getJedisCluster(url, port);

        for (Coffee coffee : coffees) {
            jedisCluster.setex(String.valueOf(coffee.getId()), 60 * 60 * 24, coffee.toString());
        }

        String collect = coffees.stream().map(x -> String.valueOf(x.getId())).collect(Collectors.joining(
                ","));
        List<String> mget = jedisCluster.mget(collect);
        mget.forEach(System.out::println);
        return coffees;
    }

    @Transactional(rollbackOn = Exception.class)
    public PageInfo<Coffee> findAllByPage() {
        PageHelper.startPage(1, 2);
        Page<Coffee> pageList = coffeeRepository.findAll();
        PageInfo<Coffee> pageinfo = new PageInfo<>(pageList);
        pageinfo.setPageSize(2);
        pageinfo.setPageNum(1);
        return pageinfo;
    }

}
