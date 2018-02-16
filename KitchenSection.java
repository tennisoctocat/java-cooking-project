
/* Cynthia Hom
 * 4/26/2017
 * KitchenSection.java
 * This file holds KitchenSection class. KitchenSection is a parent class
 * of the different parts of the kitchen: Stove, Oven, Sink, and 
 * Knife. This file contains the basic functions and structure that all
 * of these child classes share. 
 * 
 * Concepts used:
 * 		1. Mouse events- drag and drop, etc. 
 * 		2. Image IO	- load food/background images
 * 		3. Graphics - draw images and directions depending on step 
 * 			of user's cooking
 * 		4. Inheritance: kitchensection is the parent class of Sink, 
 * 			Knife, Oven, Stove
 *
 * Testing:
 *     Should work: Following the directions on the screen should 
 * 			cause the directions to change. Nothing should change
 * 			if the user does not follow the directions. 
 * 			ex) dragging food to a container, clicking on the handle of 
 * 				the faucet or oven
 *     Should not work: Not following the directions should not change
 * 			the appearance of the panel
 * 			ex) clicking on the faucet handle when the directions say to 
 * 				drag the food to the sink, pressing anywhere on the left
 * 				side of the panel
 */

import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;   //MouseEvent/Listeners

import javax.imageio.ImageIO;   //needed to load and read image files
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.File;

public class KitchenSection extends JPanel implements MouseListener, MouseMotionListener   //parent class for all parts of the kitchen-Sink, Knife,Stove, Oven
{
    //instances used as pointers to change card layouts in these classes
    protected CookingPanel cp;	//allows these classes to change KitchenSection shown
    protected MainCookPanel mcp;	//allows these classes to switch to quiz panel when done

    protected String foodName;  //regular name of food- way it appears on button

///    protected boolean plate;  //determines if entire cooking proccess is done and plate should be shown

    //vars: Background, Start/End Food, Container, and Plate images
    //vars are protected so that child classes can access
    protected String backgroundName; // image names for images to be loaded in try-catch
    protected String startFoodName, endFoodName;
    ///   protected  String plateName;
    protected String containerName, endContainerName; //container is the
    //pot, pan, or ovenPan - object that food is cooked in
    //endContainer is the plate or thing food is put in when done in the kitchensection

    //declare images
    protected Image backgroundImage;    //protected so oven class can access - change oven to open/closed when neccesary
    private Image startFoodImage;   //how food looks at beginning of this step of cooking
    private Image endFoodImage; //food at end of this cooking step
    ///  private Image plateImage;
    private Image containerImage, endContainerImage;

    protected String [] directions; //tells user what to do

    protected int foodX, foodY;	//x and y coordinates so that users can drag and drop
    protected int containerX, containerY;
    protected int mouseX, mouseY;
    protected int endContainerX, endContainerY;	//so that we can tell if food has been
    //dragged to the ending container

    //variables for width and height of food and container images
    protected int foodWidth, foodHeight;
    protected int containerWidth, containerHeight;
    private int endContainerWidth, endContainerHeight;
    protected int panelWidth, panelHeight;
    protected int backgroundWidth, backgroundHeight;

    //if foods/containers are being dragged and if the food is inside of the container
    private boolean foodSelected;
    private boolean containerSelected;
    protected boolean foodInContainer;
    protected boolean foodInEndContainer;

    //  protected int foodStepNum;	//controls which KitchenSection is shown
    protected int cookStepNum;	//step within the KitchenSection

    protected boolean foodDone;	//determines whether the food has completed its
    //step in the particular section of the kitchen

    protected String imgDir;

    //constructor initialize variables and call getMyImage to load images
    public KitchenSection()
    {
        addMouseListener(this);
        addMouseMotionListener(this);

        //initialize x/y positions of mouse- container and
            //food x/y positions not initialized so that they
            //stay constant as kitchensection for a given food changes
        mouseX = mouseY = -1;

        foodWidth = foodHeight = 150;		//food images are 50 by 50
        containerWidth = containerHeight = endContainerHeight
                = endContainerWidth = 240; //container images are 100 by 100

        cookStepNum = 0;
        ///     plateName = "plate.jpg";	//plate image's name

        foodSelected = containerSelected =
                foodDone = false;

        imgDir = "sources/";

//        System.out.println("In kitchensection costructor");

        //initialize images to null
        backgroundImage =startFoodImage = endFoodImage =
                containerImage = endContainerImage = null;

        //all other vars initialized in child class' constructors or in update vars
    }

    //updates vars after some vars modified in child classes
    public void updateVars()
    {
        panelWidth = mcp.getWidth();
        panelHeight = mcp.getHeight();

        //change background image's dimensions so ratio stays same but the width fills half the panel
        backgroundWidth = backgroundHeight = panelWidth/2;

        //update x/y positions to their starting places
        containerX = panelWidth/4 + 90;
        containerY = panelHeight/10;

        //set ending container coordinates
        endContainerX = panelWidth/4; // in middle of screen

        foodX = panelWidth/4 - 20;
        foodY = panelHeight/10;
    }

