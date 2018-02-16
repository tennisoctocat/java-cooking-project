/* Cynthia Hom
 * QuizPanel.java
 *
 * This file holds QuizPanel class, along with all other panels 
 assosiated with the quiz

 Requires QuizQuestions.txt file

 */

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Quiz Panel holds all of the quiz panels
public class QuizPanel extends JPanel
{
    protected CardLayout quizCards; //cardLayout var for this panel
    protected MainCookPanel mainCookPanel;  //will be used to access buttonPressed var

    //more field vars: file name to be read, question, options A thru D, messages A thru D
    protected String fileToRead;
    protected String [] question;
    protected String [] optionA, optionB, optionC, optionD; //answer choices
    protected String [] messageA, messageB, messageC, messageD;    //feedback depending on each choice
    protected String [] mainMessage;    //overall explanation of question
    private Scanner quizQReader;

    protected int numCorrect;

    protected String pickedButton;

    protected String levelType;

    //set up panel, initialize all vars
    public QuizPanel ( MainCookPanel mcpIn )
    {
        mainCookPanel = mcpIn;  //so that mainCookPanel can be used by panel inside QuizPanel later on
        //to access buttonPressed in question panel

        //initialize vars
        fileToRead = "QuizQuestions.txt";   //file with quiz questions
        question = new String[]{"", "", ""};
        optionA = new String[]{"", "", ""};
        optionB = new String[]{"", "", ""};
        optionC = new String[]{"", "", ""};
        optionD = new String[]{"", "", ""};
        messageA = new String[]{"", "", ""};
        messageB = new String[]{"", "", ""};
        messageC = new String[]{"", "", ""};
        messageD = new String[]{"", "", ""};
        mainMessage = new String[]{"", "", ""};
        quizQReader = null;
        numCorrect = 0;	 //number of questions user gets correct out of 3\
        pickedButton = null;

        levelType = mainCookPanel.cookPanel.foodType.substring  //take substring of part before "food"
                (0, mainCookPanel.cookPanel.foodType.indexOf("Food"));  //of the var foodType in cookingPanel

        //set up panel
        //cardLayout- will hold all other quiz panels
        quizCards = new CardLayout();
        setLayout(quizCards);

        //read in info from text file
        //Use a for loop to read in three questions and their
        //options and answer explanations, storing
        //them into the proper arrays- done like this so
        //reader starts from top of file each time
        for (int qNumber = 0; qNumber < 3; qNumber++)
        {
            makeQReader();
            readQuestion(qNumber);
        }

        //make all cards
        BeginQuizPanel beginQuizPanel = new BeginQuizPanel(this);   //start of quiz
        QuizQPanel qOnePanel = new QuizQPanel(this, 0);    //quiz questions
        QuizQPanel qTwoPanel = new QuizQPanel(this, 1);
        QuizQPanel qThreePanel = new QuizQPanel(this, 2);
        EndQuizPanel endQuizPanel = new EndQuizPanel(this); //ending results

        //add all cards in order to show them
        this.add(beginQuizPanel, "begin quiz");
        this.add(qOnePanel, "question one");
        this.add(qTwoPanel, "question two");
        this.add(qThreePanel, "question three");
        this.add(endQuizPanel, "quiz results");

        //make BeginQuizPanel (panel with button to start quiz)
        //show up first
        quizCards.show(this, "begin quiz");
    }

    //makeQReader(): make reader for file
    public void makeQReader()
    {
        //declare/intitialize file object
        File questionFile = new File ("sources/" + fileToRead);
        //use try-catch block to try to make a reader
        // instance for file to be read
        try
        {
            quizQReader = new Scanner(questionFile);
        }
        catch ( FileNotFoundException e)
        {
            System.err.println("Cannot find " + fileToRead + " file.");
        }
    }

