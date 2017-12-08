package com.starland.xyqp.niuniujb.s2c;

import java.util.List;

public class S2CSettleInfo {

	private List<SettleInfo> settleInfos;
	
	public List<SettleInfo> getSettleInfos() {
		return settleInfos;
	}

	public void setSettleInfos(List<SettleInfo> settleInfos) {
		this.settleInfos = settleInfos;
	}

	public static class SettleInfo{
		
		private String name;
		
		private int type;
		
		private boolean win;
		
		private int gold;
		
		private int changGold;
		
		private boolean banker;
		
		private int position;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public boolean isWin() {
			return win;
		}

		public void setWin(boolean win) {
			this.win = win;
		}

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
		}

		public int getChangGold() {
			return changGold;
		}

		public void setChangGold(int changGold) {
			this.changGold = changGold;
		}

		public boolean isBanker() {
			return banker;
		}

		public void setBanker(boolean banker) {
			this.banker = banker;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}
}
