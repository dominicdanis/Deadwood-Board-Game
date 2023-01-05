/*
Payment Manager

This class contains methods to handle payments in Deadwood. It has one to pay players upon the ending of a scene
and one to validate a players purchase of rank

Dominic Danis Created 11/2/2021
Last Edit 11/30/2021
*/

import java.util.ArrayList;

public class PaymentManager {
    public void payPlayers(Location set, ArrayList<Integer> payments){
        ArrayList<Player> onCard = new ArrayList<Player>();
        ArrayList<Player> offCard = new ArrayList<Player>();
        for(int i =0; i<set.getOccupants().size(); i++){
            if(set.getOccupants().get(i).getRole()!=null){
                if(set.getOccupants().get(i).getRole().getCardStatus()){        //get those off card
                    onCard.add(set.getOccupants().get(i));
                }
                else{                                                           //get those off card
                    offCard.add(set.getOccupants().get(i)); 
                }
            }
        }
        GameController controller = GameController.getController();
        if(onCard.size()!=0){
            for(int i=0; i<payments.size();i++){                                 //pay those on card
                controller.addPayment(onCard.get(0), payments.get(i));
                onCard.get(0).addMoney(payments.get(i));
                onCard.add(onCard.get(0));
                onCard.remove(0);
            }
            for(int i=0; i<offCard.size();i++){                                  //pay those off card
                controller.addPayment(offCard.get(i), offCard.get(i).getRole().getRank());
                offCard.get(i).addMoney(offCard.get(i).getRole().getRank());
            }
        }
        else{
            controller.noBonus();     //No one is paid
        }
    }
    public boolean validatePurchase(Player buyer, ArrayList<String> choices, ArrayList<Upgrade> upgrades){
        if(buyer.getRank()>=Integer.parseInt(choices.get(0))){
            return false;
        }
        Upgrade chosen = new Upgrade();
        for(int i=0;i<upgrades.size();i++){                                     //find the right upgrade
            if(upgrades.get(i).getCurrency().equals(choices.get(1)) && upgrades.get(i).getRank()==Integer.parseInt(choices.get(0))){
                chosen = upgrades.get(i);
            }
        }
        if(chosen.getCurrency().equals("dollar")){                              //validate player can buy it
            if(buyer.getMoney()<chosen.getCurAmount()){
                return false;
            }
        }
        else{
            if(buyer.getCredits()<chosen.getCurAmount()){
                return false;
            }
        }
        return true;
    }
}
