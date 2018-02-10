import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

public class DropRateCalculator {

    public static int white, green, blue, purple, yellow, red, unidentified;

    public BufferedImage getScreenshot() {



        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);
            Robot robot = new Robot();
            return robot.createScreenCapture(screenRectangle);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        white = 0;
        green = 0;
        blue = 0;
        purple = 0;
        yellow = 0;
        red = 0;
        unidentified = 0;

        //set the output
        try {
            System.setOut(new PrintStream(Paths.get("C:\\Users\\Alex\\Desktop\\Memes\\Screenshots\\results.txt").toFile())); //your path goes here
            System.out.println("System output:");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //give myself 3 seconds to set up
        //to set up just open the game in fullscreen
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DropRateCalculator cs = new DropRateCalculator();

        long startTime = System.currentTimeMillis();

        //change this to set how many times it loops
        for (int i = 0; i < 10000; i++) {
            if (i != 0) {
                BufferedImage image = cs.getScreenshot();
                try {
                    ImageIO.write(image, "png", new java.io.File("C:\\Users\\Alex\\Desktop\\Memes\\Screenshots\\Screenshot_1.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tallyResults(image);
            }

            //click
            Robot bot;
            try {
                bot = new Robot();
                int mask = InputEvent.BUTTON1_MASK;
                bot.mouseMove(960, 960);
                bot.mousePress(mask);
                bot.mouseRelease(mask);
                Thread.sleep(300);
                bot.mousePress(mask);
                bot.mouseRelease(mask);
            } catch (AWTException | InterruptedException e) {
                e.printStackTrace();
            }


            //wait for next round of items
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //give progress every 100 in case program fails to complete
            if (i % 100 == 0 && i != 0){
                double total = white + green + blue + purple + yellow + red + unidentified;

                System.out.println();
                System.out.println("\nTotals at "+i+" samples:");
                System.out.println("\nWhite: "+white+"("+(white/total) * 100 + "%)");
                System.out.println("\nGreen: "+green+"(" +(green/total) * 100 + "%)");
                System.out.println("\nBlue: "+blue+"(" +(blue/total) * 100 + "%)");
                System.out.println("\nPurple: "+purple+"("+(purple/total) * 100 +"%)");
                System.out.println("\nYellow: "+yellow+"("+(yellow/total) * 100 +"%)");
                System.out.println("\nRed: "+red + "("+(red/total) * 100 +"%)");
                System.out.println("Unidentified: " + unidentified + "(" + (unidentified / total) * 100 + "%)");
            }

        }

        //print the results
        double total = white + green + blue + purple + yellow + red + unidentified;

        System.out.println("");
        System.out.println("\nFinal totals at "+(int)total+" samples:");
        System.out.println("\nWhite: "+white+"("+(white/total) * 100 + "%)");
        System.out.println("\nGreen: "+green+"(" +(green/total) * 100 + "%)");
        System.out.println("\nBlue: "+blue+"(" +(blue/total) * 100 + "%)");
        System.out.println("\nPurple: "+purple+"("+(purple/total) * 100 +"%)");
        System.out.println("\nYellow: "+yellow+"("+(yellow/total) * 100 +"%)");
        System.out.println("\nRed: "+red + "("+(red/total) * 100 +"%)");
        System.out.println("Unidentified: " + unidentified + "(" + (unidentified / total) * 100 + "%)");
        System.out.println("");
        System.out.println("\nTime in seconds: " + (System.currentTimeMillis() - startTime) / 1000.);
    }

    private static void tallyResults(BufferedImage image) {


        //get the colors from the image. the position of the rarity icon seems to change after restart so this needs to be adjusted each time.
        // TODO: 2/9/2018 Better way of finding the rarity icons programmatically, probably using image recognition libraries.

        //commented out lines here were for debugging, but I've left them in anyway, just in case anyone wants to see them.

        //the coordinates here represent the location of each rarity icon. These currently need to be set each time the game opens.
        //if you want to do it yourself, enter the coordinates corresponding to the top left corner (leave a few pixels on each side) of a pixel on each rarity icon.

        Color color = new Color(image.getRGB(188, 736));
        //System.out.println("Color 1 = R" + color.getRed() + " G" + color.getGreen() + " B" + color.getBlue());
        determineColor(color);

        color = new Color(image.getRGB(638, 727));
        //System.out.println("Color 2 = R" + color.getRed() + " G" + color.getGreen() + " B" + color.getBlue());
        determineColor(color);

        color = new Color(image.getRGB(1055, 738));
        //System.out.println("Color 3 = R" + color.getRed() + " G" + color.getGreen() + " B" + color.getBlue());
        determineColor(color);

        color = new Color(image.getRGB(1440, 728));
        //System.out.println("Color 4 = R" + color.getRed() + " G" + color.getGreen() + " B" + color.getBlue());
        determineColor(color);


    }

    private static void determineColor(Color color) {

        /*Possible colors
        * White
        * Green
        * Blue
        * Purple
        * Yellow
        * Red
        * */

        int r = color.getRed();
        int b = color.getBlue();
        int g = color.getGreen();

        //test to see if it is white
        if (r > 230 && g > 230 && b > 230){
            //System.out.println("Pixel is white");
            white++;
            return;
        }
        //test to see if it is green
        if (g > 200 && b < 70 && (r < 160 && r > 100)){
            //System.out.println("Pixel is green");
            green++;
            return;
        }
        //test to see if it is blue
        if (r < 60 && (g > 150 && g < 200) && b > 200){
            //System.out.println("Pixel is blue");
            blue++;
            return;
        }
        //test to see if it is purple
        if ((r > 150 && r < 170) && g < 75 && b > 225){
            //System.out.println("Pixel is purple");
            purple++;
            return;
        }
        //test to see if it is yellow
        if (r > 230 && (g < 160 && g > 130) && b < 60){
            //System.out.println("Pixel is yellow");
            yellow++;
            return;
        }
        //test to see if it is red
        if (r > 225 && g < 50 && b < 60){
            //System.out.println("Pixel is red");
            red++;
            return;
        }

        //should never happen unless positions are off, but record it anyway.
        System.out.println("Could not identify pixel color...");
        unidentified ++;


    }

}