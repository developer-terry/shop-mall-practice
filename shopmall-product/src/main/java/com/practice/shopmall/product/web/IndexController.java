package com.practice.shopmall.product.web;

import com.practice.shopmall.product.entity.CategoryEntity;
import com.practice.shopmall.product.service.CategoryService;
import com.practice.shopmall.product.vo.CatelogSecondVo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
//@RequestMapping("/")
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model, HttpSession httpSession) {

        //查出所有一級分類
        List<CategoryEntity> categoryEntities = categoryService.getFirstLevelCategories();

        model.addAttribute("categories", categoryEntities);

//        System.out.println(categoryEntities);
        System.out.println(httpSession.getAttribute("loginUser"));

        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<CatelogSecondVo>> getCatalogJson() {

        Map<String, List<CatelogSecondVo>> catalogJson = categoryService.getCatalogJson();
        System.out.println("=========================");
        System.out.println(catalogJson);
        return catalogJson;
    }
//    @RequestMapping("/")
//    public String indexPerson(Model model) {
//        System.out.println("okkokokook");
//        Person single=new Person("Gegegege",30);
//        List<Person> people=new ArrayList<>();
//        Person person1=new Person("a",12);
//        Person person2=new Person("b",14);
//        people.add(person1);
//        people.add(person2);
//        model.addAttribute("singlePerson", single);
//        model.addAttribute("people", people);
//        System.out.println(single.getName());
//        return "people";
//    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        //獲取一把鎖 只要鎖的名字一樣 就是同一把鎖
        RLock rLock = redisson.getLock("my-lock");

        //加鎖
        try {
            //鎖的自動續期 如果業務時間很長 運行期間自動給鎖續上新的 30s 過期時間 避免鎖自動過期被刪掉
            //加鎖的業務只要運行完成 就不會給當前鎖續期 即使不手動解鎖 鎖默認在 30s 後自動刪除
            rLock.lock(); //默認加的鎖都是 30s 時間
            System.out.println("加鎖成功 執行業務 " + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            //解鎖 假設解鎖代碼沒有被運行 redisson 會不會出現死鎖
            System.out.println("釋放鎖 " +  + Thread.currentThread().getId());
            rLock.unlock();
        }

        return "hello";
    }

    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {

        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        RLock rLock = lock.writeLock();

        String uuid = UUID.randomUUID().toString();

        try {
            rLock.lock();
            //改數據加上寫鎖 讀數據加上讀鎖
            Thread.sleep(1000);
            redisTemplate.opsForValue().set("wrtieValue", uuid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        return uuid;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {

        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        RLock rLock = lock.readLock();

        String uuid = "";

        try {
            rLock.lock();
            uuid = (String) redisTemplate.opsForValue().get("wrtieValue");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuid;
    }

    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore rSemaphore = redisson.getSemaphore("park");
        rSemaphore.acquire();//獲取一個信號 獲取一個值 佔一個車位

        return "ok";
    }

    @GetMapping("/go")
    @ResponseBody
    public String go(){
        RSemaphore rSemaphore = redisson.getSemaphore("park");
        rSemaphore.release(); //釋放一個車位

        return "ok";
    }

    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor(){
        RCountDownLatch rCountDownLatch = redisson.getCountDownLatch("door");
        rCountDownLatch.trySetCount(5);

        try {
            rCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "放假了";
    }

    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id){
        RCountDownLatch rCountDownLatch = redisson.getCountDownLatch("door");
        rCountDownLatch.countDown();

        return id + "班級的人都走了";
    }
}
