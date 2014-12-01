package com.eric.huatianzhi.beans;

import java.io.Serializable;

import org.json.JSONObject;

public class JoinedWeddingBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int weddingID;
	private int preWeddingPhotoAlbumID;

	public JoinedWeddingBean() {
	}

	public JoinedWeddingBean(int weddingID, int preWeddingPhotoAlbumID) {
		this.weddingID = weddingID;
		this.preWeddingPhotoAlbumID = preWeddingPhotoAlbumID;
	}

	public int getWeddingID() {
		return weddingID;
	}

	public void setWeddingID(int weddingID) {
		this.weddingID = weddingID;
	}

	public int getPreWeddingPhotoAlbumID() {
		return preWeddingPhotoAlbumID;
	}

	public void setPreWeddingPhotoAlbumID(int preWeddingPhotoAlbumID) {
		this.preWeddingPhotoAlbumID = preWeddingPhotoAlbumID;
	}

	public static JoinedWeddingBean fromJson(JSONObject j) {
		if (j == null) {
			return null;
		} else {
			return new JoinedWeddingBean(j.optInt("WeddingID"),
					j.optInt("PreWeddingPhotoAlbumID"));
		}
	}
}
