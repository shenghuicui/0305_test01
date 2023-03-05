package geektime.spring.springbucks.repository;

import com.github.pagehelper.Page;
import geektime.spring.springbucks.model.Coffee;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

    @Select("SELECT  * FROM t_coffee order by id desc")
    Page<Coffee> findAll();

    @Select("SELECT  * FROM t_coffee where name =#{name} order by id desc")
    Page<Coffee> findAllByID(@Param("name") String name);

    @Select("SELECT  * FROM t_coffee where id =#{id} order by id desc")
    Page<Coffee> findAllByName(@Param("id") String id);
}
