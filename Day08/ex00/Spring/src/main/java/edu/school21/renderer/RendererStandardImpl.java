package edu.school21.renderer;

import edu.school21.preprocessor.PreProcessor;

public class RendererStandardImpl implements Renderer {
    private PreProcessor preprocessor;

    public RendererStandardImpl(PreProcessor preprocessor){
        this.preprocessor = preprocessor;
    }

    @Override
    public void render(String msg) {
        System.out.println(preprocessor.process(msg));
    }
}
