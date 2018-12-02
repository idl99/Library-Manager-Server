package controllers;

import play.mvc.Result;

public interface LibraryManager {

    Result borrowItem(); // method to handle Item borrowal

    Result returnItem(); // method to handle Item return
//
//    Result reserveItem(String isbn, Reader readerId); // method to reserve Item
//
    Result report(String generatedOn); // method to get summary of all overdue Item and fees

}