package wishlist.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wishlist.beans.*;

@Controller
public class WebController {

	private List<VideoGame> gameQueue = new LinkedList<>();
	private List<VideoGame> selectedGames = new ArrayList<>();
    private List<Wishlist> wishlists = new ArrayList<>();

    /**
     * Default No Arg Constructor 
     * Only here to make the unit tests run 
     * otherwise the tests will always fail as the controller is null
     */
    public WebController() {
        super();
    }

    /**
     * populates the gameQueue with values from the unit tests
     * @param games
     */
    public void createGameQueue(List<VideoGame> games) {
        gameQueue.addAll(games);
    }

    /**
     * populates the wishlists list with values from the unit tests
     * @param wishlists
     */
    public void createWishlists(List<Wishlist> wishlists) {
        wishlists.addAll(wishlists);

    }
    
    /**
     * directs to the specified html form page
     * @return html redirect link to the add game page
     */
	@GetMapping("/addGame")
    public String showAddGameForm() {
        return "addVideoGame";
    }

    /**
     * adds the specified video game to the video game queue
     * @param name
     * @param store
     * @param price
     * @return html redirect to the viewGames html template
     */
    @PostMapping("/addGame")
    public String addGame(@RequestParam String name, @RequestParam String store, @RequestParam double price) {
        VideoGame newGame = new VideoGame(name, store, price);
        gameQueue.add(newGame);
        return "redirect:/viewGames";
    }

    /**
     * sends the video game queue to the specified html template
     * @param model
     * @return html redirect to the showVideoGames html template
     */
    @GetMapping("/viewGames")
    public String viewGames(Model model) {
        model.addAttribute("videoGames", gameQueue);
        return "showVideoGames.html";
    }

    /**
     * creates a new wishlist with the specified username and video games list
     * then adds the new wishlist to the wishlist List
     * @param username
     * @param selectedGames
     * @param model
     * @return html redirect to the viewGames html template
     */
	@PostMapping("/addToWishlist")
	public String createWishlist(@RequestParam String username, @RequestParam List<String> selectedGames, Model model) {
        List<VideoGame> selectedGameList = getSelectedGamesByNames(selectedGames);

        if (selectedGameList.isEmpty()) {
            model.addAttribute("errorMessage", "Please select at least one game for the wishlist.");
            return "viewGames";
        }

        Wishlist newWishlist = new Wishlist(username, selectedGameList);
        wishlists.add(newWishlist);

        // Clear selectedGames for the next wishlist
        this.selectedGames.clear();

        return "redirect:/viewGames";
    }

    /**
     * makes a list of video games selected from the video game queue based on the names of the games
     * @param selectedGameNames
     * @return th list of video games selected
     */
    public List<VideoGame> getSelectedGamesByNames(List<String> selectedGameNames) {
        List<VideoGame> selectedGameList = new LinkedList<VideoGame>();
        selectedGameList.addAll(getSelectedGamesByNames(selectedGameNames, gameQueue));
        return selectedGameList;
    }

    /**
     * sends the selected game to the edit game form html page
     * @param name
     * @param model
     * @return html redirect to the edit game html form template
     */
	@GetMapping("/editGame")
    public String showEditGameForm(@RequestParam String name, Model model) {
        VideoGame gameToEdit = findGameByName(name);

        if (gameToEdit == null) {
            model.addAttribute("errorMessage", "Game not found for editing.");
            return "viewGames";
        }

        model.addAttribute("gameToEdit", gameToEdit);
        return "editGame";
    }

    /**
     * edits the selected game with the new values from the edit game form
     * @param editedGame
     * @return html redirect to the view games html template
     */
    @PostMapping("/editGame")
    public String editGame(@ModelAttribute("gameToEdit") VideoGame editedGame) {
        VideoGame originalGame = findGameByName(editedGame.getName());

        if (originalGame != null) {
            // Update the original game with the edited details
            originalGame.setName(editedGame.getName());
            originalGame.setStore(editedGame.getStore());
            originalGame.setPrice(editedGame.getPrice());
        }

        // Redirect to the viewGames page after editing
        return "redirect:/viewGames";
    }

    /**
     * adds the wishlist list to the model that is passed to the html template
     * @param model
     * @return html redirect to the view wishlists html template
     */
    @GetMapping("/viewWishlists")
    public String viewWishlists(Model model) {
        model.addAttribute("wishlists", wishlists);
        return "viewWishlists.html";
    }

