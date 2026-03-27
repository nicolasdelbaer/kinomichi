package be.nidel.utils;

public class OutputUtils
{
    // Source - https://stackoverflow.com/a/5762502
    // Posted by WhiteFang34, modified by community. See post 'Timeline' for change history
    // Retrieved 2026-03-10, License - CC BY-SA 3.0
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BOLD = "\u001B[1;30m";
    public static final String ANSI_RED_BOLD = "\u001B[1;31m";
    public static final String ANSI_GREEN_BOLD = "\u001B[1;32m";
    public static final String ANSI_YELLOW_BOLD = "\u001B[1;33m";
    public static final String ANSI_BLUE_BOLD = "\u001B[1;34m";
    public static final String ANSI_PURPLE_BOLD = "\u001B[1;35m";
    public static final String ANSI_CYAN_BOLD = "\u001B[1;36m";
    public static final String ANSI_WHITE_BOLD = "\u001B[1;37m";

    public static final String ANSI_BLACK_ITALIC = "\u001B[3;30m";
    public static final String ANSI_RED_ITALIC = "\u001B[3;31m";
    public static final String ANSI_GREEN_ITALIC = "\u001B[3;32m";
    public static final String ANSI_YELLOW_ITALIC = "\u001B[3;33m";
    public static final String ANSI_BLUE_ITALIC = "\u001B[3;34m";
    public static final String ANSI_PURPLE_ITALIC = "\u001B[3;35m";
    public static final String ANSI_CYAN_ITALIC = "\u001B[3;36m";
    public static final String ANSI_WHITE_ITALIC = "\u001B[3;37m";

    // Source - https://stackoverflow.com/a/5762502
    // Posted by WhiteFang34, modified by community. See post 'Timeline' for change history
    // Retrieved 2026-03-10, License - CC BY-SA 3.0
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void sOutTitle(String s) {
        System.out.printf("%s%s%s%s%n", OutputUtils.ANSI_WHITE_BACKGROUND, OutputUtils.ANSI_BLACK, s, OutputUtils.ANSI_RESET);
    }
    public static void sOut(String s) {
        System.out.printf("%s%s%s%n", OutputUtils.ANSI_YELLOW, s, OutputUtils.ANSI_RESET);
    }
    public static void sOutWarning(String s) {
        System.out.printf("%s%s%s%n", OutputUtils.ANSI_YELLOW, s, OutputUtils.ANSI_RESET);
    }
    public static void sOutError(String s) {
        System.out.printf("%s%s%s%n", OutputUtils.ANSI_RED, s, OutputUtils.ANSI_RESET);
    }
    public static void sOutInfo(String s) {
        System.out.printf("%s%s%s%n", OutputUtils.ANSI_BLUE_ITALIC, s, OutputUtils.ANSI_RESET);
    }
}
