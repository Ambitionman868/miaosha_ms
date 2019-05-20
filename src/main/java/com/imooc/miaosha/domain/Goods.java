package com.imooc.miaosha.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Goods {

    @Id
	private Long id;
	private String goodsName;
	private String goodsTitle;
	private String goodsImg;
	private String goodsDetail;
	private Double goodsPrice;
	private Integer goodsStock;
}
