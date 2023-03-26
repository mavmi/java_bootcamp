package edu.school21.printer;

import edu.school21.renderer.Renderer;

public class PrinterWithPrefixImpl implements Printer{
    private Renderer renderer;
    private String prefix;

    public PrinterWithPrefixImpl(Renderer renderer){
        this.renderer = renderer;
        this.prefix = "PREFIX";
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    @Override
    public void print(String msg) {
        renderer.render(prefix + " " + msg);
    }
}
