package edu.ucsb.cs56.projects.games.minesweeper;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by ryanwiener on 11/4/17.
 */

public class GuiTest {

    private Robot robot;

    @Before
    public void setUpMainMenu() {
        try {
            robot = new Robot();
            robot.setAutoDelay(500);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        MineGUI.main(new String[0]);
    }

    @Test
    public void testInitialMainMenu() {
        assertTrue(MineGUI.getCurrentFrame() instanceof MainMenu);
    }

    @Test
    public void testEasyGameButton() {
        robot.mouseMove(50, 100);
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 10);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testLoadGameButtonEasy() {
        robot.mouseMove(350, 300);
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 10);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testMediumGameButton() {
        robot.mouseMove(350, 100);
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 15);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testLoadGameButtonMedium() {
        robot.mouseMove(350, 300);
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 15);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testHardGameButton() {
        robot.mouseMove(50, 300);
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 20);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testLoadGameButton() {
        robot.mouseMove(350, 300);
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 20);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testHelpButton() {
        robot.mouseMove(50, 400);
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof HelpScreen);
    }

    @Test
    public void testHelpBackButton() {
        robot.mouseMove(150, 500);
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof MainMenu);
    }

    @Test
    public void testGameFrameGridFlag() {
        MineGUI.newGame(-1);
        robot.mouseMove(50, 200);
        rightClick();
        assertTrue(((GameFrame) MineGUI.getCurrentFrame()).getGrid().isFlag(0));
        MineGUI.goToMainMenu();
    }

    @Test
    public void testGameFrameGridRefresh() {
        MineGUI.newGame(-1);
        robot.mouseMove(50, 200);
        rightClick();
        robot.mouseMove(400, 60);
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        robot.delay(100);
        assertTrue(!((GameFrame) MineGUI.getCurrentFrame()).getGrid().isFlag(0));
        MineGUI.goToMainMenu();
    }

    @Test
    public void testGameFrameGridOpen() {
        MineGUI.newGame(-1);
        robot.mouseMove(250, 200);
        leftClick();
        assertTrue(((GameFrame) MineGUI.getCurrentFrame()).getGrid().isOpen(1));
        MineGUI.goToMainMenu();
    }

    public void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void typeKey(int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
    }
}