    //readQuestion(): read questions from the file
    public void readQuestion(int qNum)
    {
        int randNum = -1;    //randomly generated question number
        //used to look for level name in questionPanel
        boolean readingDone = false;     //if level is found in file
        String inLine = null;   //line read in from file
        boolean levelFound = false; //if level header has been read in
        boolean questNumFound = false;  //if question number has been read in


        //generate a random number between one and ten, this is the
        //number of the question to be used
        randNum = (int)(Math.random() * 7 + 1);     //(int)(Math.random() * 10 + 1);
        
        //keep reading in and checking lines until all needed
        //info is read in
        while (quizQReader.hasNext() && !readingDone)
        {
            inLine = quizQReader.nextLine();
            //if level name has not been found yet, keep
            //comparing line to level type until it is found
            //otherwise, if the question number has not yet
            //been found, then look for the question number
            if (!levelFound)
            {   //level is found if the levelType is found in the line and if
                //the line contains the word "level"
                levelFound = (inLine.toLowerCase().indexOf(levelType) != -1) &&
                        (inLine.indexOf("Level") != -1);
            }
            else if (!questNumFound)
            {
                questNumFound = (inLine.indexOf(randNum + "") != -1);
                //then, when both question number and level name
                //have been found, store the lines into the
                //proper arrays
                if (questNumFound)
                {
                    // after finding the number, store the next line into
                    //the question variable (part after the '.')
                    question[qNum] = inLine.substring(inLine.indexOf('.') + 1);
                    // For options/messages:
                    // Store the next into the option variable until the dash, then
                    // store the rest of the line after the dash into the message
                    // variable
                    //optionA and messageA
                    inLine = quizQReader.nextLine();
                    optionA[qNum] = inLine.substring(0, inLine.indexOf('-'));
                    messageA[qNum] = inLine.substring(inLine.indexOf('-') + 1);
                    //B
                    inLine = quizQReader.nextLine();
                    optionB[qNum] = inLine.substring(0, inLine.indexOf('-'));
                    messageB[qNum] = inLine.substring(inLine.indexOf('-') + 1);
                    //C
                    inLine = quizQReader.nextLine();
                    optionC[qNum] = inLine.substring(0, inLine.indexOf('-'));
                    messageC[qNum] = inLine.substring(inLine.indexOf('-') + 1);
                    //D
                    inLine = quizQReader.nextLine();
                    optionD[qNum] = inLine.substring(0, inLine.indexOf('-'));
                    messageD[qNum] = inLine.substring(inLine.indexOf('-') + 1);

                    // at end, store last bit into the mainMessage variable
                    mainMessage[qNum] = quizQReader.nextLine();

                    readingDone = true;
                }
            }
        }
    }

    //BeginQuizPanel holds a button to start the quiz
    class BeginQuizPanel extends JPanel
    {
        private QuizPanel quizPanel;    //to change to next card

        public BeginQuizPanel( QuizPanel quizPanelIn ) //set up panel
        {
            quizPanel = quizPanelIn;

            //flow layout- default

            //make JLabel instances to tell user they will be starting a quiz
            //(two so that it'll fit on screen well)
            JLabel messageOne = new JLabel("Great job! You've successfully cooked "
                    + mainCookPanel.questPanel.buttonPressed + "! ");
            JLabel messageTwo = new JLabel("Now let's answer " +
                    "some questions about the cooking process. ");

            //make JButton instance to start the quiz, add listener
            JButton begQuizButton = new JButton("Begin Quiz");
            BegQuizbHandler begQuizbHandler = new BegQuizbHandler();
            begQuizButton.addActionListener(begQuizbHandler);

            //set font for all components
            Font bigMessageFont = new Font( "Serif", Font.PLAIN, 40);
            messageOne.setFont(bigMessageFont);
            messageTwo.setFont(bigMessageFont);
            begQuizButton.setFont(bigMessageFont);

            //add all to panel
            add(messageOne);
            add(messageTwo);
            add(begQuizButton);
        }

