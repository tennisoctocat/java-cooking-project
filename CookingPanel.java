
/* Cynthia Hom
 * 4/26/17
 * CookingPanel.java
 *
 * CookingPanel file holds the CookingPanel class.  In the CookingPanel,
 * the user learns how to make the food. Instances of either
 * the KitchenSection class's child classes are added as cards
 * to the CookingPanel class depending on the type of food cooked. 
 *
 * Java concepts used:
 *      1. Mouse methods to allow the user to click on or drag items
 *      2. arrays to hold the cooking and direction sequences for the foods
 *      3. if-else conditionals
 * 		4. objects of panels passed in as parameters so that the card
 * 			layouts in these panels can be changed from the panels added
 * 			to them
 *
 * Testing:
 *      no user interaction in this panel
 */

import javax.swing.JPanel;
import java.awt.CardLayout;
///import java.awt.Graphics;
///import java.awt.GridLayout;

public class CookingPanel extends JPanel  //where user virtually 'cooks' the food
{
    /// private String foodName;   //buttonPressed holds the text of the food to be cooked
    protected CardLayout cards; //protected so KitchenSection can change the cards in this panel
    protected int foodStepNum; //number of KitchenSections completed
    //make String to determine what type of food is cooked
    protected String foodType;  //used for quiz panel later, and for pot vs. pan in Stove class
    // protected String potPan;
    protected String[] foodCards;// array for  sequence of which section of kitchen to show

    public CookingPanel(String foodNameIn, MainCookPanel mcpIn)  //set up panel
    {
        //initialize foodName to parameter passed in-the button that is pressed
        /// foodName = foodNameIn;

        foodStepNum = 0;    //no steps completed yet
        //   potPan = "pot";	//initialize defaults
        foodType = null;
        foodCards = null;
        //intialize type of food and order of the Kitchen section to be
        // shown depending on food, as well as which panel to be made
        if (foodNameIn.equals("Broccoli") ||
                foodNameIn.equals("Zucchini") || foodNameIn.equals("Pasta"))    //pot foods
        {
            foodType = "potFood";

            //if the food is pasta, don't cut the food up before boiling it
            //otherwise, do cut op the food first
            if (foodNameIn.equals("Pasta"))
                foodCards = new String[]{"fillPot", "stove"};
            else
                foodCards = new String[]{"sink", "knife", "fillPot", "stove"};  //pot foods that are cut first
        }
         else if (foodNameIn.equals("Salmon") ||
                foodNameIn.equals("Mushrooms") || foodNameIn.equals("Green beans"))   //pan foods
        {
            foodType = "panFood";
            foodCards = new String[]{"sink", "knife", "stove"};
        } 
        else if (foodNameIn.equals("Chicken") || foodNameIn.equals("Potatoes")
			|| foodNameIn.equals("Sweet potatoes") )//oven foods
        {
            foodType = "ovenFood";
            foodCards = new String[]{"sink", "oven"};
        }
         else 
        {
            foodType = "knifeFood";
            foodCards = new String[]{"sink", "knife"};
        }

        //set up panel
        //cardLayout
        cards = new CardLayout();
        setLayout(cards);

        //make and add instances of Knife, Stove, Oven, and Sink- child classes of KitchenSection
        //depending on type of food
        if (!foodNameIn.equals("Pasta"))        //always need the sink unless food is pasta
        {
            Sink sinkPanel = new Sink(this, mcpIn, foodNameIn);
            this.add(sinkPanel, "sink");
        }
        if (foodType.equals("panFood") || foodType.equals("potFood"))   //make stove class if a pot or pan is being used
        {
            Stove stovePanel = new Stove(this, mcpIn, foodNameIn);
            this.add(stovePanel, "stove");
        }
        if (foodType.equals("potFood")) {
            Sink fillPotPanel = new Sink(this, mcpIn, "pot.jpg");    //this is for step where user fills pot with water to be boiled
            this.add(fillPotPanel, "fillPot");
        }
        if (!(foodNameIn.equals("Pasta") || foodNameIn.equals("Chicken") || foodNameIn.equals("Potatoes"))) {
            Knife knifePanel = new Knife(this, mcpIn, foodNameIn);
            this.add(knifePanel, "knife");
        }
        if (foodType.equals("ovenFood")) {
            Oven ovenPanel = new Oven(this, mcpIn, foodNameIn);
            this.add(ovenPanel, "oven");
        }

        //show the card corresponding to the first step for the food
        cards.show(this, foodCards[0]);
    }
}