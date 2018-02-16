

/* Cynthia Hom
 * 
 * This file contains all of the child classes of KitchenSection,
 *  Sink, Oven, Knife, and Stove. These classes all inherit a
 *  similar structure and some similar variable from KitchenSection,
 *  but each has some variables and code unique to itself.
 *
 * See KitchenSection.java for testing and concepts used. 
 */

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
//import java.awt.event.MouseListener;

//Sink class holds graphics for washing foods/filling up pots/pans
class Sink extends KitchenSection
{
    private boolean water;  //if water should be drawn
    private int sinkHandleX, sinkHandleY, sinkHandleWidth, sinkHandleHeight;
    private int sinkX, sinkY, sinkWidth, sinkHeight;
    private String endingContainer; //used for directions- same as endContainerName
    //but in different format- see constructor

    //constructor: initialize all vars
    //instances of MainCookPanel and CookingPanel sent in to use to change their card layouts from
    //any kitchenSection child class
    public Sink(CookingPanel cpIn, MainCookPanel mcpIn, String foodNameIn)   //every food is washed, so foodNameIn is passed in only
    {
        super();

        //update field vars in KitchenSection to parameters passed in
        cp = cpIn;      //to change kitchen sections
        mcp = mcpIn;

        updateVars();

        /*//vars to be used for locations when images are put on screen
        panelWidth = mcp.getWidth();
        panelHeight = mcp.getHeight();*/

        //initialize sink's vars
        sinkHandleX =  panelWidth/2 + (int)(175 * (double)backgroundWidth/400);       //position of handle
        //add panelWidth/2 because otherwise the coordinates will be based on the right half of the screen only
        sinkHandleY = (int)(170 * (double)backgroundHeight/400);        //400 is width and height of original sink image
        sinkHandleWidth = (int)((75) * (double)backgroundWidth/400);    //background height and width are of resized image
        sinkHandleHeight = (int)((80) * (double)backgroundHeight/400);

        sinkX = panelWidth/2 + (int)(100 * (double)backgroundWidth/400);    // dimensions for section of sink under faucet
        sinkY = (int)(100 * (double)backgroundHeight/400);
        sinkWidth = (int)(150 * (double)backgroundWidth/400);
        sinkHeight = (int)(200 * (double)backgroundHeight/400);
        //if the object to be dragged to the sink is not the pot, then make the food name the passed in parameter
        //otherwise, make the item to dragged into the sink the pot and keep food name the same
        if (foodNameIn.indexOf(".jpg") == -1)
        {
            foodName = foodNameIn;
            startFoodName = endFoodName = "uncut" + foodName + ".jpg";	//specific to food
        }
        else
        {
            foodName = "pot";
            startFoodName = endFoodName = foodNameIn;
        }

        //initialize image name vars in KitchenSection class to to correct names
        backgroundName = "sink.jpg";
        containerName = null;

        //initialize the contianer for the washed food to be dragged to
        //depending on type of food.
        if ( cp.foodType.equals("ovenFood"))
        {
            endContainerName = "ovenPan.jpg";
            endingContainer = "oven pan";
        }
        else if (foodName.equals("pot"))
        {
            endContainerName = null;    //no ending container if pot is being filled
        }
        else
        {
            endContainerName = "cuttingBoard.jpg";
            endingContainer = "cutting board";
        }

        //initialize other KitchenSection vars to values specific to sink
        directions = new String [] {"Drag the " + foodName + " under the faucet. ", "Now turn the " +
                "sink on by pressing the handle on the right. ", "Great! Turn the sink " +
                "off by pressing the handle again. ", "Drag the " + foodName + " out of the " +
                "sink"};
        //if we are not filling a pot, drag the food to the ending container after washing it
        if (!foodName.equals("pot"))
            directions[directions.length - 1] = directions[directions.length - 1] +
                    " and onto the " + endingContainer + ".";

        getMyImage();
        repaint();
    }

