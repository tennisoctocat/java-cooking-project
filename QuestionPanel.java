

/* Cynthia Hom
 * 4/24/17
 * QuestionPanel.java
 * This file holds QuestionPanel, the main panel that asks the user
 * which food they'd like to cook. QuestionPanel has a BorderLayout and holds
 *
 * A pointer object, mcp, allows the cards in mainCookPanel to be switched
 * 		from an outside class. "this" is sent as a parameter when the
 * 		outside class is created, and mcp is set to the parameter sent in.
 *
 * Testing:
 * 		Should work: Clicking a button should bring the user to the 
 * 			panel to cook the food if they have completed the prior level
 * 			If they have not completed the prior level, a JDialog should 
 * 			show up, notifying them that they must complete the lower level
 * 			first. 
 * 		Should not work: Users should not be able to jump levels, clicking 
 * 			a level they have unlocked should show the panel to cook the 
 * 			food. Clicking anywhere else should not cause anything to happen.
 */

import javax.swing.*;

import java.awt.Font; //Font

import java.awt.event.ActionListener;	//import listeners
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;  //import Layouts
import java.awt.GridLayout;

public class QuestionPanel extends JPanel
{
    private MainCookPanel mcp;
    protected String buttonPressed;
    //field vars
    public QuestionPanel (MainCookPanel mcpIn)	//constructor: set up panel
    {
        mcp = mcpIn;
        //set up panel with a Border Layout
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        //add JLabel w/ question "What food would ..." to top
        JLabel question = new JLabel("What food would you like to cook?");
        Font questionFont = new Font ("Serif", Font.BOLD, 50);
        question.setFont(questionFont);
        add(question, BorderLayout.NORTH);

        //add JScrollBar to right
        ///	JScrollBar scrollBar = new JScrollBar();

        //add instance of OptionPanel to the middle- this
        //holds buttons for each food
        OptionPanel optionPanel = new OptionPanel();
        add(optionPanel, BorderLayout.CENTER);
    }
    class OptionPanel extends JPanel	//holds buttons for each food
    {
        public OptionPanel()	//sets up panel
        {
            GridLayout gridLayout = new GridLayout(4, 1);	//1 column, 4 rows
            setLayout(gridLayout); 	//set up panel with GridLayout

            //make instances of LevelPanel- one per level
            //w/ parameters of foods to cook and level description

            LevelPanel knife = new LevelPanel("Level 1: Knife Skills",
                    "Watermelon", "Apple", "Pears");
            LevelPanel boilSteam = new LevelPanel("Level 2: Boiling and " +
                    "Steaming", "Broccoli", "Zucchini", "Pasta");
            LevelPanel pan = new LevelPanel("Level 3: Using a pan",
                    "Green beans", "Mushrooms", "Salmon");
            LevelPanel oven = new LevelPanel("Level 4: Using an oven",
                    "Chicken", "Potatoes", "Sweet potatoes");

            add(knife);		//add all to panel
            add(boilSteam);
            add(pan);
            add(oven);

        }
    }

    class LevelPanel extends JPanel //contains level header and buttons for each food in the level
    {
        //constructor- sets up panel
        public LevelPanel (String level, String button1Name,
                           String button2Name, String button3Name)
        {

           /* //set up panel with GridLayout
            GridLayout levelGrid = new GridLayout(2, 1); //2 rows, 1 column
            setLayout(levelGrid);*/

            //make and add JLabel for title
            JLabel levelTitle = new JLabel(level);
            Font levelFont = new Font("Serif", Font.PLAIN, 25);
            levelTitle.setFont(levelFont);
            add(levelTitle);
            
            

            //Make buttons for each food, setting text to appropriate field var
            JButton button1 = new JButton(button1Name);
            JButton button2 = new JButton(button2Name);

            //set font for buttons
            Font buttonFont = new Font ("Serif", Font.PLAIN, 17);
            button1.setFont(buttonFont);
            button2.setFont(buttonFont);

            //add button listener to each button
            FoodButtonListener foodButtonListener = new FoodButtonListener();
            button1.addActionListener(foodButtonListener);
            button2.addActionListener(foodButtonListener);
            
            

            //add all buttons to panel
            add(button1);
            add(button2);
            
            //only make button 3 if the level is not the oven panel
            if (!level.equals("Level 4: Using an oven"))
			{
				JButton button3 = new JButton(button3Name);
				button3.setFont(buttonFont);
				button3.addActionListener(foodButtonListener);
				add(button3);
			}
        }

        class FoodButtonListener implements ActionListener	//this class listens to the food buttons
        {
            //make correct cooking panel appear for the food pressed
            public void actionPerformed (ActionEvent evt)
            {
                //see which button is pressed by getting the command on the
                //button, store into a variable
                buttonPressed = evt.getActionCommand();

                //determine the level in which the button is in
                int level = -1;
                if (buttonPressed.equals("Watermelon") || 
					buttonPressed.equals("Apple") || buttonPressed.equals("Pears"))
                    level = 1;
                else if (buttonPressed.equals("Broccoli") || buttonPressed.equals("Zucchini")
                        || buttonPressed.equals("Pasta"))
                    level = 2;
                else if (buttonPressed.equals("Mushrooms") || buttonPressed.equals("Salmon")
                    || buttonPressed.equals("Green beans"))
                    level = 3;
                else if (buttonPressed.equals("Chicken") || buttonPressed.equals("Potatoes")
                    || buttonPressed.equals("Sweet potatoes"))
                    level = 4;

            //if the user clicks on a button in a level that they have not yet unlocked, show a dialog that
                //tells them to unlock the level first
            //otherwise... make, add, and show cooking panel card using instance of MainCookPanel
                //this is done here so that the cooking panel knows what food is being cooked
                if ( level > mcp.highLevelCompleted + 1)   //level != 4 is temporary
                    JOptionPane.showMessageDialog(mcp, "Sorry! You must complete a lower " +
                            "level first to unlock this level! ");  //show JDialog
                else
                {
                    mcp.cookPanel = new CookingPanel(buttonPressed, mcp); //user graphically makes food
                    //buttonPressed is passed in as a parameter so CookingPanel knows which food is being cooked
                    mcp.add(mcp.cookPanel, "cookingPanel");

                    mcp.quizPanel = new QuizPanel(mcp); //make and add the quiz panel- this way the buttonpressed is not null
                    mcp.add(mcp.quizPanel, "quizPanel");

                    //show the cooking panel
                    mcp.cardLayout.show(mcp, "cookingPanel");
                }



            }

            //returns buttonPressed variable so cooking panel knows which
            //food is being cooked
//          // public String getButtonPressed ()
//          {
//                return buttonPressed; 	//return buttonPressed (string)
//          }
        }
    }

    /**
     class ScrollListener implements _____	//listener class for the JScrollBar
     {
     public void adjustmentValueChanged ()
     {
     //save the value of the scroll bar in a variable
     //make the screen "scroll down"
     }
     }
     */
}