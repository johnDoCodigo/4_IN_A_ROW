package academy.mindswap.server.commands;


public enum Command {
    HELP("/help", new HelpHandler()),
    QUIT("/quit", new QuitHandler()),
    PLAYAGAIN("/playagain", new PlayAgainHandler());


    private String description;
    private CommandHandler handler;

    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return null;
    }
    public CommandHandler getHandler() {
        return handler;
    }
}
