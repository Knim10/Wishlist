package wishlist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import wishlist.beans.VideoGame;
import wishlist.beans.Wishlist;
import wishlist.controller.WebController;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

@SpringBootTest
class WishlistApplicationTests {

	private List<VideoGame> games = new ArrayList<VideoGame>();
	private List<Wishlist> wishlists = new ArrayList<Wishlist>();
	private VideoGame game1 = new VideoGame("Witcher 3", "Steam", 59.99);
	private VideoGame game2 = new VideoGame("Satisfactory", "Epic", 34.99);
	private VideoGame game3 = new VideoGame("Battle Bit", "Steam", 14.99);
	private WebController controller = new WebController();

	@Test
	void contextLoads() {
	}

	@Test
	public void testFindGameByName() {
		games.add(game1);
		games.add(game2);
		controller.createGameQueue(games);

		String nameToSearFor = "Witcher 3";
		String selectedGameName = controller.findGameByName(nameToSearFor).getName();
		String expectedString = "Witcher 3";

		assertEquals(expectedString, selectedGameName);

	}

	@Test
	public void testGetSelectedGamesByNames() {
		List<String> names = new ArrayList<String>();
		names.add("Witcher 3");
		names.add("Satisfactory");
		games.add(game1);
		games.add(game2);
		games.add(game3);
		controller.createGameQueue(games);
		List<VideoGame> gamesSelected = new ArrayList<VideoGame>();
		gamesSelected.addAll(controller.getSelectedGamesByNames(names));
		
		String actual = gamesSelected.get(1).getName();
		String expected = "Satisfactory";
		assertEquals(expected, actual);
		expected = "Witcher 3";
		actual = gamesSelected.get(0).getName();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetWishlistByName() {
		games.add(game1);
		games.add(game2);
		games.add(game3);
		controller.createGameQueue(games);
		Wishlist wishlist1 = new Wishlist("Knim1", games);
		Wishlist wishlist2 = new Wishlist("root", games);
		wishlists.add(wishlist1);
		wishlists.add(wishlist2);
		controller.createWishlists(wishlists);

		String testActual = controller.getWishlistByName("root").getUsername();

		//System.out.println(testActual);

		String actual  = controller.getWishlistByName("root").getUsername();
		String expected = "root";

		assertEquals(expected, actual);


	}

}