    //paintComponent: make water show up if water is suppossed to show up
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);    //call paintComponent in KitchenSection
        if (cookStepNum == 2)
        {
            Color lightBlue = new Color(83, 163, 226);
            g.setColor(lightBlue);    //light blue color
            g.fillRect(sinkX + sinkWidth/2 + 50, sinkY + 10, 20, sinkHeight);	//water rectangle starts at faucet opening, is 20 pixels wide
        }
    }

    //mouseClicked sees if user clicks on handle of sink
    public void mouseClicked(MouseEvent evt)
    {
        super.mouseClicked(evt);

        //handle vars to see if user clicked on handle of sink to make water run
        //   int handleX, handleY, handleWidth, handleHeight;
        boolean handleClicked = mouseX >= sinkHandleX &&
                mouseX <= (sinkHandleX + sinkHandleWidth) && mouseY >= sinkHandleY &&
                mouseY <= sinkHandleY + sinkHandleHeight;

        // draw water if mouse pos. is within pos. of handle and the
        // user is at the correct step to turn on water), if the
        // user clicks the handle again, increment the step again to turn off the water
        if (cookStepNum == 1 && handleClicked)
        {
            cookStepNum++;
            repaint();
        }
        else if (cookStepNum == 2 && handleClicked)
        {
            cookStepNum++;
            repaint();
        }
    }

    //mouse released: see if food is dragged under sink
    public void mouseReleased( MouseEvent evt )
    {
        super.mouseReleased(evt);

        //is the food underneath the faucet
        boolean underSink = (mouseX >= sinkX && mouseX <= sinkX + sinkWidth)
                && (mouseY >= sinkY && mouseY <= sinkY + sinkHeight);
        //if the food is at step 0, meaning that the user is supposed to drag
        //    the food to the sink, and the user dragged the food to the sink,
        //  increment the step and change the appearance of the screen
        if (underSink && cookStepNum == 0)
        {
            cookStepNum++;
            repaint();
        }

        //if the user dragged the pot out of the sink and is supposed to, show
        //the stove kitchensection
        if (foodName.equals("pot") && !underSink && cookStepNum == directions.length - 1)
        {
            cp.foodStepNum++;
            cp.cards.show(cp, cp.foodCards[cp.foodStepNum]);   //use instance of cp to change its cards
        }
    }
}

//oven class holds specifics for roasting/baking foods
class Oven extends KitchenSection
{
  //  private boolean ovenClosed;   //if oven door is opened or not
    private String closedOvenName;  //name of image for the closed oven
    private Image closedOvenImage;
    private String openOvenName;
 ///   private Image openOvenImage;    //used to hold openOvenImage while backgroundImage is set to closedOven
    private int handleX, handleY, handleWidth, handleHeight;
    private int ovenX, ovenY, ovenWidth, ovenHeight;
    private int doorX, doorY, doorWidth, doorHeight;
    //constructor:
    public Oven(CookingPanel cpIn, MainCookPanel mcpIn, String foodNameIn)
    {
        super();

        cp = cpIn;
        mcp = mcpIn;
        foodName = foodNameIn;

        updateVars();

       /* //make food appear in container on the left of the screen
        containerX = panelWidth/2;
        containerY = panelHeight/8;
        foodX = containerX + 10;
        foodY = containerY + 10;*/

       foodInContainer = true;  //user has dragged food to ovenPan already

        closedOvenName = "closedOven.jpg";
        openOvenName = "openOven.jpg";

        //set the dimensions of oven handle depending on size of oven pic
        handleX = panelWidth/2 + (int)(67 * (double)backgroundWidth/600);   //67 is original x, 600 is original width of pic
        handleY = (int)(131 * (double)backgroundHeight/487);    //132 is original y
        handleWidth = (int)(429 * (double)backgroundWidth/600); //429 is original width
        handleHeight = (int)(37 * (double)backgroundHeight/487);

        //dimensions of open oven door in terms of size of open oven pic
        doorX = panelWidth/2 + (int)(45 * (double)backgroundWidth/600); //open oven pic is 600 by 400
        doorY = (int)(270 * (double)backgroundHeight/400);
        doorWidth = (int)(505 * (double)backgroundWidth/600);
        doorHeight = (int)(130 * (double)backgroundHeight/400);

        //dimensions of open oven in terms of size of open oven pic
        ovenX = panelWidth/2 + (int)(145 * (double)backgroundWidth/600);    //oven pic is 600 by 400
        ovenY = (int)(60 * (double)backgroundHeight/400);
        ovenWidth = (int)((550-145) * (double)backgroundWidth/600);
        ovenHeight =(int)((200) * (double)backgroundHeight/400);

        //initialize image name vars to to correct names
        backgroundName = openOvenName;
        containerName = "ovenPan.jpg";
        startFoodName = "uncut" + foodName + ".jpg";	//specific to food
        endFoodName = "cooked" + foodName + ".jpg";
        endContainerName = "plate.jpg";

        //update foodName var to parameter passed in
        foodName = foodNameIn;

        //update directions
        directions = new String [] {"Click on the oven handle to open the oven door. ",
                "Drag the oven pan with the " + foodName + " into the oven. ",
                "Click on the oven again to close the oven door", "Click on "
                + "the oven to fast forward until the " + foodName 
                + " is done. ",
                "Awesome! The " + foodName + " is now done! Click on the oven handle to open the oven door.",
                "Drag the oven pan with the " + foodName + "out of the oven. ", "Now click " +
                "on the oven door to close the oven. ",
                "Great! Now drag the cooked " + foodName + " out of the oven pan and onto the plate. "};

        //initialize, then load ovenClosedImage
     ///   openOvenImage = null;
        getMyImage();
      ///  closedOvenImage = backgroundImage;    //background image is currently the open oven
    }

