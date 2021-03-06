package com.foxinmy.weixin4j.mp.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.mime.content.ByteArrayBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.JsonResult;
import com.foxinmy.weixin4j.http.PartParameter;
import com.foxinmy.weixin4j.http.Response;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.mp.model.CustomRecord;
import com.foxinmy.weixin4j.mp.model.KfAccount;
import com.foxinmy.weixin4j.mp.model.KfSession;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.IOUtil;

/**
 * 多客服API
 * 
 * @className CustomApi
 * @author jy
 * @date 2014年11月16日
 * @since JDK 1.7
 * @see <a href="http://dkf.qq.com">多客服说明</a>
 * @see<a 
 *        href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html"
 *        >多客服说明</a>
 */
public class CustomApi extends MpApi {

	private final TokenHolder tokenHolder;

	public CustomApi(TokenHolder tokenHolder) {
		this.tokenHolder = tokenHolder;
	}

	/**
	 * 客服聊天记录
	 * 
	 * @param openId
	 *            用户标识 可为空
	 * @param starttime
	 *            查询开始时间
	 * @param endtime
	 *            查询结束时间 每次查询不能跨日查询
	 * @param pagesize
	 *            每页大小 每页最多拉取50条
	 * @param pageindex
	 *            查询第几页 从1开始
	 * @throws WeixinException
	 * @see com.foxinmy.weixin4j.mp.model.CustomRecord
	 * @see <a href="http://dkf.qq.com/document-1_1.html">查询客服聊天记录</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/19/7c129ec71ddfa60923ea9334557e8b23.html">查询客服聊天记录</a>
	 */
	public List<CustomRecord> getCustomRecord(String openId, Date starttime,
			Date endtime, int pagesize, int pageindex) throws WeixinException {
		JSONObject obj = new JSONObject();
		obj.put("openId", openId == null ? "" : openId);
		obj.put("starttime", starttime.getTime() / 1000);
		obj.put("endtime", endtime.getTime() / 1000);
		obj.put("pagesize", pagesize > 1000 ? 1000 : pagesize);
		obj.put("pageindex", pageindex);
		String custom_record_uri = getRequestUri("custom_record_uri");
		Token token = tokenHolder.getToken();
		Response response = request.post(
				String.format(custom_record_uri, token.getAccessToken()),
				obj.toJSONString());

		String text = response.getAsJson().getString("recordlist");
		return JSON.parseArray(text, CustomRecord.class);
	}

	/**
	 * 获取公众号中所设置的客服基本信息，包括客服工号、客服昵称、客服登录账号
	 * 
	 * @param isOnline
	 *            是否在线 为ture时可以可以获取客服在线状态（手机在线、PC客户端在线、手机和PC客户端全都在线）、客服自动接入最大值、
	 *            客服当前接待客户数
	 * @return 多客服信息列表
	 * @see com.foxinmy.weixin4j.mp.model.KfAccount
	 * @see <a href="http://dkf.qq.com/document-3_1.html">获取客服基本信息</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E8.8E.B7.E5.8F.96.E5.AE.A2.E6.9C.8D.E5.9F.BA.E6.9C.AC.E4.BF.A1.E6.81.AF">获取客服基本信息</a>
	 * @see <a href="http://dkf.qq.com/document-3_2.html">获取在线客服接待信息</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E8.8E.B7.E5.8F.96.E5.9C.A8.E7.BA.BF.E5.AE.A2.E6.9C.8D.E6.8E.A5.E5.BE.85.E4.BF.A1.E6.81.AF">获取在线客服接待信息</a>
	 * @throws WeixinException
	 */
	public List<KfAccount> getKfAccountList(boolean isOnline)
			throws WeixinException {
		Token token = tokenHolder.getToken();
		String text = "";
		if (isOnline) {
			String getonlinekflist_uri = getRequestUri("getonlinekflist_uri");
			Response response = request.post(String.format(getonlinekflist_uri,
					token.getAccessToken()));
			text = response.getAsJson().getString("kf_online_list");
		} else {
			String getkflist_uri = getRequestUri("getkflist_uri");
			Response response = request.post(String.format(getkflist_uri,
					token.getAccessToken()));
			text = response.getAsJson().getString("kf_list");
		}
		return JSON.parseArray(text, KfAccount.class);
	}

	/**
	 * 新增客服账号
	 * 
	 * @param id
	 *            完整客服账号，格式为：账号前缀@公众号微信号，账号前缀最多10个字符，必须是英文或者数字字符。如果没有公众号微信号，
	 *            请前往微信公众平台设置。
	 * @param name
	 *            客服昵称，最长6个汉字或12个英文字符
	 * @param pwd
	 *            客服账号登录密码
	 * @return 处理结果
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E5.AE.A2.E6.9C.8D.E7.AE.A1.E7.90.86.E6.8E.A5.E5.8F.A3.E8.BF.94.E5.9B.9E.E7.A0.81.E8.AF.B4.E6.98.8E">客服管理接口返回码</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E6.B7.BB.E5.8A.A0.E5.AE.A2.E6.9C.8D.E8.B4.A6.E5.8F.B7">新增客服账号</a>
	 */
	public JsonResult addAccount(String id, String name, String pwd)
			throws WeixinException {
		JSONObject obj = new JSONObject();
		obj.put("kf_account", id);
		obj.put("nickname", name);
		obj.put("password", DigestUtils.md5Hex(pwd));
		String custom_add_uri = getRequestUri("custom_add_uri");
		Token token = tokenHolder.getToken();
		Response response = request.post(
				String.format(custom_add_uri, token.getAccessToken()),
				obj.toJSONString());
		return response.getAsJsonResult();
	}

