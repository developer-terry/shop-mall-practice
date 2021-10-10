package com.practice.shopmall.order.web;

import com.practice.shopmall.order.service.OrderService;
import com.practice.shopmall.order.vo.OrderConfirmVo;
import com.practice.shopmall.order.vo.OrderSubmitVo;
import com.practice.shopmall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {

        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
        model.addAttribute("confirmOrder", orderConfirmVo);

        return "confirm";
    }

    @GetMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
        SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);
        if(submitOrderResponseVo.getCode() == 0){
            model.addAttribute("order", submitOrderResponseVo);
            return "pay";
        }

        String msg = "下單失敗：";

        switch (submitOrderResponseVo.getCode()) {
            case 1:
                msg += "訂單過期，請重整頁面後再提交";
                break;
            case 2:
                msg += "訂單商品價格發生變化，請確認後再次提交";
                break;
            case 3:
                msg += "庫存庫存不足";
                break;
        }
        redirectAttributes.addFlashAttribute("msg", msg);
        return "redirect:http://order.shopmall-test.com/toTrade";
    }
}
