package edu.school21.app;

import edu.school21.printer.*;
import edu.school21.renderer.*;
import edu.school21.preprocessor.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        {
            Printer printer = context.getBean("printerWithPrefix", Printer.class);
            printer.print("Hello!");
        }
        {
            Printer printer = context.getBean("printerWithDateTime", Printer.class);
            printer.print("Hello!");
        }
        {
            PreProcessor preProcessor = new PreProcessorToLowerImpl();
            Renderer renderer = new RendererStandardImpl(preProcessor);
            PrinterWithDateTimeImpl printer = new PrinterWithDateTimeImpl(renderer);
            printer.print("Hello!");
        }
        {
            PreProcessor preProcessor = new PreProcessorToUpperImpl();
            Renderer renderer = new RendererErrImpl(preProcessor);
            PrinterWithPrefixImpl printer = new PrinterWithPrefixImpl(renderer);
            printer.setPrefix("Prefix");
            printer.print("Hello!");
        }
    }
}