    //getMyImage uses try-catch block to load images
    public void getMyImage()
    {
        //declare/initialize files
        File startFoodFile = new File (imgDir +  startFoodName);
        File endFoodFile = new File (imgDir + endFoodName);
        File backgroundFile = new File (imgDir + backgroundName);//backgroundName);
        ///    File plateFile = new File (plateName);
        ///   File endContainerFile = new File ("Java Project Images/" + endContainerName);
        File endContainerFile = null;
        if (endContainerName != null)
            endContainerFile = new File (imgDir + endContainerName);
        //only make the container file if a container image is needed
        File containerFile = null;
        if (containerName != null)
            containerFile = new File (imgDir + containerName);
        //use try-catch to read all image files
        try
        {
            backgroundImage = ImageIO.read(backgroundFile);
            startFoodImage = ImageIO.read(startFoodFile);
            endFoodImage = ImageIO.read(endFoodFile);
            ///        plateImage = ImageIO.read(plateFile);
            if (endContainerName != null)
            {
				endContainerImage = ImageIO.read(endContainerFile);
			}
                
            //read the container file if a container image is needed
            if (containerName != null)
                containerImage = ImageIO.read(containerFile);
        }
        catch( IOException e )
        {
            System.err.println("\n\n Image can't be found. \n\n");
            e.printStackTrace();
        }

        //change background image's dimensions so ratio stays same but the width fills half the panel
        //backgroundWidth = panelWidth/2;
        // backgroundHeight = backgroundWidth;//backgroundImage.getHeight(this) * (panelWidth/2)/backgroundWidth;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        endContainerY = panelHeight/10;  //towards top of panel

        //if the food is in the container, make it appear in the container
        if (foodInContainer || foodInEndContainer)
        {
            foodX = containerX + 35;
            foodY = containerY + 35;
        }

        //draw directions
        Font directionsFont = new Font("Serif", Font.PLAIN, 25);
        g.setFont(directionsFont);
        g.drawString(directions[cookStepNum], 0, 50);	//draw directions on left

        //draw the background image to cover the right side of panel
        g.drawImage(backgroundImage, panelWidth/2, 20, backgroundWidth,
                backgroundHeight, this);

        //multiply image width by panelHeight/imageHeight so
        //width to height ratio of image stays constant- is not distorted

        /*//draw the ending container if the food is at its last step
        endContainerX = panelWidth/2; // in middle of screen
        endContainerY = panelHeight/10;  //towards top of panel*/
        if (cookStepNum == directions.length - 1)
            g.drawImage(endContainerImage, endContainerX,
                    endContainerY, containerWidth, containerHeight, this);
        //with same width and height as container images

        //if there is a container image, draw it with its correct x
        //and y coordinates
        if (containerName != null )
            g.drawImage(containerImage, containerX, containerY, containerWidth, containerHeight, this);

        //draw food image with variables for coordinates
        //use foodDone boolean to see if start or end image
        //should be drawn
        if (foodDone)
            g.drawImage(endFoodImage, foodX, foodY, foodWidth, foodHeight, this);
        else
            g.drawImage(startFoodImage, foodX, foodY, foodWidth, foodHeight, this);
    }

    public void mouseDragged(MouseEvent evt)
    {
        mouseX = evt.getX(); //save x and y coordinates in the field vars
        mouseY = evt.getY();//save x and y coordinates in the field vars
        //if the food is selected, then the x/y coordinates of food should change with the mouse
        if (foodSelected)
        {
            foodX = mouseX;
            foodY = mouseY;
        }
        //same with container, but not if user is trying to drag food out of container
        //user only drags food out of container in pot/pan
   //     System.out.println("cookStepNum is " + cookStepNum);
        if (foodSelected && foodInContainer && !cp.foodType.equals("ovenFood")
                || (cp.foodType.equals("ovenFood") && cookStepNum == 7)   )
                //6th step of oven class user drags food out of oven pan
                //user never drags pot/pan with food in it
        {
            containerSelected = false;
            foodInContainer = false;
        }
        if (containerSelected)
        {
            containerX = mouseX;
            containerY = mouseY;

        }
        repaint();
    }

    public void mouseMoved(MouseEvent evt){}

    //mouse methods: respond to user interacting with mouse
    //mouseReleased: see if food is dragged to the right place
    public void mouseReleased(MouseEvent evt)
    {
        //if food has been dragged to regular container
        foodInContainer = (foodX >= containerX && foodX + foodWidth
                <= containerX + containerWidth) &&
                (foodY >= containerY && foodY + foodHeight <= containerY + containerHeight);

        //if food has been dragged to the ending container
        foodInEndContainer = (foodX >= endContainerX && foodX + foodWidth
                <= endContainerX + endContainerWidth) &&
                (foodY >= endContainerY && foodY + foodHeight <= endContainerY + endContainerHeight);
    
        //if the user dragged the food to the ending container, and the
        //step is the last in the KitchenSection, then make the next kitchenSection
        //show up
        if (foodInEndContainer && cookStepNum == directions.length - 1) //&& cookStepNum == directions.length - 1)
        {
            cp.foodStepNum++;
            //if this is the last kitchen section, make the quiz panel show up
            //otherwise, make the next kitchen section show up
            if (cp.foodStepNum < cp.foodCards.length)
            {
                cp.cards.show(cp, cp.foodCards[cp.foodStepNum]);   //use instance of cp to change its cards

            }
            else
            {
                mcp.cardLayout.show(mcp, "quizPanel");
            }
        }

        // if the food or container images are "selected", make them not selected
        containerSelected = false;
        foodSelected = false;
        repaint();
    }

    //deterime if a food or object is pressed
    public void mousePressed(MouseEvent evt)
    {
        //make foodSelected or containerSelected true if either are selected

        mouseX = evt.getX(); //save x and y coordinates in the field vars
        mouseY = evt.getY();//save x and y coordinates in the field vars
        foodSelected = (mouseX >= foodX && mouseX <= foodX + foodWidth
                && mouseY >= foodY && mouseY <= foodY + foodHeight);
        containerSelected = (mouseX >= containerX && mouseX <= containerX + containerWidth
                && mouseY >= containerY && mouseY <= containerY + containerHeight);
        repaint();
    }
    public void mouseClicked(MouseEvent evt){}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}   //end KitchenSection