    //loads ovenClosed image, calls super class to load other images
    public void getMyImage()
    {
        super.getMyImage();
        File closedOvenFile = new File(imgDir + closedOvenName);
        try
        {
            closedOvenImage = ImageIO.read(closedOvenFile);
        }
        catch (IOException e)
        {
            System.err.println("\n\n Closed oven image can't be found. \n\n");
            e.printStackTrace();
        }
    }

    //paint component: show the oven door closed if it is suppossed to be
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        //draw closed oven image if it is supposed to be drawn
       if (cookStepNum == 0 || cookStepNum == 3 ||
               cookStepNum == 4 || cookStepNum == 7)
           g.drawImage(closedOvenImage, panelWidth/2, 20,
                   backgroundWidth, backgroundHeight, this); //same coordinates as background image
    }

    //mouseClicked sees if user clicks on handle of oven
    public void mouseClicked(MouseEvent evt)
    {
        super.mouseClicked(evt);

        //is handle clicked so oven door should open/close
        boolean handleClicked = ( mouseX >= handleX && mouseX <= handleX + handleWidth
                && mouseY >= handleY && mouseY <= handleY + handleHeight);
        boolean doorClicked = mouseX >= doorX && mouseX <= doorX + doorWidth
                && mouseY >= doorY && mouseY <= doorY + doorHeight;
                //if mouse clicked on oven image
		boolean ovenClicked = (mouseX >= panelWidth/2 && mouseX <= panelWidth/2 + backgroundWidth )
                && (mouseY >= 20 && mouseY <= 20 + backgroundHeight );

        //if mouse is clicked on handle of oven, and user is supposed to open the oven by
        // clicking on handle, then draw the oven as open by incrementing the step
        if( (cookStepNum == 0 || cookStepNum == 4) && handleClicked )
        {
            cookStepNum++;
         //   backgroundImage = openOvenImage;
            repaint();
        }
        //THIS IS TEMPORARY until timer is integrated
        if ( cookStepNum == 3 && ovenClicked)
        {
            cookStepNum++;
            foodDone = true;
            repaint();
        }
        //if mouse is clicked on door of oven, and user is supposed to close the oven
        //by clicking on door, draw the oven as closed and increment the step
        if((cookStepNum == 2 || cookStepNum == 6) && doorClicked)
        {
            cookStepNum++;
         //   backgroundImage = closedOvenImage;
            repaint();
        }
        
    }

    //mouseReleased- see if user drags an object to the correct place
    public void mouseReleased(MouseEvent evt)
    {
        super.mouseReleased(evt);
        //if user has dragged oven pan into oven
        boolean dragToOven = ( containerX >= ovenX &&
                containerX + containerWidth <= ovenX + ovenWidth) &&
                (containerY >= ovenY && containerY + containerHeight <= ovenY + ovenHeight);

        //increment food step num and repaint if the user has completed the correct action
        //2: drag container to oven
        //6: drag container out of oven
        if ( (cookStepNum == 1 && dragToOven) ||
                (cookStepNum == 5 && !dragToOven) ) //foodInContainer is kitchen section's field var
        {
            cookStepNum++;
            repaint();
        }
    }
}

//Knife class holds graphics for cutting foods
class Knife extends KitchenSection
{
    private String knifeImageName;
    private Image knifeImage;
    private boolean knifeSelected;
    private int knifeX, knifeY, knifeWidth, knifeHeight;    //knife width and height refer to blade
    private int boardX, boardY, boardWidth, boardHeight;
    private int cuts;   //number of times knife is dragged over food

