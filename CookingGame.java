
/* Cynthia Hom
CookingGame.java
4/23/17

This java file holds the classes for the basic set up and beginning of
the game.
CookingGame is the main frame of the project. This frame holds
an instance of the panel MainPanel that will hold all of the other
panels in the game. StartPanel is one of the panels that is added to
the MainPanel, and is included in this java file because it only holds
the title of the game and the button to start the game.
* this is passed in as a parameter to the panels being added to 
* the main cooking panel to act as a pointer to the main cooking panel,
* allowing the cards in the main cooking panel to be changed from these
* panels. 
* 
* Concepts used:
* 	JPanel, JFrame, JButtons, CardLayout	(components and layouts)

Testing:
    Should work: Clicking on the button to start the game should
    * 	bring the user to the question panel
    Should not work: clicking anywhere else on the screen should 
    * 	not do anything, clicking on the button should only show the 
    * 	question panel and nothing else
 */


import javax.swing.JButton;		//JButton, JLabel, JFrame, JPanel
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.CardLayout;		//layout, Font
import java.awt.Font;

import java.awt.event.ActionListener;	//import ActionEvent/Listener
import java.awt.event.ActionEvent;

public class CookingGame    //class that sets up the overall frame that the game is played in
{
    //empty constructor
    public CookingGame ()
    {
    }

    public static void main(String[] args)
    {
        CookingGame cookingGame =  new CookingGame();
        cookingGame.runFrame(); //call runFrame() to set up characteristics of frame
    }

    //runFrame sets up characteristics of frame
    public void runFrame()
    {
        //set up frame
        JFrame cookingFrame = new JFrame("It's Cooking Time!");
        cookingFrame.setSize(1360,730);	//covers almost whole screen
        cookingFrame.setLocation(0,0);
        cookingFrame.setResizable(true);
        cookingFrame.setDefaultCloseOperation(cookingFrame.EXIT_ON_CLOSE);
        //make and add MainCookPanel instance to frame
        MainCookPanel mainCookPanel = new MainCookPanel();
        cookingFrame.getContentPane().add(mainCookPanel);
        cookingFrame.setVisible(true);
    }
}

//this panel has a cardLayout and holds all other panels in the game
class MainCookPanel extends JPanel
{
    protected int highLevelCompleted;    //highest level completed
    protected CardLayout cardLayout; //protected so that actionEvent, etc. methods can access
    protected CookingPanel cookPanel;
    protected QuizPanel quizPanel;
    protected QuestionPanel questPanel; //is a field var so that its buttonPressed variable can be
    //accessed from other classes
    public MainCookPanel ()
    {
        highLevelCompleted = 0;

        //set up panel with CardLayout
        cardLayout = new CardLayout();
        setLayout (cardLayout);

        //make each card- 'this' allows other classes to change cardLayout
        //by sending a pointer to this class
        StartPanel startPanel = new StartPanel(this);	//panel with title
        questPanel = new QuestionPanel(this); //Q for which food to cook

        cookPanel = null;   //will be made later when button is pressed in questPanel, this is so that the
        //buttonPressed variable can be passed in as a parameter to the constructor (tells which food is being cooked)_

        quizPanel = null; 	// holds quiz questions, will be made later so food button has already been pressed when it is made

        //add each card to panel
        this.add(startPanel, "startingPanel");
        this.add(questPanel, "questionPanel");

        //show StartPanel first
        cardLayout.first(this);
    }
}

//first panel to show up, holds title and button to start the game
class StartPanel extends JPanel
{
    private MainCookPanel mcp;	//mcp is var. that will be used as a
    //pointer to the mainCookPanel class so that the cardLayout
    //can be changed from this class
    //constructor sets up panel

    public StartPanel(MainCookPanel mcpIn)
    {
        //mcpIn allows the use of the mainCookPanel instance to change the
        //card in mainCookPanel from this class
        mcp = mcpIn;

        //Layout is FlowLayout- default
        //make instance of JLabel (holds title)
        Font titleFont = new Font("Serif", Font.BOLD, 50);
        JLabel titleLabel = new JLabel("It's Cooking Time!");
        titleLabel.setFont(titleFont);

        add(titleLabel);    //add to panel

        //instance of JButton(to start game)
        JButton startButton = new JButton("Start Game");
        startButton.setFont(titleFont);
        //add listener class to JButton
        StartButtonListener startBListener = new StartButtonListener();
        startButton.addActionListener(startBListener);
        add(startButton);    //add to panel
    }

    class StartButtonListener implements ActionListener //listener for start button
    {
        //show the QuestionPanel- allows user to pick what food to learn to cook
        public void actionPerformed(ActionEvent evt)
        {
            //mcp is the instance of the panel with the cardLayout
            mcp.cardLayout.next(mcp); //make questionPanel show up
        }
    }

}