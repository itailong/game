package com.starland.xyqp.pdk.s2c;

import java.util.List;

public class S2CApplyDissolveRoom {

	/**
	 * 申请者姓名
	 */
	private String applyName;
	
	private List<UserInfo> userInfos;
	
	/**
	 * 解散剩余时间，单位秒
	 */
	private long surplusTime;
	
	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public List<UserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	public long getSurplusTime() {
		return surplusTime;
	}

	public void setSurplusTime(long surplusTime) {
		this.surplusTime = surplusTime;
	}

	public static class UserInfo {
		
		/**
		 * 位置
		 */
		private int position;
		
		/**
		 * 是否同意解散房间，0未投票，1同意，2不同意
		 */
		private int agreeDissolve;
		
		/**
		 * 用户名称
		 */
		private String userName;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public int getAgreeDissolve() {
			return agreeDissolve;
		}

		public void setAgreeDissolve(int agreeDissolve) {
			this.agreeDissolve = agreeDissolve;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
		
	}
	
}
