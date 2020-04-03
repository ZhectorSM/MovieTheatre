package com.javafever.ticket;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.javafever.auditorium.Auditorium;
import com.javafever.auditorium.AuditoriumAction;
import com.javafever.movie.Movie;
import com.javafever.movie.MovieAction;

public class TicketController {

	private static final int DEFAULT_CUSTOMER = 0;

	@Deprecated
	public void lisTickets() {

		TicketAction tkAction = new TicketAction();
		List<Ticket> lstTicktes = tkAction.read();
		System.out.println("*My Tickets*");
		for (Ticket tk : lstTicktes) {
			System.out.println(tk.getIdTicket());
		}

	}

	public void addTicket(Ticket myTicket) {

		TicketAction tkAction = new TicketAction();// Creating actions obj
		boolean success = tkAction.create(myTicket);// Executing operation

		if (success) {// If the operation was succesful
			System.out.println("Ticket inserted.");
		} else {
			System.out.println("Ticket insertion failed.");
		}

	}

	public void buyTicket() {

		Ticket myTicket = new Ticket();
		myTicket.setMovie(chooseMovie(myTicket));
		myTicket.setMovieFunction(chooseSchedule(myTicket));
		myTicket.setSeat(chooseSeat(myTicket));
		myTicket.setIdCustomer(DEFAULT_CUSTOMER);

		TicketAction tkAction = new TicketAction();
		boolean success = tkAction.create(myTicket);

		if (success) {

			showTicket(myTicket);
			System.out.println("Do you want to buy another Ticket ?  1 = Yes 2 = No");
			Scanner input = new Scanner(System.in);
			int userAnswer = input.nextInt();
			input.nextLine();

			if (userAnswer == 1) {
				buyTicket();
			} else {
				System.out.println("Thank you for your preference.");
				System.exit(0);
			}

		} else {
			System.out.println("Ticket insertion failed");
		}

	}

	public Movie chooseMovie(Ticket myTicket) {

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
		Movie choosenMovie = null;
		boolean movieExist = false;
		for (Movie movie : lstMovies) {
			if (userIdMovie == movie.getIdMovie()) {
				movieExist = true;
				choosenMovie = movie;
				break;
			}
		}
		// Set movie
		if (!movieExist) {
			System.out.println("Movie does not exist");
			choosenMovie = chooseMovie(myTicket);
		}

		return choosenMovie;
	}

	public MovieFunction chooseSchedule(Ticket myTicket) {

		Scanner input = new Scanner(System.in);

		// Getting available schedule
		TicketAction tkAction = new TicketAction();
		List<MovieFunction> lstFunctions = tkAction.getMovieFuntions(myTicket.getMovie().getIdMovie());
		System.out.println("- Functions -  ");

		System.out.println("        SHOWTIME                VIP     PRICE   LOC ADDRES  ");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
		for (MovieFunction fun : lstFunctions) {
			System.out.println(fun.getIdSchedule() + "\t" + fun.getShowtime().format(formatter) + "\t"
					+ (fun.isVip() ? "Yes" : "No") + "\t" + fun.getPrice() + "\t" + fun.getAddress());
		}

		System.out.println("Choose the Date (Type the Id):");
		int userIdSchedule = input.nextInt();
		input.nextLine();

		// verifing the id of the schedule exist
		MovieFunction choosenFun = null;
		boolean scheduleExist = false;
		for (MovieFunction sch : lstFunctions) {
			if (userIdSchedule == sch.getIdSchedule()) {
				scheduleExist = true;
				choosenFun = sch;
				break;
			}
		}

		// Set schedule
		if (!scheduleExist) {
			System.out.println("Function does not exist");
			choosenFun = chooseSchedule(myTicket);
		}

		return choosenFun;

	}

	public int chooseSeat(Ticket myTicket) {

		Scanner input = new Scanner(System.in);

		int idAuditorium = 0;
		TicketAction tkAction = new TicketAction();
		List<MovieFunction> lstFunctions = tkAction.getMovieFuntions(myTicket.getMovie().getIdMovie());
		for (MovieFunction fun : lstFunctions) {
			if (myTicket.getMovieFunction().getIdSchedule() == fun.getIdSchedule()) {
				idAuditorium = fun.getIdAuditorium();
			}
		}

		AuditoriumAction audActAction = new AuditoriumAction();
		List<Auditorium> lstAuditoriums = audActAction.read();
		int totalSeats = 0;
		for (Auditorium aud : lstAuditoriums) {
			if (idAuditorium == aud.getIdAuditorium()) {
				totalSeats = aud.getSeatTotal();
			}
		}

		// Asking for a valid seat
		int seatNum = 0;
		while (seatNum <= 0 || seatNum > totalSeats) {

			System.out.println("Choose a seat...");
			seatNum = input.nextInt();
			input.nextLine();

		}

		// Get tickest already sold
		List<Ticket> lstTicketsSold = tkAction.readBySchedule(myTicket.getMovieFunction().getIdSchedule());

		boolean seatAvailable = true;
		for (Ticket tk : lstTicketsSold) {
			if (seatNum == tk.getSeat()) {
				seatAvailable = false;
			}
		}

		if (!seatAvailable) {
			System.out.println("Seat no available");
			seatNum = chooseSeat(myTicket);
		}

		return seatNum;

	}

	public void showTicket(Ticket myTicket) {

		System.out.println("..........Ticket...............");
		System.out.println("Movie:" + myTicket.getMovie().getMovieName());
		System.out.println("Duration: " + myTicket.getMovie().getRuntimeMinutes() + " minutes");
		System.out.println("Time: " + myTicket.getMovieFunction().getShowtime());
		System.out.println("Price: " + myTicket.getMovieFunction().getPrice());
		System.out.println("Seat" + myTicket.getSeat());

		System.out.println(".................................");

	}

}
