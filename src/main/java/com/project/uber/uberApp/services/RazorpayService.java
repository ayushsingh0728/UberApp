package com.project.uber.uberApp.services;

import com.project.uber.uberApp.dto.RazorpayOrderRequestDto;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    public String createRazorpayOrder(RazorpayOrderRequestDto razorpayOrderRequestDto) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKey,razorpaySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",razorpayOrderRequestDto.getAmount()*100);
        orderRequest.put("currency",razorpayOrderRequestDto.getCurrency());
        orderRequest.put("receipt",razorpayOrderRequestDto.getReceiptId());

        Order order=razorpayClient.orders.create(orderRequest);
        return order.get("id");
    }
}
