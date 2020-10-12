package com.example.mvvmapijava.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Recipe implements Parcelable {

	@SerializedName("social_rank")
	private double socialRank;

	@SerializedName("recipe_id")
	private String recipeId;

	@SerializedName("publisher_url")
	private String publisherUrl;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("ingredients")
	private List<String> ingredients;

	@SerializedName("publisher")
	private String publisher;

	@SerializedName("_id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("source_url")
	private String sourceUrl;

	public Recipe(double socialRank, String recipeId, String publisherUrl, String imageUrl, List<String> ingredients, String publisher, String id, String title, String sourceUrl) {
		this.socialRank = socialRank;
		this.recipeId = recipeId;
		this.publisherUrl = publisherUrl;
		this.imageUrl = imageUrl;
		this.ingredients = ingredients;
		this.publisher = publisher;
		this.id = id;
		this.title = title;
		this.sourceUrl = sourceUrl;
	}

	public Recipe() {
	}

	protected Recipe(Parcel in) {
		socialRank = in.readDouble();
		recipeId = in.readString();
		publisherUrl = in.readString();
		imageUrl = in.readString();
		ingredients = in.createStringArrayList();
		publisher = in.readString();
		id = in.readString();
		title = in.readString();
		sourceUrl = in.readString();
	}

	public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
		@Override
		public Recipe createFromParcel(Parcel in) {
			return new Recipe(in);
		}

		@Override
		public Recipe[] newArray(int size) {
			return new Recipe[size];
		}
	};

	public double getSocialRank() {
		return socialRank;
	}

	public void setSocialRank(double socialRank) {
		this.socialRank = socialRank;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public String getPublisherUrl() {
		return publisherUrl;
	}

	public void setPublisherUrl(String publisherUrl) {
		this.publisherUrl = publisherUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	@Override
	public String toString() {
		return "Recipe{" +
				"socialRank=" + socialRank +
				", recipeId='" + recipeId + '\'' +
				", publisherUrl='" + publisherUrl + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", ingredients=" + ingredients +
				", publisher='" + publisher + '\'' +
				", id='" + id + '\'' +
				", title='" + title + '\'' +
				", sourceUrl='" + sourceUrl + '\'' +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(socialRank);
		dest.writeString(recipeId);
		dest.writeString(publisherUrl);
		dest.writeString(imageUrl);
		dest.writeStringList(ingredients);
		dest.writeString(publisher);
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(sourceUrl);
	}
}