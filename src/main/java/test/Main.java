package test;

import services.Abonnement.ServiceAbonnement;
import services.Abonnement.ServicePack;
import utils.MyDatabase;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        MyDatabase db = new MyDatabase();
        Date dateAbonnement = new Date(System.currentTimeMillis());
        ServiceAbonnement SA = new ServiceAbonnement();
        ServicePack SP = new ServicePack();


    }
}
