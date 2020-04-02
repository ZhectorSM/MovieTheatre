package com.javafever.main;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.javafever.auditorium.AuditoriumController;
import com.javafever.category.CategoryController;
import com.javafever.customer.CustomerController;
import com.javafever.location.LocationController;
import com.javafever.loyalprograms.LoyalProgramController;
import com.javafever.movie.Movie;
import com.javafever.movie.MovieAction;
import com.javafever.movie.MovieController;
import com.javafever.theatreschedule.ScheduleController;
import com.javafever.ticket.MovieFunction;
import com.javafever.ticket.Ticket;
import com.javafever.ticket.TicketAction;
import com.javafever.user.User;
import com.javafever.user.UserAction;

public class MovieTheatreMain {

	public static void main(String[] args) {

		showHeader();
		showMainMenu();

	}

	public static void showMainMenu() {
		System.out.println("1 Buy a Ticket as a Guest");
		System.out.println("2 Log In");
		System.out.println("0 Exit");

		System.out.println("Choose an option...");
		Scanner input = new Scanner(System.in);
		int userChoice = input.nextInt();
		input.nextLine();
		switch (userChoice) {

		case 1:
			buyTicket();
			break;
		case 2:
			login();
			break;
		case 0:
			System.out.println("See you later!");
			System.exit(0);
			break;
		default:
			showMainMenu();
			break;
		}

		input.close();
	}

	public static void login() {

		Scanner input = new Scanner(System.in);
		System.out.println("Type your user name ");
		User user = new User();
		user.setName(input.nextLine().trim());
		System.out.println("Type your password");
		user.setPwd(input.nextLine().trim());

		// Get users of the database
		UserAction catAction = new UserAction();
		List<User> lstUser = catAction.read();

		// If the user exist on the list
		if (lstUser.contains(user)) {

			String userRole = "";
			for (User usr : lstUser) {// Gettirng the rol of the user

				if (usr.equals(user)) {
					userRole = usr.getRole();
				}
			}

			// Show custom menu
			if (userRole.contentEquals("admin")) {
				showAdminMenu();
			} else {
				showUserMenu();
			}

		} else {
			System.out.println("Verify your data.");
		}

		input.close();
	}

	public static void showAdminMenu() {
		System.out.println(">> Admin Menu <<");
		System.out.println("1 Category");
		System.out.println("2 Movie");
		System.out.println("3 Location");
		System.out.println("4 Auditorium");
		System.out.println("5 Customer");
		System.out.println("6 Schedule");
		System.out.println("7 Loyalty program");
		System.out.println("0 Go to main menu");
		System.out.println("Choose an option...");

		Scanner input = new Scanner(System.in);
		int adminChoice = input.nextInt();
		input.nextLine();
		switch (adminChoice) {
		case 1:
			CategoryController catControl = new CategoryController();
			catControl.categoryMenu();
			break;
		case 2:
			MovieController movieControl = new MovieController();
			movieControl.movieMenu();
			break;
		case 3:
			LocationController locContol = new LocationController();
			locContol.locationMenu();
			break;

		case 4:
			AuditoriumController audContol = new AuditoriumController();
			audContol.auditoriumMenu();
			break;
		case 5:
			CustomerController cusControl = new CustomerController();
			cusControl.customerMenu();
			break;

		case 6:
			ScheduleController schControl = new ScheduleController();
			schControl.scheduleMenu();
			break;

		case 7:
			LoyalProgramController loyalControl = new LoyalProgramController();
			loyalControl.LoyalProgramMenu();
			break;
		case 0:
			showMainMenu();
			break;
		default:
			showAdminMenu();
			break;
		}

		input.close();

	}

	public static void showUserMenu() {

		System.out.println(">> User Menu <<");
		System.out.println("1 List my Tickets");
		System.out.println("2 Buy Ticket");
		System.out.println("3 Update Ticket");
		System.out.println("4 Check my Tickets");
		System.out.println("0 Go to main menu");
		System.out.println("Choose an option...");

		Scanner input = new Scanner(System.in);
		int userChoice = input.nextInt();
		input.nextLine();
		switch (userChoice) {
		case 1:
			break;
		case 2:
			buyTicket();
			break;
		case 3:
			showMainMenu();
			break;

		default:
			showUserMenu();
			break;
		}

		input.close();

	}

	public static void buyTicket() {
		Ticket myTicket = new Ticket();

		int userIdMovie = chooseMovie();
		myTicket.setIdMovie(userIdMovie);
		int userIdChedule = chooseSchedule(myTicket);
		myTicket.setIdSchedule(userIdChedule);

		System.out.println(myTicket);

	}

	public static int chooseMovie() {

		System.out.println("- Movies -");
		MovieAction movAct = new MovieAction();
		List<Movie> lstMovies = movAct.read();
		for (Movie movie : lstMovies) {
			System.out.println(movie.getIdMovie() + " " + movie.getMovieName());
		}

		Scanner input = new Scanner(System.in);
		System.out.println("Choose a Movie (Type the Id):");
		int userIdMovie = input.nextInt();
		input.nextLine();

		// verifing the id of the movie exist
		boolean movieExist = false;
		for (Movie movie : lstMovies) {
			if (userIdMovie == movie.getIdMovie()) {
				movieExist = true;
				break;
			}
		}
		// Set movie
		if (!movieExist) {
			buyTicket();
		}

		return userIdMovie;

	}

	public static int chooseSchedule(Ticket myTicket) {

		Scanner input = new Scanner(System.in);

		// Getting available schedule
		TicketAction tkAction = new TicketAction();
		List<MovieFunction> lstFunctions = tkAction.getMovieFuntions(myTicket.getIdMovie());
		System.out.println("- Functions -  ");

		System.out.println("            SHOWTIME            VIP     PRICE     LOC ADDRES  ");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
		for (MovieFunction fun : lstFunctions) {
			System.out.println(fun.getIdSchedule() + "\t" + fun.getShowtime().format(formatter) + "\t"
					+ (fun.isVip() ? "Yes" : "No") + "\t" + fun.getPrice() + "\t" + fun.getAddress());
		}

		System.out.println("Choose the Date (Type the Id):");
		int userIdSchedule = input.nextInt();
		input.nextLine();

		// verifing the id of the schedule exist
		boolean scheduleExist = false;
		for (MovieFunction sch : lstFunctions) {
			if (userIdSchedule == sch.getIdSchedule()) {
				scheduleExist = true;
				break;
			}
		}

		// Set schedule
		if (!scheduleExist) {
			System.out.println("Function does not exist");
			chooseSchedule(myTicket);
		}

		input.close();
		return userIdSchedule;

	}

	public static void chooseLocation(Ticket ticket) {

	}

	public static void showHeader() {
		System.out.println("*******************************");
		System.out.println("**      JAVA FEVER THEATRE   **");
		System.out.println("**           WELCOME         **");
		System.out.println("**                           **");
		System.out.println("*******************************");
	}

}
