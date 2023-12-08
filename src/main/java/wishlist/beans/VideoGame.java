package wishlist.beans;

public class VideoGame {
	
	private String name;
	private String store;
	private double price;

	public VideoGame() {
		super();
	}

	public VideoGame(String name, String store, double price) {
		super();
		this.name = name;
		this.store = store;
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public String getStore() {
		return store;
	}

	public double getPrice() {
		return price;
	}
}