	/**
	 * 更新客服账号
	 * 
	 * @param id
	 *            完整客服账号，格式为：账号前缀@公众号微信号，账号前缀最多10个字符，必须是英文或者数字字符。如果没有公众号微信号，
	 *            请前往微信公众平台设置。
	 * @param name
	 *            客服昵称，最长6个汉字或12个英文字符
	 * @param pwd
	 *            客服账号登录密码
	 * @return 处理结果
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E5.AE.A2.E6.9C.8D.E7.AE.A1.E7.90.86.E6.8E.A5.E5.8F.A3.E8.BF.94.E5.9B.9E.E7.A0.81.E8.AF.B4.E6.98.8E">客服管理接口返回码</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E8.AE.BE.E7.BD.AE.E5.AE.A2.E6.9C.8D.E4.BF.A1.E6.81.AF">新增客服账号</a>
	 */
	public JsonResult updateAccount(String id, String name, String pwd)
			throws WeixinException {
		JSONObject obj = new JSONObject();
		obj.put("kf_account", id);
		obj.put("nickname", name);
		obj.put("password", DigestUtils.md5Hex(pwd));
		String custom_update_uri = getRequestUri("custom_update_uri");
		Token token = tokenHolder.getToken();
		Response response = request.post(
				String.format(custom_update_uri, token.getAccessToken()),
				obj.toJSONString());
		return response.getAsJsonResult();
	}

	/**
	 * 上传客服头像
	 * 
	 * @param id
	 *            完整客服账号，格式为：账号前缀@公众号微信号
	 * @param headimg
	 *            头像图片文件必须是jpg格式，推荐使用640*640大小的图片以达到最佳效果
	 * @return 处理结果
	 * @throws WeixinException
	 * @throws IOException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E5.AE.A2.E6.9C.8D.E7.AE.A1.E7.90.86.E6.8E.A5.E5.8F.A3.E8.BF.94.E5.9B.9E.E7.A0.81.E8.AF.B4.E6.98.8E">客服管理接口返回码</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E4.B8.8A.E4.BC.A0.E5.AE.A2.E6.9C.8D.E5.A4.B4.E5.83.8F">上传客服头像</a>
	 */
	public JsonResult uploadAccountHeadimg(String id, File headimg)
			throws WeixinException, IOException {
		Token token = tokenHolder.getToken();
		String custom_uploadheadimg_uri = getRequestUri("custom_uploadheadimg_uri");
		byte[] bytes = IOUtil.toByteArray(new FileInputStream(headimg));
		Response response = request.post(
				String.format(custom_uploadheadimg_uri, token.getAccessToken(),
						id),
				new PartParameter("media", new ByteArrayBody(bytes, headimg
						.getName())));

		return response.getAsJsonResult();
	}

	/**
	 * 删除客服账号
	 * 
	 * @param id
	 *            完整客服账号，格式为：账号前缀@公众号微信号
	 * @return 处理结果
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E5.AE.A2.E6.9C.8D.E7.AE.A1.E7.90.86.E6.8E.A5.E5.8F.A3.E8.BF.94.E5.9B.9E.E7.A0.81.E8.AF.B4.E6.98.8E">客服管理接口返回码</a>
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/9/6fff6f191ef92c126b043ada035cc935.html#.E5.88.A0.E9.99.A4.E5.AE.A2.E6.9C.8D.E8.B4.A6.E5.8F.B7">删除客服账号</a>
	 */
	public JsonResult deleteAccount(String id) throws WeixinException {
		Token token = tokenHolder.getToken();
		String custom_delete_uri = getRequestUri("custom_delete_uri");
		Response response = request.get(String.format(custom_delete_uri,
				token.getAccessToken(), id));

		return response.getAsJsonResult();
	}

	/**
	 * 创建会话
	 * <p>
	 * 开发者可以使用本接口，为多客服的客服工号创建会话，将某个客户直接指定给客服工号接待，需要注意此接口不会受客服自动接入数以及自动接入开关限制。
	 * 只能为在线的客服（PC客户端在线，或者已绑定多客服助手）创建会话。
	 * </p>
	 * 
	 * @param userOpenId
	 *            用户的userOpenId
	 * @param kfAccount
	 *            完整客服账号，格式为：账号前缀@公众号微信号
	 * @param text
	 *            附加信息，文本会展示在客服人员的多客服客户端
	 * @return 处理结果
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/2/6c20f3e323bdf5986cfcb33cbd3b829a.html#.E5.88.9B.E5.BB.BA.E4.BC.9A.E8.AF.9D">创建会话</a>
	 */
	public JsonResult createKfSession(String userOpenId, String kfAccount,
			String text) throws WeixinException {
		Token token = tokenHolder.getToken();
		String kfsession_create_uri = getRequestUri("kfsession_create_uri");
		JSONObject obj = new JSONObject();
		obj.put("openid", userOpenId);
		obj.put("kf_account", kfAccount);
		obj.put("text", text);
		Response response = request.post(
				String.format(kfsession_create_uri, token.getAccessToken()),
				obj.toJSONString());

		return response.getAsJsonResult();
	}

