package ru.spbau.mit.sd;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Pattern patternForQuotes = Pattern.compile("([\"\']).*?\\1");
    private static Pattern patternForGetVar = Pattern.compile("\\w+=\\w+");
    private Vector<String> commands = new Vector<>();
    private Vector<String> params = new Vector<>();
    private int[] pipePos = null;
    /**
     * parse line to get Vector of commands and their parameters
     * @param line String which input by user
     */
    public void parse(String line) {
        parserReset();
        String pipe = "[\\s]?+\\|+[\\s]?+";
        pipePos = new int[line.split(pipe).length];
        int old = 0;
        int i = 0;
        for (String aTemp : line.split(pipe)) {
            parsePipe(aTemp);
            int count = commands.size() - old;
            old += count;
            pipePos[i] = count;
            ++i;
        }
    }

    private void parserReset() {
        commands.clear();
        params.clear();
    }

    private void parsePipe(String str) {
        String colon = "[\\s]?+;[\\s]?+";
        for (String command : str.split(colon)) {
            parseCommand(command);
        }
    }

    private String getAssignment(String line) {
        Matcher matchVar = patternForGetVar.matcher(line);
        int varStart = 0;
        Matcher matchQuotes = patternForQuotes.matcher(line);
        boolean hasQuotes = matchQuotes.find();

        while (matchVar.find(varStart)) {
            if (hasQuotes && matchVar.start() > matchQuotes.start()
                    && matchVar.end() < matchQuotes.end()) {
                varStart = matchVar.end();
                continue;
            } else {
                if (hasQuotes && matchVar.start() > matchQuotes.end()) {
                    hasQuotes = matchQuotes.find(matchQuotes.end());
                    continue;
                }
            }
            String temp = line.substring(matchVar.start(), matchVar.end()).trim();
            line = line.substring(0, matchVar.start()).concat(line.substring(matchVar.end()));
            varStart = matchVar.start();
            matchVar = matchVar.reset(line);
            int ind = temp.indexOf("=");
            commands.add("assignment");
            params.add(temp.substring(0, ind).trim() + " " + temp.substring(ind + 1).trim());
        }
        return line.trim();
    }

    private void parseCommand(String str) {
        str = getAssignment(str);
        Matcher matcher = Pattern.compile("^[\\s]?\\w+").matcher(str);
        if (matcher.find()) {
            commands.add(str.substring(matcher.start(), matcher.end()).trim());
            params.add(str.substring(matcher.end()).trim());
        }
    }

    public int[] getPipePos() {
        return pipePos;
    }

    public Vector<String> getCommands() {
        return commands;
    }

    public Vector<String> getParams() {
        return params;
    }

}

