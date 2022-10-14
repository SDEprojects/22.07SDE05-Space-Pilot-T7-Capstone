public class Game {

  public static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void printTitle() {
    System.out.println(
        "     _______..______      ___       ______  _______       .______    __   __        ______   .___________.\n"
            +
            "    /       ||   _  \\    /   \\     /      ||   ____|      |   _  \\  |  | |  |      /  __  \\  |           | _\n"
            +
            "   |   (----`|  |_)  |  /  ^  \\   |  ,----'|  |__         |  |_)  | |  | |  |     |  |  |  | `---|  |----`(_)\n"
            +
            "    \\   \\    |   ___/  /  /_\\  \\  |  |     |   __|        |   ___/  |  | |  |     |  |  |  |     |  |\n"
            +
            ".----)   |   |  |     /  _____  \\ |  `----.|  |____       |  |      |  | |  `----.|  `--'  |     |  |      _\n"
            +
            "|_______/    | _|    /__/     \\__\\ \\______||_______|      | _|      |__| |_______| \\______/      |__|     (_)\n"
            +
            " __    __    ______   .___  ___.  _______ .______     ______    __    __  .__   __.  _______\n"
            +
            "|  |  |  |  /  __  \\  |   \\/   | |   ____||   _  \\   /  __  \\  |  |  |  | |  \\ |  | |       \\  \n"
            +
            "|  |__|  | |  |  |  | |  \\  /  | |  |__   |  |_)  | |  |  |  | |  |  |  | |   \\|  | |  .--.  |\n"
            +
            "|   __   | |  |  |  | |  |\\/|  | |   __|  |   _  <  |  |  |  | |  |  |  | |  . `  | |  |  |  |\n"
            +
            "|  |  |  | |  `--\'  | |  |  |  | |  |____ |  |_)  | |  `--\'  | |  `--\'  | |  |\\   | |  \'--\'  |\n"
            +
            "|__|  |__|  \\______/  |__|  |__| |_______||______/   \\______/   \\______/  |__| \\__| |_______/\n");

  }

  public static void showBackgroundInfoAndGameInstructions() {
    System.out.println("*ring ring, ring ring*\n"
        + "A few months after his retirement, former Space Force Astronaut \n"
        + "Colonel Walker receives a call from NASA. He is asked to do \n"
        + "his one last dance: a Search & Rescue mission; he must rescue \n"
        + "astronauts stuck throughout our solar system. The international \n"
        + "community manages to cooperate and quickly construct one special \n"
        + "spacecraft – but time is not on Walker’s side. Colonel Walker must \n"
        + "rescue every stranded astronaut and safely bring them home in just \n"
        + "one trip. NASA expects Col. Walker to bring back at least ⅘ (four-fifths) \n"
        + "of the stranded astronauts (overpopulation, sorry).\n"
        + "__________________________________________________________________________\n"
        + "Game Instructions \n"
        + "Col. Walker starts off on Planet Earth. Help him move around the solar \n"
        + "system by using the \"go\" command. For example:\n"
        + "   - go to the moon\n"
        + "   - go to Mars\n"
        + "   - go to Mercury\n"
        + "   - go to Earth"
        + "In case the spacecraft is damaged, you can repair it if you have more than\n"
        +"one engineer onboard. If the spacecraft's health turns 0, it will explode."
        + "Use the following command to repair the spacecraft:\n"
        + "   - repair\n"
        + "As Col. Walker hops from planet to planet, he must load passengers – and \n"
        + "eventually unload them on planet Earth. Loading and unloading passengers \n"
        + "can be done using the following commands:\n"
        + "   - load\n"
        + "   - unload\n"
        //TODO: MEDIOCRE INSTRUCTIONS -- UPDATE LATER ONCE CODING COMPLETE
    );
  }

}