        //listens to the start quiz button
        class BegQuizbHandler implements ActionListener
        {
            //change cards in QuizPanel when button is pressed- go to the quiz
            public void actionPerformed (ActionEvent evt)
            {
                //show the first question card in QuizPanel
                //using instance of QuizPanel class passed in
                quizCards.next(quizPanel);    //uses pointer object passed in
            }
        }
    }


    //panel with actual quiz questions and optionQPanel extends JPanels, reads in Q's from text file
    class QuizQPanel extends JPanel
    {
        private QuizPanel quizPanel;
        private int questNum;
        //constructor: set up panel
        public QuizQPanel ( QuizPanel quizPanelIn, int questNumIn )
        {
            quizPanel = quizPanelIn;
            questNum = questNumIn;

            //set layout to Border
            BorderLayout border = new BorderLayout();
            setLayout(border);

            //make a JLabel for question with the question variable
            JLabel questionLabel = new JLabel( question[questNumIn]);
            questionLabel.setFont( new Font("Serif", Font.PLAIN, 40));
            //add to panel
            add(questionLabel, border.NORTH);

            //make 4 JRadioButtons, one for each option,
            JRadioButton buttonA = new JRadioButton( optionA[questNumIn]);
            JRadioButton buttonB = new JRadioButton( optionB[questNumIn]);
            JRadioButton buttonC = new JRadioButton( optionC[questNumIn]);
            JRadioButton buttonD = new JRadioButton( optionD[questNumIn]);

            //set font for buttons
            Font optionFont = new Font("Serif", Font.PLAIN, 25);
            buttonA.setFont(optionFont);
            buttonB.setFont(optionFont);
            buttonC.setFont(optionFont);
            buttonD.setFont(optionFont);

            // add OpButtonListner for all buttons
            OpButtonListener optionListener = new OpButtonListener();
            buttonA.addActionListener(optionListener);
            buttonB.addActionListener(optionListener);
            buttonC.addActionListener(optionListener);
            buttonD.addActionListener(optionListener);

            //add all buttons to group
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(buttonA);
            buttonGroup.add(buttonB);
            buttonGroup.add(buttonC);
            buttonGroup.add(buttonD);

            //add all buttons to a panel with GridLayout
            JPanel qChoicesPanel = new JPanel();
            GridLayout qLayout = new GridLayout(4,1); //one column, four rows
            qChoicesPanel.setLayout(qLayout);
            qChoicesPanel.add(buttonA);
            qChoicesPanel.add(buttonB);
            qChoicesPanel.add(buttonC);
            qChoicesPanel.add(buttonD);

            //add this panel to the QuizQPanel
            add(qChoicesPanel, BorderLayout.CENTER);

            //make and add JButton to submit answer, add SubButtonListener
            JButton submitButton = new JButton("Submit");
            submitButton.setFont (new Font("Serif", Font.PLAIN, 30));
            SubButtonListener subButtonListener = new SubButtonListener();
            submitButton.addActionListener(subButtonListener);
            qChoicesPanel.add(submitButton, BorderLayout.SOUTH);
        }

        //listener for JRadioButtons (options A-D)
        class OpButtonListener implements ActionListener
        {
            //actionPerformed: determine which button is pressed
            public void actionPerformed( ActionEvent evt )
            {
                //determine which radio button is pressed, store into a
                //buttonSelected variable
                pickedButton = evt.getActionCommand();
            }
        }

        //listener for submit button
        class SubButtonListener implements ActionListener
        {
            //make proper JDialogBox appear
            public void actionPerformed(ActionEvent evt)
            {
                String message = null;
                //use message A, B, C, or D depending on button selected
                //and the main message
                if (pickedButton.equals(optionA[questNum]))
                    message = messageA[questNum] + " " + mainMessage[questNum];
                else if (pickedButton.equals(optionB[questNum]))
                    message = messageB[questNum] + " " + mainMessage[questNum];
                else if (pickedButton.equals(optionC[questNum]))
                    message = messageC[questNum] + " " + mainMessage[questNum];
                else if (pickedButton.equals(optionD[questNum]))
                    message = messageD[questNum] + " " + mainMessage[questNum];

                //if the message contains the word "correct", then
                // increment the total number of correct answers
                if (message.toLowerCase().indexOf ("incorrect") == -1)
                {
                    numCorrect++;
                }

                //make a JDialogBox show up with proper feedback:
                //the message and the ok button
                JOptionPane.showMessageDialog(quizPanel, message);

                //show the next card
                quizCards.next(quizPanel);
            }
        }
    }

