package com.foxinmy.weixin4j.mp.payment.v3;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.foxinmy.weixin4j.mp.model.WeixinMpAccount;
import com.foxinmy.weixin4j.mp.payment.PayPackage;
import com.foxinmy.weixin4j.mp.type.TradeType;
import com.foxinmy.weixin4j.util.RandomUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * V3支付的订单详情
 * 
 * @className PayPackageV3
 * @author jy
 * @date 2014年10月21日
 * @since JDK 1.7
 * @see
 */
@XStreamAlias("xml")
public class PayPackageV3 extends PayPackage {

	private static final long serialVersionUID = 8944928173669656177L;

	/**
	 * 微信分配的公众账号 必须
	 */
	private String appid;
	/**
	 * 微信支付分配的商户号 必须
	 */
	@XStreamAlias("mch_id")
	@JSONField(name = "mch_id")
	private String mchId;
	/**
	 * 微信支付分配的终端设备号 非必须
	 */
	@XStreamAlias("device_info")
	@JSONField(name = "device_info")
	private String deviceInfo;
	/**
	 * 随机字符串,不长于 32 位 必须
	 */
	@XStreamAlias("nonce_str")
	@JSONField(name = "nonce_str")
	private String nonceStr;
	/**
	 * 签名 <font color="red">调用者无需关心</font>
	 */
	private String sign;
	/**
	 * 交易类型JSAPI、NATIVE、APP 必须
	 */
	@XStreamAlias("trade_type")
	@JSONField(name = "trade_type")
	private String tradeType;
	/**
	 * 用户在商户 appid 下的唯一 标识, trade_type 为 JSAPI 时,此参数必传
	 */
	private String openid;
	/**
	 * 只在 trade_type 为 NATIVE 时需要填写 非必须
	 */
	@XStreamAlias("product_id")
	@JSONField(name = "product_id")
	private String productId;

	public PayPackageV3(WeixinMpAccount weixinAccount, String openId,
			String body, String outTradeNo, double totalFee,
			String spbillCreateIp, TradeType tradeType) {
		this(weixinAccount, openId, body, null, outTradeNo, totalFee, null,
				spbillCreateIp, tradeType);
	}

	public PayPackageV3(WeixinMpAccount weixinAccount, String openId,
			String body, String attach, String outTradeNo, double totalFee,
			String notifyUrl, String spbillCreateIp, TradeType tradeType) {
		this(weixinAccount.getId(), weixinAccount.getMchId(), weixinAccount
				.getDeviceInfo(), RandomUtil.generateString(16), body, attach,
				outTradeNo, totalFee, spbillCreateIp, null, null, null,
				notifyUrl, tradeType, openId, null);
	}

	public PayPackageV3(String appid, String mchId, String deviceInfo,
			String nonceStr, String body, String attach, String outTradeNo,
			double totalFee, String spbillCreateIp, Date timeStart,
			Date timeExpire, String goodsTag, String notifyUrl,
			TradeType tradeType, String openid, String productId) {
		super(body, attach, outTradeNo, totalFee, spbillCreateIp, timeStart,
				timeExpire, goodsTag, notifyUrl);
		this.appid = appid;
		this.mchId = mchId;
		this.deviceInfo = deviceInfo;
		this.nonceStr = nonceStr;
		this.tradeType = tradeType.name();
		this.openid = openid;
		this.productId = productId;
	}

	public String getAppid() {
		return appid;
	}

	public String getMchId() {
		return mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTradeType() {
		return tradeType;
	}

	public String getOpenid() {
		return openid;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "PayPackageV3 [appid=" + appid + ", mchId=" + mchId
				+ ", deviceInfo=" + deviceInfo + ", nonceStr=" + nonceStr
				+ ", sign=" + sign + ", tradeType=" + tradeType + ", openid="
				+ openid + ", productId=" + productId + ", " + super.toString()
				+ "]";
	}
}
