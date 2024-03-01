package tests;

import entities.Produit;

import java.sql.SQLException;
import java.util.List;

public interface IListener {
    public void onClickListener(Produit produit);


}