    //EndQuizPanel holds the panel at end of quiz that tells user their results
    class EndQuizPanel extends JPanel
    {
        private QuizPanel quizPanel;

        //constructor: set up panel
        public EndQuizPanel( QuizPanel quizPanelIn )
        {
            quizPanel = quizPanelIn;
            //set up panel with a BorderLayout
         /*   BorderLayout borderLayout =  new BorderLayout();
            setLayout(borderLayout);
            */
            //make a JLabel with the string "Great job!" to add
            //to top of panel
            JLabel goodJobLabel = new JLabel("Great job!");
            goodJobLabel.setFont( new Font("Serif", Font.PLAIN, 50));
            add(goodJobLabel, BorderLayout.NORTH);

            //set up "return to main menu" JButton instance
            JButton mainMenuButton = new JButton("Return to main menu");
            mainMenuButton.setFont( new Font("Serif", Font.PLAIN, 30));
            //add EndQuizbHandler to button
            EndQuizbHandler endQuizbHandler = new EndQuizbHandler();
            mainMenuButton.addActionListener(endQuizbHandler);
            add(mainMenuButton); //, BorderLayout.SOUTH); //add button to bottom of panel
        }

        //paintComponent: make the correct feedback show up
        public void paintComponent (Graphics g)
        {
            super.paintComponent(g);    //reset background
            //make a string variable for the feedback that the user
            //receives about performance on the quiz
            //if they have answered all 3 questions correctly,
            //then tell them they have unlocked the next level
            //otherwise tell them to try again
            String feedback = null;
            feedback = "You've answered " + numCorrect
                    + " questions correctly! ";
            if ( numCorrect == 3 )
            {
                //dont say they unlocked a level if there are no more levels left,
                    //rather, tell them they've become a master chef
                if (mainCookPanel.highLevelCompleted < 3)
                    feedback = feedback + "You've unlocked the next level! ";
                else
                    feedback = feedback + "Congratulations! You've become a master chef!";
                //increment the highest level completed if the user has not completed
                    //this level successfully before
                if ( (levelType.equals("knife")) || (levelType.equals("pot") && mainCookPanel.highLevelCompleted < 2)
                    || (levelType.equals("pan") && mainCookPanel.highLevelCompleted < 3) ||
                        (levelType.equals("oven") && mainCookPanel.highLevelCompleted < 4)  )
                    mainCookPanel.highLevelCompleted++;
            }

            //make a JLabel with this string, add it to the center
         //   JLabel feedbackLabel = new JLabel(feedback);
            g.setFont(new Font ("Serif", Font.PLAIN, 40));
            g.drawString(feedback, 0, quizPanel.getHeight()/2);

            if (numCorrect != 3)
            {
                g.drawString("If you play this level again "
                        + " and answer all questions correctly,", 0,
                        quizPanel.getHeight()/2 + 60);
                g.drawString("you will "
                        + " unlock the next level! ", 0, quizPanel.getHeight()/2 + 120);
            }
        }

        //EndQuizbHandler listens to the button to return to the main menu
        class EndQuizbHandler implements ActionListener
        {
            //actionPerformed: make the main menu show up
            public void actionPerformed (ActionEvent evt)
            {
                //show the main menu card, using instance of MainPanel class
                quizPanel.mainCookPanel.cardLayout.show
                        ( quizPanel.mainCookPanel, "questionPanel");
            }
        }
    }
}
