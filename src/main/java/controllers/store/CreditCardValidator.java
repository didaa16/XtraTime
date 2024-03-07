package controllers.store;

public class CreditCardValidator {

    public static boolean isValid(String cardNumber) {
        // Supprimer les espaces et les tirets du numéro de carte
        cardNumber = cardNumber.replaceAll("[ -]", "");

        // Vérifier que le numéro de carte a une longueur valide
        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }

        // Vérifier que le numéro de carte ne contient que des chiffres
        if (!cardNumber.matches("[0-9]+")) {
            return false;
        }

        // Calculer la somme des chiffres de contrôle
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        // Vérifier que la somme est un multiple de 10
        return sum % 10 == 0;
    }
}

