package test;

import entities.Abonnement;
import entities.Pack;
import services.ServiceAbonnement;
import services.ServicePack;
import utils.MyDatabase;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyDatabase db = new MyDatabase();
        Date dateAbonnement = new Date(System.currentTimeMillis());
        ServiceAbonnement SA = new ServiceAbonnement();
        ServicePack SP = new ServicePack();


    }
}
