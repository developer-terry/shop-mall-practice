package com.practice.shopmall.cart.controller;

import com.practice.common.constant.AuthServiceConstant;
import com.practice.shopmall.cart.interceptor.CartInterceptor;
import com.practice.shopmall.cart.service.CartService;
import com.practice.shopmall.cart.vo.Cart;
import com.practice.shopmall.cart.vo.CartItem;
import com.practice.shopmall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems(){
        return cartService.getCurrentUserCartItems();
    }

    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId, @RequestParam("isChecked") Integer checked){
        cartService.checkItem(skuId, checked);
        return "redirect:http://cart.shopmall-test.com/cart.html";
    }

    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num){
        cartService.countItem(skuId, num);
        return "redirect:http://cart.shopmall-test.com/cart.html";
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.shopmall-test.com/cart.html";
    }

    @GetMapping("/cart.html")
    public String cartListPage(Model model){

//        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();

        Cart cart = cartService.getCart();
        System.out.println(cart);
        model.addAttribute("cart", cart);
        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes){

        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
//        model.addAttribute("skuId", skuId);
//        model.addAttribute("cartItem", cartItem);

        return "redirect:http://cart.shopmall-test.com/addToCartSuccess.html";
    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model){

        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItem);
        return "success";
    }
}
