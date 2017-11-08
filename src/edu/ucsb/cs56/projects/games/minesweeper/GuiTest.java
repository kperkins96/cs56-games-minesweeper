package edu.ucsb.cs56.projects.games.minesweeper;

import org.junit.Test;
import org.junit.BeforeClass;
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

    private static Robot robot;

    @BeforeClass
    public static void setUpMainMenu() {
        try {
            robot = new Robot();
            robot.setAutoDelay(200);
            robot.setAutoWaitForIdle(false);
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
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getEasyGameX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getEasyGameY());
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 10);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testLoadGameButtonEasy() {
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getLoadGameX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getLoadGameY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 10);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testMediumGameButton() {
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getMedGameX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getMedGameY());
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 15);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testLoadGameButtonMedium() {
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getLoadGameX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getLoadGameY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 15);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testHardGameButton() {
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getHardGameX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getHardGameY());
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 20);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testLoadGameButton() {
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getLoadGameX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getLoadGameY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame && ((GameFrame) MineGUI.getCurrentFrame()).getGrid().getSize() == 20);
        MineGUI.goToMainMenu();
    }

    @Test
    public void testHelpButton() {
        MainMenu menu = (MainMenu) MineGUI.getCurrentFrame();
        robot.mouseMove(menu.getX() + menu.getHelpX(), menu.getY() + (menu.getHeight() - menu.getContentPane().getHeight()) + menu.getHelpY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof HelpScreen);
    }

    @Test
    public void testHelpBackButtonMain() {
        HelpScreen help = (HelpScreen) MineGUI.getCurrentFrame();
        robot.mouseMove(help.getX() + help.getBackX(), help.getY() + (help.getHeight() - help.getContentPane().getHeight()) + help.getBackY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof MainMenu);
    }

    @Test
    public void testGameFrameGridFlag() {
        MineGUI.newGame(Grid.Difficulty.TEST);
        GameFrame game = (GameFrame) MineGUI.getCurrentFrame();
        robot.mouseMove(game.getX() + game.getGridButtonX(0, 0), game.getY() + (game.getHeight() - game.getContentPane().getHeight()) + game.getGridButtonY(0, 0));
        rightClick();
        assertTrue(((GameFrame) MineGUI.getCurrentFrame()).getGrid().isFlag(0, 0));
    }

    @Test
    public void testGameFrameGridDeflag() {
        GameFrame game = (GameFrame) MineGUI.getCurrentFrame();
        robot.mouseMove(game.getX() + game.getGridButtonX(0, 0), game.getY() + (game.getHeight() - game.getContentPane().getHeight()) + game.getGridButtonY(0, 0));
        rightClick();
        assertTrue(!((GameFrame) MineGUI.getCurrentFrame()).getGrid().isFlag(0, 0));
    }

    @Test
    public void testGameFrameGridOpen() {
        GameFrame game = (GameFrame) MineGUI.getCurrentFrame();
        robot.mouseMove(game.getX() + game.getGridButtonX(0, 1), game.getY() + (game.getHeight() - game.getContentPane().getHeight()) + game.getGridButtonY(0, 1));
        leftClick();
        assertTrue(((GameFrame) MineGUI.getCurrentFrame()).getGrid().isOpen(0, 1));
    }

    @Test
    public void testGameFrameRefresh() {
        GameFrame game = (GameFrame) MineGUI.getCurrentFrame();
        robot.mouseMove(game.getX() + game.getRefreshX(), game.getY() + (game.getHeight() - game.getContentPane().getHeight()) + game.getRefreshY());
        leftClick();
        typeKey(KeyEvent.VK_ENTER);
        game = (GameFrame) MineGUI.getCurrentFrame();
        boolean open = false;
        for (int i = 0; i < game.getGrid().getSize(); i++) {
            for (int j = 0; j < game.getGrid().getSize(); j++) {
                if (game.getGrid().isOpen(i, j)) {
                    System.out.println(i + " " + j);
                    open = true;
                    break;
                }
            }
        }
        assertFalse(open);
    }

    @Test
    public void testGameFrameHelp() {
        GameFrame game = (GameFrame) MineGUI.getCurrentFrame();
        robot.mouseMove(game.getX() + game.getHelpX(), game.getY() + (game.getHeight() - game.getContentPane().getHeight()) + game.getHelpY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof HelpScreen);
    }

    @Test
    public void testHelpBackButtonGame() {
        HelpScreen help = (HelpScreen) MineGUI.getCurrentFrame();
        robot.mouseMove(help.getX() + help.getBackX(), help.getY() + (help.getHeight() - help.getContentPane().getHeight()) + help.getBackY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof GameFrame);
    }

    @Test
    public void testGameFrameMainMenu() {
        GameFrame game = (GameFrame) MineGUI.getCurrentFrame();
        robot.mouseMove(game.getX() + game.getMainMenuX(), game.getY() + (game.getHeight() - game.getContentPane().getHeight()) + game.getMainMenuY());
        leftClick();
        assertTrue(MineGUI.getCurrentFrame() instanceof MainMenu);
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
