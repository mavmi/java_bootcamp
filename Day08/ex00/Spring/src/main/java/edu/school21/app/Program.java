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
            PreProcessor preProcessor = new PreProcessorToLowerImpl();
            Renderer renderer = new RendererStandardImpl(preProcessor);
            PrinterWithDateTimeImpl printer = new PrinterWithDateTimeImpl(renderer);
            printer.print("1a");
        }
        {
            PreProcessor preProcessor = new PreProcessorToUpperImpl();
            Renderer renderer = new RendererErrImpl(preProcessor);
            PrinterWithPrefixImpl printer = new PrinterWithPrefixImpl(renderer);
            printer.setPrefix("Prefixxx");
            printer.print("22aA");
        }
        {
            Printer printer = context.getBean("printerDateTimeStdToLower", Printer.class);
            printer.print("333aAa");
        }
        {
            Printer printer = context.getBean("printerDateTimeErrToUpper", Printer.class);
            printer.print("4444aAAa");
        }
    }
}
