package wishlist.beans;

import java.util.List;

public class Wishlist {
	
	private String username;
	private List<VideoGame> selectedGames;

	public Wishlist() {
		super();
	}

	public Wishlist(String userName, List<VideoGame> selectedGames) {
		super();
		setUsername(userName);
		setSelectedGames(selectedGames);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSelectedGames(List<VideoGame> selectedGames) {
		this.selectedGames = selectedGames;
	}

	public String getUsername() {
		return username;
	}

	public List<VideoGame> getSelectedGames() {
		return selectedGames;
	}

}