    //constructor: initialize vars, set up panel
    public Knife(CookingPanel cpIn, MainCookPanel mcpIn, String foodNameIn)
    {
        super();

        cp = cpIn;
        mcp = mcpIn;
        foodName = foodNameIn;

        updateVars();

        //initialize field vars specific to knife class
        knifeImageName = "knife.jpg";
        knifeImage = null;
        knifeSelected = false;
        knifeX = panelWidth/2;
        knifeY = 200;
        knifeHeight = 60;
        knifeWidth = 150;

        boardX = panelWidth/2;  //cutting board is the background image
        boardY = 20;
        boardWidth = backgroundWidth;
        boardHeight = backgroundHeight;

        cuts = 0;   //number of times knife has "cut" food

        //update image names
        containerName = null;
        backgroundName = "cuttingboard.jpg";
        startFoodName = "uncut" + foodName + ".jpg";	//specific to food
        endFoodName = "cut" + foodName + ".jpg";

        //initialize endContainerName depending on the type of food
        if (cp.foodType.equals("ovenFood"))
            endContainerName = "plate.jpg"; //"ovenPan.jpg";
        else if (cp.foodType.equals("knifeFood"))
            endContainerName = "plate.jpg";
        else
            endContainerName = "plate.jpg"; //"bowl.jpg";

        //update foodName in CookingPanel with parameter passed in
        foodName = foodNameIn;

        //update directions array to be specific to cutting the food
        String endContainer = endContainerName.substring(0, endContainerName.indexOf('.'));
        directions = new String [] {"Drag the " + foodName + " onto the center of the cutting board. ",
                "Drag the knife up and down over the food to cut the " + foodName + ". ",
                "Now drag the food off of the cutting board and into the "
                        + endContainer + ". "};
        //container that food is being dragged on is the portion of
        //the endContainer name before the .jpg
        getMyImage();
        repaint();
    }

    //try catch block to load knife image and call super's to load other images
    public void getMyImage()
    {
        super.getMyImage(); //load other images
        File knifeImageFile = new File(imgDir + knifeImageName);

        //try catch block to load file
        try
        {
            knifeImage = ImageIO.read(knifeImageFile);
        }
        catch (IOException e)
        {
            System.err.println("\n\n Knife image can't be found. \n\n");
            e.printStackTrace();
        }
    }

    //mousePressed(): make knife selected if it is
    public void mousePressed(MouseEvent evt)
    {
        super.mousePressed(evt);

        //if mouse pressed on knife, the knife is clicked on and knifeSelected is true
        knifeSelected = ((mouseX >= knifeX && mouseX <= knifeX + knifeWidth)
                && (mouseY >= knifeY && mouseY <= knifeY + knifeHeight));
        
        repaint();
    }

    //mouseReleased(): make knife unselected if it is, see if user has dragged food to cutting board
    public void mouseReleased( MouseEvent evt )
    {
        super.mouseReleased(evt);

        knifeSelected = false;  //user cannot be holding knife if mouse is not down

        //whether or not user has dragged food to cutting board
        boolean foodToBoard = (foodX >= boardX && foodX + foodWidth <= boardX + boardWidth)
                && (foodY >= boardY && foodY + foodHeight <= boardY + boardHeight);
        //if user has dragged food to board, and is suppossed to, then increment the step number
        if (foodToBoard && cookStepNum == 0)
            cookStepNum++;

        repaint();
    }

    //mouseMoved(): make knife move if it is selected, make food cut if it
    //is dragged on the food
    public void mouseDragged( MouseEvent evt )
    {
        super.mouseDragged(evt);  //get x and y coordinates in super class' method
        //if knife is selected, make its coordinates the same as the mouse's,

        if (knifeSelected)
        {
            knifeX = mouseX;
            knifeY = mouseY;
            //if knife is being dragged over food
            boolean knifeOnFood = (knifeX >= foodX - foodWidth/2 && knifeX <= foodX + foodWidth/2)
                    && (knifeY >= foodY - foodHeight/2 && knifeY <= foodY + foodHeight/2);
            //if knife is being dragged over food, then increase the cuts variable
            //add foodWidth/height so that knife does not need to be exactly on top of food
            if (knifeOnFood && cookStepNum == 1)
            {
                cuts++;
                //if cuts exceeds 100, then show the food as cut
                foodDone = cuts > 50;
                //if the food is done, then increment the step number to make the cut image appear
                if (foodDone)
                    cookStepNum++;
            }
            repaint();
        }
    }

