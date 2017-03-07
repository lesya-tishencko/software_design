package ru.spbau.mit.sd;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {

    private Vector<String> commands = new Vector<>();
    private Vector<String> params = new Vector<>();
//    private Vector<String> options = new Vector<>(); // TODO

    /**
     * parse line to get Vector of commands and their parameters
     * @param line String which input by user
     */
    void parse(String line) {
        parserReset();
        String pipe = "[\\s]?+\\|+[\\s]?+";
        for (String aTemp : line.split(pipe)) {
            parseCommand(aTemp);
        }
    }

    private void parserReset() {
        commands.clear();
        params.clear();
//        options.clear();
    }
    private void parseCommand(String str) {
        Matcher matcher = Pattern.compile("^[\\s]?\\w+").matcher(str);
        if (matcher.find()) {
            commands.add(str.substring(matcher.start(), matcher.end()).trim());
            params.add(str.substring(matcher.end()).trim());
        }

    }

    Vector<String> getCommands() {
        return commands;
    }

    Vector<String> getParams() {
        return params;
    }

}