    /**
     * gets the selected wishlist from the wishlist list based on the username
     * adds the selected wishlist to the model that is passed to the html template
     * @param model
     * @param username
     * @return html redirect to the view wishlists detail html template
     */
    @GetMapping("/wishlistDetail/{username}")
    public String showWishlistDetail(Model model,@PathVariable String username) {
        Wishlist wishlist = getWishlistByName(username);
        model.addAttribute("wishlist", wishlist);
        return "wishlistDetail";
    }

    /**
     * gets the selected wishlist based on the selected username
     * adds the selected wishlist to the model that is passed to the html template
     * @param model
     * @param username
     * @return html redirect to the edit wishlist form template
     */
    @GetMapping("/wishlist.edit/{username}")
    public String showEditWishlistForm(Model model, @PathVariable String username) {
        Wishlist wishlist = getWishlistByName(username);
        model.addAttribute("wishlist", wishlist);
        return "editWishlist";
    }

    /**
     * removes the selected wishlist from the wishlist list and creates a new wishlist based on the new values 
     * adds the wishlist to the model that is passed to the html template the wishlist list
     * @param updatedWishlist
     * @param username
     * @return html redirect to the view wishlists template
     */
    @GetMapping("/editWishlist/{username}")
    public String editWishlist(Wishlist updatedWishlist, @PathVariable String username) {
        Wishlist existingWishlist = getWishlistByName(username);

        for(int i = 0; i < wishlists.size(); i++) {
            if(wishlists.get(i).getUsername().equalsIgnoreCase(username)) {
                wishlists.remove(i);
                break;
            }
        }

        //update the existing wishlist with the edited values
        existingWishlist.setUsername(updatedWishlist.getUsername());
        existingWishlist.setSelectedGames(updatedWishlist.getSelectedGames());
        wishlists.add(existingWishlist);
        return "redirect:/viewWishlists";
    }

    /**
     * deletes the specified wishlist from the wishlist list
     * @param username
     * @return html redirect to the view wishlists template
     */
    @GetMapping("/deleteWishlist/{username}")
    public String deleteWishlist(@PathVariable String username) {
        Wishlist wishlistToDelete = getWishlistByName(username);
        wishlists.remove(wishlistToDelete); 
        return "redirect:/viewWishlists";
    }

    /**
     * binary sort for the wishlists list to sort it in ascending order from lowest price to highest price
     * @return html redirect link to the viewSortedGames html template
     */
    @GetMapping("/sortGamesByPrice")
    public String sortByPrice(Model model) {
        List<VideoGame> list = new ArrayList<VideoGame>();

        for(int i = 0; i < gameQueue.size(); i++) {
            list.add(gameQueue.get(i));
        }

        int n = list.size();

        for (int i = 1; i < n; ++i) {
            VideoGame key = list.get(i);
            int j = i - 1;

            // Move elements of list[0..i-1] that are greater than key.price to one position ahead
            // of their current position
            while (j >= 0 && list.get(j).getPrice() > key.getPrice()) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }

        for(int i = 0; i < list.size(); i++) {
            gameQueue.set(i, list.get(i));
        }   

        model.addAttribute("videoGames", gameQueue);

        return "viewSortedGames.html";
    }

    /**
     * takes a name as input asn loops through the game queue until a 
     * match is found adn then returns the matching VideoGame
     * @param name
     * @return
     */
    public VideoGame findGameByName(String name) {
        VideoGame game = new VideoGame();
        for(int i = 0; i < gameQueue.size(); i++) {
            if(gameQueue.get(i).getName().equalsIgnoreCase(name)) {
                game = gameQueue.get(i);
                break;
            }
        }

        return game;
    }

    /**
     * creates a new list of video games that contains video games from teh game queue that have matching names
     * to the names in the selected video games names list
     * @param selectedGamesNames
     * @param games
     * @return a list of VideoGame objects containing the selected games based on names
     */
    public List<VideoGame> getSelectedGamesByNames(List<String> selectedGamesNames, List<VideoGame> games) {
        List<VideoGame> selectedGameList = new ArrayList<>();
        for(VideoGame game1 : gameQueue) {
            for(VideoGame game2 : games) {
                if(game1.getName().equalsIgnoreCase(game2.getName())) {
                    selectedGameList.add(game1);
                }
            }
        }
        return selectedGameList;
    }

    /**
     * finds the selected wishlist form the list of wishlists based on the username field
     * @param name
     * @return the selected wishlist object based on its username field
     */
    public Wishlist getWishlistByName(String name) {
        Wishlist wishlist = new Wishlist();
        for(int i = 0; i < wishlists.size(); i++) {
            if(wishlists.get(i).getUsername().equalsIgnoreCase(name)) {
                wishlist = wishlists.get(i);
                break;
            }
        }
        return wishlist;
    }

}