    //paintComponent: move knife if it is supposed to move
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(knifeImage, knifeX, knifeY, knifeWidth, knifeHeight, this);
    }
}


//Stove class holds graphics for using pot/pans on stove
class Stove extends KitchenSection
{
    private int burnerX, burnerY, burnerWidth, burnerHeight;
    private int dialX, dialY, dialWidth, dialHeight;
    private String potPan;

    //constructor: update field vars in cookingPanel
    public Stove(CookingPanel cpIn, MainCookPanel mcpIn, String foodNameIn)
    {
        super();    //call cookingPanel's constructor

        cp = cpIn;  //update vars in KitchenSection- so that they are updated even if pasta is being cooked
        mcp = mcpIn;        //(vars are also updated in Sink class)
        foodName = foodNameIn;

        updateVars();   //update background width/height, etc.

        //initialize position of burner vars
        burnerX = panelWidth/2 + (int)(250 * (double)backgroundWidth/600);  //initialize dimensions of burner, depend on size of stove image
        burnerY = (int)(80 * (double)backgroundHeight/314);
        burnerWidth = (int)(136 * (double)backgroundWidth/600);
        burnerHeight = (int)(150 * (double)backgroundHeight/314);

        //same for position of dial
        dialX = panelWidth/2 + (int)(497 * (double)backgroundWidth/600);   //dimensions of oven handle depending on size of oven pic
        dialY = (int)(163 * (double)backgroundHeight/314);
        dialWidth = (int)(58 * (double)backgroundWidth/600);
        dialHeight = (int)(55 * (double)backgroundHeight/314);

        potPan = "pot";

        if (foodNameIn.equals("Salmon") ||
                foodNameIn.equals("Mushrooms") || foodNameIn.equals("Green beans"))   //pan foods
        {
            potPan = "pan";
        }
        //update image names

        containerName = potPan + ".jpg";
        backgroundName = "stove.jpg";
        startFoodName = "cut" + foodName + ".jpg";	//specific to food
        endFoodName = "cooked" + foodName + ".jpg";
        endContainerName = "plate.jpg";

        //update directions to be printed
        directions = new String [] {"Drag the " + potPan + " onto the front " +
                "right burner of the stove. ",
                "Great! Now turn the stove on by pressing the front dial.",
                " Drag the " + foodName + " into the " + potPan + ". "
                , "Click on the " + foodName + " to fast forward until "
				+ " it is done.", "The " + foodName +
                " is done! Click the front dial to turn off the stove.",
                 "Now drag the " + foodName + " out of the "
                + potPan + " and onto the plate. "};
        getMyImage();
    }


    //mouseClicked: see if mouse is clicked on the button to turn the stove on
    public void mouseClicked( MouseEvent evt )
    {
        super.mouseClicked(evt);

        //if handle of stove is clicked
        boolean dialClicked = (mouseX >= dialX && mouseX <= dialX + dialWidth )
                && (mouseY >= dialY && mouseY <= dialY + dialHeight );

		boolean foodClicked = (mouseX >= foodX && mouseX <= foodX + foodWidth )
                && (mouseY >= foodY && mouseY <= foodY + foodHeight );

        //if mouse is clicked within the region of the stove on button, and the user
        // is supposed to (to turn it on or off), increment the food step number
        if (  (cookStepNum == 1 || cookStepNum == 4) && dialClicked  )
        {
            cookStepNum++;
            repaint();
        }
        //THIS IS TEMPORARY until timer is integrated
        if ( cookStepNum == 3 && foodClicked)
        {
            foodDone = true;
            cookStepNum++;
            repaint();
        }
    }

    //mouseReleased: see if objects are dragged/dropped to the right places
    public void mouseReleased (MouseEvent evt)
    {
        super.mouseReleased(evt);

        //if pot or pan is dragged onto the stove
        boolean onStove = ( containerX >= burnerX &&
                containerX  <= burnerX + burnerWidth) &&
                (containerY >= burnerY && containerY <=
                        burnerY + burnerHeight);

        //if user moved objects to where they were supposed to,
        //increment the step number and change the directions
        if ( (onStove && cookStepNum == 0) ||
                (foodInContainer && cookStepNum == 2) )
        {
            cookStepNum++;
            repaint();
        }
    }
}

