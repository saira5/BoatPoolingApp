package com.example.boatpoolingapp;

public class Urls {
 //public static String serverAddress = "http://4ee2ed9dec9b.ngrok.io/users";
//
// public static String serverAddress = "http://144a7fd16d4e.ngrok.io/users";
//   public static String serverChatAddress = "http://144a7fd16d4e.ngrok.io";

 public static String serverAddress = "http://192.168.10.6:3000/users";
public static String serverChatAddress = "http://192.168.10.6:3000";
    public static String login=serverAddress+"/login";
    public static String signup=serverAddress+"/signup";
    public static String forgot_password_seller=serverAddress+"/forgetPassword";
    public static String verify_pass=serverAddress+"/resetForgotPassword";
    public static String verifyEmail=serverAddress+"/verifyEmail";
    public static String getProfile=serverAddress+"/getProfile";
    public static String offerRide=serverAddress+"/offer-a-ride";
    public static String bookRide=serverAddress+"/book-a-ride";
    public static String findRide=serverAddress+"/find-rides";
    public static String updatePendingRide=serverAddress+"/update-pending-reservation";
    public static String findPendingRides=serverAddress+"/find-pending-rides";
    public static String endRide=serverAddress+"/end-ride";
    public static String findRiders=serverAddress+"/find-specific-ride";
    public static String getChat=serverChatAddress+"/getchat";
    public static String leftRatings=serverAddress+"/leftRatings";
    public static String recievedRatings=serverAddress+"/recieveRatings";
    public static String findNotification=serverAddress+"/findNotifications";
    public static String checkReview=serverAddress+"/checkReview";
    public static String addReview=serverAddress+"/add_review";
    public static String getAccountDetail=serverAddress+"/addAcountDetails";




}
