package ru.spbau.mit.sd;

class Exit extends Factory {

    /**
     * exit from command line interpreter
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    @Override
    public void execute(IOStream ios, String param) {
        System.exit(0);
    }
}

