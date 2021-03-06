package com.foxinmy.weixin4j.tuple;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 音乐对象
 * <p>
 * <font color="red">可用于「被动消息」「客服消息」</font>
 * </p>
 * 
 * @className Music
 * @author jy
 * @date 2014年9月29日
 * @since JDK 1.7
 * @see
 */
public class Music implements ResponseTuple, NotifyTuple {

	private static final long serialVersionUID = -5952134916367253297L;

	@Override
	public String getMessageType() {
		return "music";
	}
	
	/**
	 * 音乐标题
	 */
	@XStreamAlias("Title")
	private String title;
	/**
	 * 音乐描述
	 */
	@JSONField(name = "description")
	@XStreamAlias("Description")
	private String desc;
	/**
	 * 音乐链接
	 */
	@JSONField(name = "musicurl")
	@XStreamAlias("MusicUrl")
	private String musicUrl;
	/**
	 * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
	 */
	@JSONField(name = "hqmusicurl")
	@XStreamAlias("HQMusicUrl")
	private String hqMusicUrl;
	/**
	 * 缩略图的媒体id，通过上传多媒体文件，得到的id
	 */
	@JSONField(name = "thumb_media_id")
	@XStreamAlias("ThumbMediaId")
	private String thumbMediaId;

	public Music(String thumbMediaId) {
		this(null, null, null, null, thumbMediaId);
	}

	public Music(String title, String desc, String musicUrl, String hqMusicUrl,
			String thumbMediaId) {
		this.title = title;
		this.desc = desc;
		this.musicUrl = musicUrl;
		this.hqMusicUrl = hqMusicUrl;
		this.thumbMediaId = thumbMediaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public String getHqMusicUrl() {
		return hqMusicUrl;
	}

	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		return "Music [title=" + title + ", desc=" + desc + ", musicUrl="
				+ musicUrl + ", hqMusicUrl=" + hqMusicUrl + ", thumbMediaId="
				+ thumbMediaId + "]";
	}
}