	/**
	 * 关闭会话
	 * 
	 * @param userOpenId
	 *            用户的userOpenId
	 * @param kfAccount
	 *            完整客服账号，格式为：账号前缀@公众号微信号
	 * @param text
	 *            附加信息，文本会展示在客服人员的多客服客户端
	 * @return 处理结果
	 * @throws WeixinException
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/2/6c20f3e323bdf5986cfcb33cbd3b829a.html#.E5.85.B3.E9.97.AD.E4.BC.9A.E8.AF.9D">创建会话</a>
	 */
	public JsonResult closeKfSession(String userOpenId, String kfAccount,
			String text) throws WeixinException {
		Token token = tokenHolder.getToken();
		String kfsession_close_uri = getRequestUri("kfsession_close_uri");
		JSONObject obj = new JSONObject();
		obj.put("openid", userOpenId);
		obj.put("kf_account", kfAccount);
		obj.put("text", text);
		Response response = request.post(
				String.format(kfsession_close_uri, token.getAccessToken()),
				obj.toJSONString());

		return response.getAsJsonResult();
	}

	/**
	 * 获取客户的会话状态:获取客户当前的会话状态。
	 * 
	 * @param userOpenId
	 *            用户的openid
	 * @return 会话对象
	 * @throws WeixinException
	 * @see com.foxinmy.weixin4j.mp.model.KfSession
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/2/6c20f3e323bdf5986cfcb33cbd3b829a.html#.E8.8E.B7.E5.8F.96.E5.AE.A2.E6.88.B7.E7.9A.84.E4.BC.9A.E8.AF.9D.E7.8A.B6.E6.80.81">获取会话状态</a>
	 */
	public KfSession getKfSession(String userOpenId) throws WeixinException {
		Token token = tokenHolder.getToken();
		String kfsession_get_uri = getRequestUri("kfsession_get_uri");
		Response response = request.get(String.format(kfsession_get_uri,
				token.getAccessToken(), userOpenId));

		KfSession session = response
				.getAsObject(new TypeReference<KfSession>() {
				});
		session.setUserOpenId(userOpenId);
		return session;
	}

	/**
	 * 获取客服的会话列表:获取某个客服正在接待的会话列表。
	 * 
	 * @param kfAccount
	 *            完整客服账号，格式为：账号前缀@公众号微信号，账号前缀最多10个字符，必须是英文或者数字字符。
	 * @return 会话列表
	 * @throws WeixinException
	 * @see com.foxinmy.weixin4j.mp.model.KfSession
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/2/6c20f3e323bdf5986cfcb33cbd3b829a.html#.E8.8E.B7.E5.8F.96.E5.AE.A2.E6.9C.8D.E7.9A.84.E4.BC.9A.E8.AF.9D.E5.88.97.E8.A1.A8">获取客服的会话列表</a>
	 */
	public List<KfSession> getKfSessionList(String kfAccount)
			throws WeixinException {
		Token token = tokenHolder.getToken();
		String kfsession_list_uri = getRequestUri("kfsession_list_uri");
		Response response = request.get(String.format(kfsession_list_uri,
				token.getAccessToken(), kfAccount));

		List<KfSession> sessionList = JSON.parseArray(response.getAsJson()
				.getString("sessionlist"), KfSession.class);
		return sessionList;
	}

	/**
	 * 获取未接入会话列表:获取当前正在等待队列中的会话列表，此接口最多返回最早进入队列的100个未接入会话。</br>
	 * <font color="red">缺陷：没有count字段</font>
	 * @return 会话列表
	 * @throws WeixinException
	 * @see com.foxinmy.weixin4j.mp.model.KfSession
	 * @see <a
	 *      href="http://mp.weixin.qq.com/wiki/2/6c20f3e323bdf5986cfcb33cbd3b829a.html#.E8.8E.B7.E5.8F.96.E6.9C.AA.E6.8E.A5.E5.85.A5.E4.BC.9A.E8.AF.9D.E5.88.97.E8.A1.A8">获取客服的会话列表</a>
	 */
	public List<KfSession> getKfSessionWaitList() throws WeixinException {
		Token token = tokenHolder.getToken();
		String kfsession_wait_uri = getRequestUri("kfsession_wait_uri");
		Response response = request.get(String.format(kfsession_wait_uri,
				token.getAccessToken()));

		List<KfSession> sessionList = JSON.parseArray(response.getAsJson()
				.getString("waitcaselist"), KfSession.class);
		return sessionList;
	}
}