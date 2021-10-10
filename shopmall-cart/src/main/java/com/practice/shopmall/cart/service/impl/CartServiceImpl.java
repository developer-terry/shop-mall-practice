package com.practice.shopmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.practice.common.utils.R;
import com.practice.shopmall.cart.feign.ProductFeignService;
import com.practice.shopmall.cart.interceptor.CartInterceptor;
import com.practice.shopmall.cart.service.CartService;
import com.practice.shopmall.cart.vo.Cart;
import com.practice.shopmall.cart.vo.CartItem;
import com.practice.shopmall.cart.vo.SkuInfoVo;
import com.practice.shopmall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    private final String CART_PREFIX = "shopmall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) {
        // 得到當前的用戶訊息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        // 遠程查詢產品訊息

        String cartItemString = (String) cartOps.get(skuId.toString());
        if(StringUtils.isEmpty(cartItemString)) {
            //購物車沒有這個商品
            CartItem cartItem = new CartItem();

            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                R r = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = r.getData("skuInfo", new TypeReference<SkuInfoVo>(){});

                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            }, executor);

            // 遠程查詢 sku 的組合訊息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                System.out.println(values);
                System.out.println("===================================");
                cartItem.setSkuAttr(values);
            }, executor);

            try {
                CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValues).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            String cartItemStirng = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), cartItemStirng);

            return cartItem;
        }
        else {
            CartItem cartItem = JSONObject.parseObject(cartItemString, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));

            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String cartItemString = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSONObject.parseObject(cartItemString, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCart() {

        Cart cart = new Cart();

        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();

        if(userInfoTo.getUserId() != null) {
            //  login
            //如果臨時購物車數據還沒有進行合併
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if(tempCartItems != null) {
                // 將臨時購物車的數據合併到登入之後的購物車
                for (CartItem tempCartItem : tempCartItems) {
                    addToCart(tempCartItem.getSkuId(), tempCartItem.getCount());
                }

                clearCart(tempCartKey);
            }

            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
            for(CartItem cartItem : cartItems) {
                System.out.println(1);
                System.out.println(cartItem.getSkuAttr());

//                for(String attr : cartItem.getSkuAttr()) {
//                    System.out.println(attr);
//                }
            }
            System.out.println("==========================");
        } else {
            //  not login
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
            if(cartItems != null) {
                for(CartItem cartItem : cartItems) {
                    System.out.println(cartItem.getSkuAttr());

//                for(String attr : cartItem.getSkuAttr()) {
//                    System.out.println(attr);
//                }
                }
                System.out.println("==========================");
            }
        }

        return cart;
    }

    // 獲取到我們要操作的購物車
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";

        if(userInfoTo.getUserId() != null) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }

        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(cartKey);
        return stringObjectObjectBoundHashOperations;
    }

    private List<CartItem> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = stringObjectObjectBoundHashOperations.values();
        if(values != null && values.size() > 0) {
            List<CartItem> cartItems = values.stream().map(value -> {
                String str = (String) value;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());

            return cartItems;
        }
        return null;
    }

    @Override
    public void clearCart(String cartKey) {
        stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(checked == 1 ? true : false);
        String s = JSON.toJSONString(cartItem);
        getCartOps().put(skuId.toString(), s);
    }

    @Override
    public void countItem(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        String s = JSON.toJSONString(cartItem);
        getCartOps().put(skuId.toString(), s);
    }

    @Override
    public void deleteItem(Long skuId) {
        getCartOps().delete(skuId.toString());
    }

    @Override
    public List<CartItem> getCurrentUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if(userInfoTo.getUserId() == null) {
            return null;
        }

        String cartKey = CART_PREFIX + userInfoTo.getUserId();
        List<CartItem> cartItems = getCartItems(cartKey);
        //獲取被選中的購物項
        cartItems = cartItems.stream().filter(cartItem -> cartItem.getCheck()).map(cartItem -> {
            R r = productFeignService.getPrice(cartItem.getSkuId());
            String data = (String) r.get("data");
            cartItem.setPrice(new BigDecimal(data));
            return cartItem;
        }).collect(Collectors.toList());
        return cartItems;
    }
}
