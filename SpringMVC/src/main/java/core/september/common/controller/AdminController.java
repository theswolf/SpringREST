package core.september.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.september.common.model.Shop;

@Controller
@RequestMapping("/auth/")
public class AdminController {
	
	@RequestMapping(value = "{api}", method = RequestMethod.GET)
	public @ResponseBody
	Shop getShopInJSON(@PathVariable String api) {

		Shop shop = new Shop();
		shop.setName(api);
		shop.setStaffName(new String[] { "TOKEN NO AUTH" });

		return shop;

	}

}
