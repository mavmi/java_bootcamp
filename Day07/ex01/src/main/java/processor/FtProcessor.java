package processor;

import annotation.HtmlForm;
import annotation.HtmlInput;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
//@SupportedAnnotationTypes({"annotation.HtmlForm", "annotation.HtmlInput"})
@SupportedAnnotationTypes("annotation.HtmlForm")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FtProcessor extends AbstractProcessor {

    // Set<? extends TypeElement> annotations - set of annotations
    // RoundEnvironment roundEnv - information about the current processing round
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element cls : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            List<String> content = new ArrayList<>();

            HtmlForm htmlForm = cls.getAnnotation(HtmlForm.class);
            String fileName = htmlForm.fileName();
            content.add(
                    new StringBuilder()
                        .append("<form action = \"")
                        .append(htmlForm.action())
                        .append("\" method = \"")
                        .append(htmlForm.method())
                        .append("\">").
                        toString()
            );

            List<? extends Element> clsElems = cls.getEnclosedElements();
            for (Element field : roundEnv.getElementsAnnotatedWith(HtmlInput.class)){
                if (!clsElems.contains(field)) continue;
                HtmlInput htmlInput = field.getAnnotation(HtmlInput.class);
                content.add(
                        new StringBuilder()
                            .append("\t<input type = \"")
                            .append(htmlInput.type())
                            .append("\" name = \"")
                            .append(htmlInput.name())
                            .append("\" placeholder = \"")
                            .append(htmlInput.placeholder())
                            .append("\">")
                            .toString()
                );
            }

            content.add("</form>");
            writeToFile(fileName, content);
        }
        return true;
    }

    private void writeToFile(String fileName, List<String> content){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("target/classes/" + fileName))) {
            for (String line : content){
                writer.write(line);
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
