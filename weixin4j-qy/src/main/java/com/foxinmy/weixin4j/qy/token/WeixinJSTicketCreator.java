package com.foxinmy.weixin4j.qy.token;

import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.HttpRequest;
import com.foxinmy.weixin4j.http.Response;
import com.foxinmy.weixin4j.model.Consts;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.token.TokenCreator;
import com.foxinmy.weixin4j.token.TokenHolder;

/**
 * 微信企业号JSTICKET创建者
 * 
 * @className WeixinJSTicketCreator
 * @author jy
 * @date 2015年1月10日
 * @since JDK 1.7
 * @see <a
 *      href="http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD.951-JS-SDK.E4.BD.BF.E7.94.A8.E6.9D.83.E9.99.90.E7.AD.BE.E5.90.8D.E7.AE.97.E6.B3.95">JS
 *      TICKET</a>
 */
public class WeixinJSTicketCreator implements TokenCreator {

	private final String appid;
	private final TokenHolder weixinTokenHolder;
	private final HttpRequest request;

	/**
	 * <font color="red">企业号的的access_token</font>
	 * 
	 * @param weixinTokenHolder
	 */
	public WeixinJSTicketCreator(String appid, TokenHolder weixinTokenHolder) {
		this.appid = appid;
		this.weixinTokenHolder = weixinTokenHolder;
		this.request = new HttpRequest();
	}

	@Override
	public String getCacheKey() {
		return String.format("qy_jsticket_%s", appid);
	}

	@Override
	public Token createToken() throws WeixinException {
		Response response = request.get(String.format(Consts.QY_JS_TICKET_URL,
				weixinTokenHolder.getToken().getAccessToken()));
		JSONObject result = response.getAsJson();
		Token token = new Token(result.getString("ticket"));
		token.setExpiresIn(result.getIntValue("expires_in"));
		token.setTime(System.currentTimeMillis());
		return token;
	}
}
