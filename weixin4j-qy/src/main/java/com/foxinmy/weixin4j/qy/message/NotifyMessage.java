package com.foxinmy.weixin4j.qy.message;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.foxinmy.weixin4j.tuple.NotifyTuple;

/**
 * 发送消息对象
 * 
 * @className NotifyMessage
 * @author jy
 * @date 2014年11月22日
 * @since JDK 1.7
 * @see com.foxinmy.weixin4j.tuple.Text
 * @see com.foxinmy.weixin4j.tuple.Image
 * @see com.foxinmy.weixin4j.tuple.Voice
 * @see com.foxinmy.weixin4j.tuple.Video
 * @see com.foxinmy.weixin4j.tuple.File
 * @see com.foxinmy.weixin4j.tuple.News
 * @see com.foxinmy.weixin4j.tuple.MpNews
 * @see <a
 *      href="http://qydev.weixin.qq.com/wiki/index.php?title=%E6%B6%88%E6%81%AF%E7%B1%BB%E5%9E%8B%E5%8F%8A%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F">消息说明</a>
 */
public class NotifyMessage implements Serializable {

	private static final long serialVersionUID = 1219589414293000383L;
	
	private static final String SEPARATOR = "|";

	/**
	 * UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	private String touser;
	/**
	 * PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
	 */
	private String toparty;
	/**
	 * TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
	 */
	private String totag;
	/**
	 * 企业应用的id，整型。可在应用的设置页面查看
	 */
	private int agentid;
	/**
	 * 表示是否是保密消息，0表示否，1表示是，默认0
	 */
	private int safe;
	/**
	 * 消息对象
	 */
	@JSONField(serialize = false)
	private NotifyTuple tuple;

	public NotifyMessage(NotifyTuple tuple, int agentid) {
		this(null, null, null, tuple, agentid, false);
		this.touser = "@all";
	}

	public NotifyMessage(List<String> tousers, List<String> topartys,
			List<String> totags, NotifyTuple tuple, int agentid, boolean isSafe) {
		if (tousers != null && !tousers.isEmpty()) {
			this.touser = StringUtils.join(tousers, SEPARATOR);
		}
		if (topartys != null && !topartys.isEmpty()) {
			this.toparty = StringUtils.join(topartys, SEPARATOR);
		}
		if (totags != null && !totags.isEmpty()) {
			this.totag = StringUtils.join(totags, SEPARATOR);
		}
		this.agentid = agentid;
		this.safe = isSafe ? 1 : 0;
		this.tuple = tuple;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public void setTouser(List<String> tousers) {
		if (tousers != null && !tousers.isEmpty()) {
			this.touser = StringUtils.join(tousers, SEPARATOR);
		}
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public void setToparty(List<String> topartys) {
		if (topartys != null && !topartys.isEmpty()) {
			this.toparty = StringUtils.join(topartys, SEPARATOR);
		}
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(String totag) {
		this.totag = totag;
	}

	public void setTotag(List<String> totags) {
		if (totags != null && !totags.isEmpty()) {
			this.totag = StringUtils.join(totags, SEPARATOR);
		}
	}

	public int getAgentid() {
		return agentid;
	}

	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}

	public int getSafe() {
		return safe;
	}

	public void setSafe(int safe) {
		this.safe = safe;
	}

	public void setSafe(boolean isSafe) {
		this.safe = isSafe ? 1 : 0;
	}

	public NotifyTuple getTuple() {
		return tuple;
	}

	public void setTuple(NotifyTuple tuple) {
		this.tuple = tuple;
	}

	@Override
	public String toString() {
		return "NotifyMessage [touser=" + touser + ", toparty=" + toparty
				+ ", totag=" + totag + ", agentid=" + agentid + ", safe="
				+ safe + ", tuple=" + tuple + "]";
	}
}
