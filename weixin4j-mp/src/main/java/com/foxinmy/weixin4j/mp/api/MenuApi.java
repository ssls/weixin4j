package com.foxinmy.weixin4j.mp.api;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.JsonResult;
import com.foxinmy.weixin4j.http.Response;
import com.foxinmy.weixin4j.model.Button;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.type.ButtonType;

/**
 * 菜单相关API
 * 
 * @className MenuApi
 * @author jy.hu
 * @date 2014年9月25日
 * @since JDK 1.7
 * @see com.foxinmy.weixin4j.model.Button
 */
public class MenuApi extends MpApi {

	private final TokenHolder tokenHolder;

	public MenuApi(TokenHolder tokenHolder) {
		this.tokenHolder = tokenHolder;
	}

	/**
	 * 自定义菜单
	 * 
	 * @param btnList
	 *            菜单列表
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html">创建自定义菜单</a>
	 * @see com.foxinmy.weixin4j.model.Button
	 */
	public JsonResult createMenu(List<Button> btnList) throws WeixinException {
		String menu_create_uri = getRequestUri("menu_create_uri");
		Token token = tokenHolder.getToken();
		JSONObject obj = new JSONObject();
		obj.put("button", btnList);
		Response response = request.post(
				String.format(menu_create_uri, token.getAccessToken()),
				JSON.toJSONString(obj, new NameFilter() {
					@Override
					public String process(Object object, String name,
							Object value) {
						if (object instanceof Button && name.equals("content")) {
							if (((Button) object).getFormatType() == ButtonType.view) {
								return "url";
							} else {
								return "key";
							}
						}
						return name;
					}
				}));

		return response.getAsJsonResult();
	}

	/**
	 * 查询菜单
	 * 
	 * @return 菜单集合
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/16/ff9b7b85220e1396ffa16794a9d95adc.html">查询菜单</a>
	 * @see com.foxinmy.weixin4j.model.Button
	 */
	public List<Button> getMenu() throws WeixinException {
		String menu_get_uri = getRequestUri("menu_get_uri");
		Token token = tokenHolder.getToken();
		Response response = request.get(String.format(menu_get_uri,
				token.getAccessToken()));

		String text = JSON.toJSONString(
				response.getAsJson().getJSONObject("menu")
						.getJSONArray("button"), new NameFilter() {
					@Override
					public String process(Object object, String name,
							Object value) {
						if (name.equals("url") || name.equals("key")) {
							return "content";
						}
						return name;
					}
				});
		return JSON.parseArray(text, Button.class);
	}

	/**
	 * 删除菜单
	 * 
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/16/8ed41ba931e4845844ad6d1eeb8060c8.html">删除菜单</a>
	 * @return 处理结果
	 */
	public JsonResult deleteMenu() throws WeixinException {
		String menu_delete_uri = getRequestUri("menu_delete_uri");
		Token token = tokenHolder.getToken();
		Response response = request.get(String.format(menu_delete_uri,
				token.getAccessToken()));

		return response.getAsJsonResult();
	}
}
