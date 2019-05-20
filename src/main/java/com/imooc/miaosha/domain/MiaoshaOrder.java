package com.imooc.miaosha.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MiaoshaOrder {
    @Id
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
